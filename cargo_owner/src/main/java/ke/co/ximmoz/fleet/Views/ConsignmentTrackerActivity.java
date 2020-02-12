package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Models.LocationObject;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.LocationViewModel;

public class ConsignmentTrackerActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private LocationViewModel locationViewModel;
    private Consignment consignment;
    private LocationObject truckLocation;
    private ArrayList<Polyline> polylines=new ArrayList<>();
    private ArrayList<LatLng> waypoints=new ArrayList<>();

    private static int mapZoomCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignment_tracker);
        mapZoomCounter=0;
        consignment=(Consignment)getIntent().getSerializableExtra("Consignment");
        this.locationViewModel= ViewModelProviders.of(ConsignmentTrackerActivity.this).get(LocationViewModel.class);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        mapZoomCounter=0;
        super.onResume();
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

        String api=getResources().getString(R.string.google_api_key);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera


        locationViewModel.GetTruckLocation(consignment.getId()).observe(ConsignmentTrackerActivity.this, location -> {
            truckLocation=location;

            waypoints.add(new LatLng(location.getLatitude(),location.getLongitude()));

            if(waypoints.size()<2)
            {
                MarkerOptions markerOptions=new MarkerOptions()
                        .position(new LatLng(location.getLatitude(),location.getLongitude()))
                        .title("Driver is Here");
                mMap.addMarker(markerOptions);
                if(mapZoomCounter==0)
                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(markerOptions.getPosition(),17));
                    mapZoomCounter++;
                }
                else
                {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(markerOptions.getPosition()));
                }
                return;
            }
            else
            {


            }

        });






    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if(polylines.size()>0)
        {
            for(Polyline polyline:polylines)
            {
                polyline.remove();
            }
        }


        for(int i=0;i<route.size();i++)
        {
            PolylineOptions polylineOptions=new PolylineOptions();
            polylineOptions.color(getResources().getColor(R.color.colorAccent));
            polylineOptions.width(5+i*3);
            polylineOptions.addAll(route.get(i).getPoints());
            Polyline polyline=mMap.addPolyline(polylineOptions);
            polylines.add(polyline);

        }

    }

    @Override
    public void onRoutingCancelled() {

    }
}
