package ke.co.ximmoz.fleet.Views;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
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
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.Views.Utils.DatePickerFrag;

import static ke.co.ximmoz.fleet.Views.FleetRequestsActivity.hasPermissions;

public class DestinationChooserActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    private MarkerOptions markerOptions=new MarkerOptions();
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button confirmConsignment;
    int PERMISSION_ALL=1;


    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.cons)
    ConstraintLayout cons;
    @BindView(R.id.goneConstraint)
    ConstraintLayout goneConstraint;


    @BindView(R.id.containerDimensions)
    Spinner containerDimensions;

    private void   finishConsignment(View v){
        String buttonTexts=((Button)v).getText().toString();
        String PICKUP_LOCATION=getResources().getString(R.string.pickup_location);
        String DROP_OFF_LOCATION=getResources().getString(R.string.drop_off_location);
        String CONTAINER=getResources().getString(R.string.choose_container);

            if(buttonTexts==(PICKUP_LOCATION))
            {
                GetPickupLocation();
                return;
            }
            else if(buttonTexts==(DROP_OFF_LOCATION))
            {
                GetDestinationLocation();
                return;
            }
            else if(buttonTexts==CONTAINER)
            {
                GetContainer();
                return;
            }
            else
            {
                GetPickupDate();
                return;
            }




    }

    private void GetContainer() {
        consignment.setContainer_size(containerDimensions.getSelectedItem().toString());
        SaveConsignment(consignment);
    }

    private void GetPickupDate() {

        DialogFragment dialogFragment=new DatePickerFrag();
        dialogFragment.show(getSupportFragmentManager(),"date picker");
        confirmConsignment.setBackgroundResource(R.drawable.rounded_blue);
        cons.setBackgroundResource(R.drawable.rounded_view_pink);
        confirmConsignment.setText(getResources().getString(R.string.choose_container));
        description.setText(getResources().getString(R.string.choose_container));
        confirmConsignment.setBackgroundResource(R.drawable.rounded_button);
        cons.setBackgroundResource(R.drawable.rounded_view);

    }

    private void GetDestinationLocation() {
        consignment.setDestination_lng(markerOptions.getPosition().latitude);
        consignment.setDestination_lat(markerOptions.getPosition().longitude);



        markerOptions.title("Delivery Address");
        markerOptions.snippet("Make sure to Zoom in for Accuracy");

        AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Destination Location Saved")
                .setCancelable(false)
                .setMessage(markerOptions.getPosition().toString())
                .setPositiveButton("Pick A Date", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        GetPickupDate();

                    }
                });

        builder.show();


        confirmConsignment.setText(getResources().getString(R.string.choose_container));
        description.setText(getResources().getString(R.string.choose_container));
        confirmConsignment.setBackgroundResource(R.drawable.rounded_button);
        cons.setBackgroundResource(R.drawable.rounded_view);
    }

    private void GetPickupLocation() {
        consignment.setPickup_location_lat(markerOptions.getPosition().latitude);
        consignment.setPickup_location_lng(markerOptions.getPosition().longitude);
        consignment.setStatus("active");
        AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Pickup Location Saved")
                .setCancelable(false)
                .setMessage("Now Choose cargo destinatin Drop off Location")
                .setPositiveButton("Choose Destination", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        builder.show();
        confirmConsignment.setText(getResources().getString(R.string.drop_off_location));
        confirmConsignment.setBackgroundResource(R.drawable.rounded_blue);
        description.setText(getResources().getString(R.string.drop_off_location));
        cons.setBackgroundResource(R.drawable.rounded_view_pink);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
        markerOptions.title("Pickup Address");
        markerOptions.snippet("Make sure to Zoom in for Accuracy");
        mMap.addMarker(markerOptions);
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(),14));


    }



    public void SaveConsignment(Consignment consignmentFinal)
    {
        consignmentViewmodel.SaveConsignment(consignmentFinal).observe(DestinationChooserActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s==null)
                {
                    Toast.makeText(DestinationChooserActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                            .setIcon(R.mipmap.ic_launcher_round)
                            .setTitle("Truck Request Saved")
                            .setCancelable(false)
                            .setMessage("Your truck request has been succesfully filed. Kindly be patient for your truck.")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent intent=new Intent(DestinationChooserActivity.this,CargoOwnerActitivy.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                    builder.show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);
        ButterKnife.bind(this);
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        confirmConsignment=findViewById(R.id.getMeATruck);
        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

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
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.mary_map));

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(markerOptions.position(currentPosition).snippet("Click anywhere in the map as a Destination for your Consignment"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,14));


            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                mMap.clear();
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,mMap.getCameraPosition().zoom+1f));
            }
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

        confirmConsignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishConsignment(v);
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
                .setPositiveButton("Choose Container", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        goneConstraint.setVisibility(View.VISIBLE);

                    }
                });

        builder.show();
    }
}
