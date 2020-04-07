package ke.co.ximmoz.fleet;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.DocumentReference;
import com.labo.kaji.fragmentanimations.CubeAnimation;
import com.labo.kaji.fragmentanimations.MoveAnimation;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.Utils.ConsignmentTransactionCounter;
import ke.co.ximmoz.fleet.views.Utils.Shard;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link ConsignmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsignmentFragment extends Fragment implements RoutingListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ConsignmentViewmodel consignmentViewmodel;
    private NavController navController;
    private AutocompleteSupportFragment destinationSupportFragment,pickupSupportFragment;
    private String date;
    private Date todayDate;
    private DatePickerDialog datePickerDialog;
    private LatLng destination_location,pickup_location;


    @BindView(R.id.container)
    Spinner container;
    @BindView(R.id.date_of_pickup)
    TextView dateOfPickup;
    @BindView(R.id.to_payment)
    ImageView toPayment;
    @BindView(R.id.progress_dialog)
    LinearLayout progress_dialog;

    private Unbinder unbinder;


    public ConsignmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsignmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsignmentFragment newInstance(String param1, String param2) {
        ConsignmentFragment fragment = new ConsignmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_api_key));
        }
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);

        todayDate=new Date();
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getResources().getString(R.string.google_api_key));

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_consignment,container,false);
        navController= Navigation.findNavController(container);
        unbinder= ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ArrayAdapter<CharSequence> containerSize = ArrayAdapter.createFromResource(getActivity(), R.array.container_size, R.layout.spinner_items);
        containerSize.setDropDownViewResource(R.layout.spinner_dropdown_item);
        container.setAdapter(containerSize);
        pickupSupportFragment=(AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_delivery);
        destinationSupportFragment=(AutocompleteSupportFragment)getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_destination);


        pickupSupportFragment.a.setText(consignmentViewmodel.consignment.getPickupName());
        destinationSupportFragment.a.setText(consignmentViewmodel.consignment.getDestinationName());
        dateOfPickup.setText(consignmentViewmodel.consignment.getDate_of_pickup());
        if(consignmentViewmodel.consignment.getContainer_size()!=null)
        {
            container.setSelection(containerSize.getPosition(consignmentViewmodel.consignment.getContainer_size()));
            int positionOfSelected=containerSize.getPosition(consignmentViewmodel.consignment.getContainer_size());
            container.setSelection(positionOfSelected);
        }

        pickupSupportFragment.a.setTextSize(13.0f);
        destinationSupportFragment.a.setTextSize(13.0f);
        pickupSupportFragment.a.setTextColor(getResources().getColor(R.color.white));
        destinationSupportFragment.a.setTextColor(getResources().getColor(R.color.white));

        pickupSupportFragment.setPlaceFields(Arrays.asList(Place.Field.NAME,Place.Field.ADDRESS,Place.Field.ID,Place.Field.LAT_LNG));
        destinationSupportFragment.setPlaceFields(Arrays.asList(Place.Field.NAME,Place.Field.ADDRESS,Place.Field.ID,Place.Field.LAT_LNG));



        /*
        *
        *
        * Getting pickup location of the consignment
        * */
        pickupSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                consignmentViewmodel.consignment.setPickupAddress(place.getAddress());
                consignmentViewmodel.consignment.setPickupName(place.getName());
                LatLng pickupPoints=place.getLatLng();
                pickup_location=pickupPoints;
                consignmentViewmodel.consignment.setPickupLat(pickupPoints.latitude);
                consignmentViewmodel.consignment.setPickupLng(pickupPoints.longitude);

            }

            @Override
            public void onError(@NonNull Status status) {
                return;
            }
        });



        destinationSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                consignmentViewmodel.consignment.setDestinationAddress(place.getAddress());
                consignmentViewmodel.consignment.setDestinationName(place.getName());
                LatLng destinationPoints=place.getLatLng();
                destination_location=destinationPoints;
                consignmentViewmodel.consignment.setDestinationLat(destinationPoints.latitude);
                consignmentViewmodel.consignment.setDestinationLng(destinationPoints.longitude);
            }

            @Override
            public void onError(@NonNull Status status) {

                return;
            }
        });



        dateOfPickup.setOnClickListener(v -> {

            Calendar calendar=Calendar.getInstance();

            datePickerDialog=new DatePickerDialog(getActivity(),onDateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });




        toPayment.setOnClickListener((v)-> {

            if (destination_location != null && pickup_location != null) {
                progress_dialog.setVisibility(View.VISIBLE);
                String api = getResources().getString(R.string.google_api_key);
                Routing routing = new Routing.Builder()
                        .key(api)
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .waypoints(pickup_location, destination_location)
                        .withListener(this)
                        .alternativeRoutes(false)
                        .build();
                routing.execute();
            }
            else
            {
                Toast.makeText(getContext(), "All fields required to proceed", Toast.LENGTH_SHORT).show();
            }

        });

    }


    DatePickerDialog.OnDateSetListener onDateSetListener= (view, year, month, dayOfMonth) -> {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        date= DateFormat.getDateInstance().format(calendar.getTime());

        if(calendar.getTime().before(todayDate))
        {

            AlertDialog.Builder alerDialog=new AlertDialog.Builder(getContext())
                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DatePickerDialog datePickerDialog=new DatePickerDialog(getActivity(),onDateSetListener,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                            datePickerDialog.show();
                        }
                    })
                    .setTitle("Invalid Date")
                    .setMessage("The date should be today or a future date.");
            alerDialog.show();
        }
        else
        {
            consignmentViewmodel.consignment.setDate_of_pickup(date);
            dateOfPickup.setText(date);
        }

    };

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter) {
//            return CubeAnimation.create(CubeAnimation.DOWN, enter, 500);
            return MoveAnimation.create(MoveAnimation.UP, enter, 500);
        } else {
            return MoveAnimation.create(MoveAnimation.LEFT, enter, 500);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getActivity(), "Something Went Wrong Try Again Later", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        String distance = null;
        for(int i=0;i<route.size();i++)
        {
            distance=route.get(i).getDistanceText();

        }

       consignmentViewmodel.consignment.setDistance(distance);

        consignmentViewmodel.consignment.setContainer_size(container.getSelectedItem().toString());

        progress_dialog.setVisibility(View.VISIBLE);
        consignmentViewmodel.SaveConsignment().observe(getViewLifecycleOwner(), s -> {
            if(s=="1")
            {
                if(navController.getCurrentDestination().getId()==R.id.consignmentFragment)
                {
                    progress_dialog.setVisibility(View.GONE);
                    NavDirections directions=ConsignmentFragmentDirections.actionConsignmentFragmentToSampleFragmentDialog(consignmentViewmodel.consignment);
                    navController.navigate(directions);
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRoutingCancelled() {
        Toast.makeText(getActivity(), "Sorry Routing Cancelled", Toast.LENGTH_SHORT).show();
    }

    /*
    * TODO initialize a counter and number of shards if they are not yet initialized
    *
    * */





}
