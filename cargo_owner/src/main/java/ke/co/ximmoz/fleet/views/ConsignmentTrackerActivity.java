package ke.co.ximmoz.fleet.views;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.fleet.models.LocationObject;
import ke.co.ximmoz.fleet.R;

public class ConsignmentTrackerActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
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
