package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

import static ke.co.ximmoz.fleet.Views.FleetRequestsActivity.hasPermissions;

public class DestinationChooserActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;
    private FusedLocationProviderClient fusedLocationProviderClient;
    int PERMISSION_ALL=1;
    private Location mLocation;

    @OnClick(R.id.getMeATruck) void   finishConsignment(){
        consignment.setDestination_lat(markerOptions.getPosition().latitude);
        consignment.setDestination_lng(markerOptions.getPosition().longitude);
        consignment.setStatus("active");
        consignmentViewmodel= ViewModelProviders.of(DestinationChooserActivity.this).get(ConsignmentViewmodel.class);
        consignmentViewmodel.SaveConsignment(consignment).observe(DestinationChooserActivity.this, new Observer<String>() {
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
                            .setTitle("Consignment Saved")
                            .setCancelable(false)
                            .setMessage("Would you like to pay 40% of the amount now?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                    Intent intent=new Intent(DestinationChooserActivity.this,MakePaymentActivity.class);
                                    intent.putExtra("Consignment",consignment);
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

        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        if(!hasPermissions(this,PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL);
        }
        Intent intent=getIntent();
        consignment=(Consignment)intent.getSerializableExtra("Consignment") ;
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
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        markerOptions=new MarkerOptions();
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocation=location;
                LatLng currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().title("You are here").position(currentPosition).snippet("Click anywhere in the map as a Destination for your Consignment"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));


            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Confirm Delivery Address");
                markerOptions.snippet("Is this the correct location?");
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,mMap.getCameraPosition().zoom+1.5f));
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

    }
}
