package ke.co.ximmoz.fleet.Views;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.Views.Utils.DatePickerFrag;

import static ke.co.ximmoz.fleet.Views.FleetRequestsActivity.hasPermissions;

public class DestinationChooserActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private static final int AUTOCOMPLETE_LOCATION_DESTINATION = 20;
    private GoogleMap mMap;
    private MarkerOptions markerOptions=new MarkerOptions();
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button confirmConsignment;
    int PERMISSION_ALL=1;
    Unbinder unbinder;


    @BindView(R.id.main_pop_up)
    ConstraintLayout description;
    @BindView(R.id.pick_location)
    Spinner pick_location;
    @BindView(R.id.delivery_location)
    TextView delivery_location;
    @BindView(R.id.getMeATruck) Button getMeATruck;
    @BindView(R.id.containerDimensions) Spinner containerDimensions;




    LatLng destination_location=null;
    String pickup_location=null;
    String container_size=null;

    private void   finishConsignment (View v){


        if(destination_location==null||pickup_location==null||container_size==null){
            Toast.makeText(this, "All fields required to proceed", Toast.LENGTH_SHORT).show();
            return;
        }
        consignment.setStatus("notpaid");
        consignment.setPickup_location(pickup_location);
        consignment.setDestination_lat(destination_location.latitude);
        consignment.setDestination_lng(destination_location.longitude);
        consignment.setContainer_size(container_size);

        SaveConsignment(consignment);
    }

    private void GetContainer() {
        consignment.setContainer_size(containerDimensions.getSelectedItem().toString());
        SaveConsignment(consignment);
    }

    private void GetPickupDate() {

        DialogFragment dialogFragment=new DatePickerFrag();
        dialogFragment.show(getSupportFragmentManager(),"date picker");


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);
        unbinder=ButterKnife.bind(this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        pick_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pickup_location=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        containerDimensions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container_size=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if(!Places.isInitialized())
        {
            Places.initialize(getApplicationContext(),getResources().getString(R.string.google_api_key));
        }
        ArrayAdapter<CharSequence> containerSize=ArrayAdapter.createFromResource(this,R.array.container_size, android.R.layout.simple_spinner_dropdown_item);
        containerDimensions.setAdapter(containerSize);
        ArrayAdapter<CharSequence> pickupLocation=ArrayAdapter.createFromResource(this,R.array.pickup_location, android.R.layout.simple_spinner_dropdown_item);
        pick_location.setAdapter(pickupLocation);
        confirmConsignment=findViewById(R.id.getMeATruck);
        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        if(!hasPermissions(this,PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL);

        }


        consignmentViewmodel= ViewModelProviders.of(DestinationChooserActivity.this).get(ConsignmentViewmodel.class);
        consignment=new Consignment();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(DestinationChooserActivity.this,CargoOwnerActitivy.class));
//        finish();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==AUTOCOMPLETE_LOCATION_DESTINATION)
        {
            if(resultCode==RESULT_OK)
            {
                Place place=Autocomplete.getPlaceFromIntent(data);
                delivery_location.setText("DESTINATION \n"+place.getAddress());
                destination_location=place.getLatLng();
            }
            else if(resultCode== AutocompleteActivity.RESULT_ERROR)
            {
                Status status=Autocomplete.getStatusFromIntent(data);
                Toast.makeText(this, "Error "+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==AutocompleteActivity.RESULT_CANCELED)
            {
                Toast.makeText(this, "Destination Required", Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {


        delivery_location.setOnClickListener(v-> {

            List<Place.Field> fields= Arrays.asList(Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
            Intent  locationSearchAutoComplete=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fields)
                    .build(this);
            startActivityForResult(locationSearchAutoComplete,AUTOCOMPLETE_LOCATION_DESTINATION);
        });


        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mary_map));


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location ->  {
            if(location!=null)
            {
                LatLng currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(markerOptions.position(currentPosition).snippet("Click anywhere in the map as a Destination for your Consignment"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,14));
            }

        });



        mMap.setOnMapClickListener(lattlng-> {

                mMap.clear();
                markerOptions.position(lattlng);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lattlng,mMap.getCameraPosition().zoom+1f));

        });



        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),mMap.getCameraPosition().zoom+1.5f));
            }
        });



        confirmConsignment.setOnClickListener(v->finishConsignment(v));



    }

    public void SaveConsignment(Consignment consignmentFinal)
    {
        consignmentViewmodel.SaveConsignment(consignmentFinal).observe(DestinationChooserActivity.this, s-> {

            if(s==null)
            {
                Toast.makeText(DestinationChooserActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
            }
            else
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setTitle("Payment")
                        .setCancelable(false)
                        .setMessage("Proceeding to payment")
                        .setPositiveButton("Okay", (dialog,which)-> {

                            dialog.dismiss();
                            Intent intent=new Intent(DestinationChooserActivity.this,MakePaymentActivity.class);
                            startActivity(intent);
                            finish();

                        });

                builder.show();
            }

        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        String date= DateFormat.getDateInstance().format(calendar.getTime());
        consignment.setDate_of_pickup(date);


        AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Choose Container")
                .setCancelable(false)
                .setMessage("what knd of container looking for?")
                .setPositiveButton("Choose Container", (dialog,which)-> {

                        dialog.dismiss();



                });

        builder.show();
    }


}
