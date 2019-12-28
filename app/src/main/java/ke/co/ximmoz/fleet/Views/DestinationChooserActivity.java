package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

public class DestinationChooserActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions markerOptions;
    @OnClick(R.id.getMeATruck) void   finishConsignment(){
        consignment.setDestination_lat(markerOptions.getPosition().latitude);
        consignment.setDestination_lng(markerOptions.getPosition().longitude);
        consignment.setStatus("notpaid");

        consignmentViewmodel= ViewModelProviders.of(DestinationChooserActivity.this).get(ConsignmentViewmodel.class);

        Toast.makeText(this, consignment.getAmount(), Toast.LENGTH_SHORT).show();
        consignmentViewmodel.SaveConsignment(consignment).observe(DestinationChooserActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s==null)
                {
                    Toast.makeText(DestinationChooserActivity.this, "Something Went Wrong Try Again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(DestinationChooserActivity.this, s, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder=new AlertDialog.Builder(DestinationChooserActivity.this)
                            .setIcon(R.mipmap.ic_launcher_round)
                            .setTitle("Consignment Saved")
                            .setMessage("Would you like to pay 40% of the amount now?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent=new Intent(DestinationChooserActivity.this,MakePaymentActivity.class);
                                    intent.putExtra("Consignment",consignment);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(DestinationChooserActivity.this,CargoOwnerActitivy.class));
                                    finish();
                                }
                            });

                    builder.show();
                }
            }
        });

    }
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_chooser);
        ButterKnife.bind(this);
        Intent intent=getIntent();
        consignment=(Consignment)intent.getSerializableExtra("Consignment") ;
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(DestinationChooserActivity.this,CargoOwnerActitivy.class));
        finish();
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
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        markerOptions=new MarkerOptions();
        LatLng sydney = new LatLng(-34, 151);
        markerOptions.position(sydney);
        markerOptions.draggable(true);
        markerOptions.title("Here");
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,5));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
            }
        });
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),12));
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),12));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),12));
            }
        });

    }
}
