package ke.co.ximmoz.fleet.Views;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Views.Utils.ConsignmentDialog;
import ke.co.ximmoz.fleet.Views.Utils.MarkerClusters;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

public class FleetRequestsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private GoogleMap mMap;
    private ClusterManager<MarkerClusters> clustersClusterManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mLocation;
    LatLng currentPosition;
    LatLng dest;
    LatLng pickup;
    private static MarkerOptions opt=new MarkerOptions();
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;
    private ArrayList<Polyline> polylines=new ArrayList<>();
    private static String markerListener="";
    private LocationRequest locationRequest;

    MarkerOptions pickupMarker;
    static Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_requests);
        locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        consignmentViewmodel= ViewModelProviders.of(FleetRequestsActivity.this).get(ConsignmentViewmodel.class);
        int PERMISSION_ALL=1;
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

        if(!hasPermissions(this,PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    protected BroadcastReceiver stopReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(stopReceiver);

        }
    };

    public static boolean hasPermissions(Context context,String... permissions)
    {
        if(context!=null &&permissions!=null)
        {
            for(String permission:permissions)
            {
                if(ActivityCompat.checkSelfPermission(context,permission)!=PackageManager.PERMISSION_GRANTED)
                {
                    return false;
                }
            }
        }
        return true;
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
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.mary_map));
        clustersClusterManager=new ClusterManager<>(this,mMap);

//        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                Location location=locationResult.getLastLocation();
//                if(location!=null)
//                {
//                    mLocation=location;
//                    currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
//                    mMap.clear();
//                    mMap.setOnMarkerClickListener(clustersClusterManager);
//                    mMap.setOnCameraIdleListener(clustersClusterManager);
//                    MarkerOptions opt=new MarkerOptions()
//                            .title("You are here")
//                            .position(currentPosition)
//                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
//                    mMap.addMarker(opt);
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,14));
//                }
//            }
//        },null);

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location-> {
            if(location!=null)
            {
                mLocation=location;
                currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.clear();
                mMap.setOnMarkerClickListener(clustersClusterManager);
                mMap.setOnCameraIdleListener(clustersClusterManager);
                MarkerOptions opt=new MarkerOptions()
                        .title("You are here")
                        .position(currentPosition)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
                mMap.addMarker(opt);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,14));

            }


        });



        clustersClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<MarkerClusters>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<MarkerClusters> cluster) {

                    Toast.makeText(FleetRequestsActivity.this, "clicker", Toast.LENGTH_SHORT).show();

            }
        });

        pickupMarker=new MarkerOptions();

        clustersClusterManager.setOnClusterItemClickListener(markerClusters-> {




            double destinationLat=markerClusters.getConsignment().getDestination_lat();
            double destinationLng=markerClusters.getConsignment().getDestination_lng();

            double pickupLat=markerClusters.getConsignment().getPickup_lat();
            double pickupLng=markerClusters.getConsignment().getPickup_lng();



            consignment=markerClusters.getConsignment();
            pickup=new LatLng(pickupLat,pickupLng);
            dest=new LatLng(destinationLat,destinationLng);
            pickupMarker.draggable(false);
            pickupMarker.title("Pickup Location")
                    .position(pickup)
                    .snippet("This is the pickup location")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickup,14));
            if(marker!=null)
            {
                marker.remove();
            }
            marker=mMap.addMarker(pickupMarker);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickup,14));


            String api=getResources().getString(R.string.google_api_key);
            if(markerListener!=""&&markerListener==markerClusters.getMarkerId())
            {

                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                Fragment fragment=getSupportFragmentManager().findFragmentByTag("dialog");
                if(fragment!=null)
                {
                    fragmentTransaction.remove(fragment);
                }
                fragmentTransaction.addToBackStack(null);
                DialogFragment dialogFragment=new ConsignmentDialog(markerClusters.getConsignment(),fusedLocationProviderClient);
                dialogFragment.show(fragmentTransaction,"dialog");
            }
            markerListener=markerClusters.getMarkerId();
            Routing routing=new Routing.Builder()
                    .key(api)
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .waypoints(pickup,dest)
                    .withListener(this)
                    .alternativeRoutes(false)
                    .build();
            routing.execute();
             return false;

        });

        consignmentViewmodel.GetConsignments().observe(this, consignments-> {
            mMap.clear();
            addItems(consignments);

        });


    }




    private void addItems(List<Consignment> consignmentReturned) {


        clustersClusterManager.clearItems();
        for(Consignment con:consignmentReturned)
        {
            LatLng points=new LatLng(con.getDestination_lat(),con.getDestination_lng());
            MarkerClusters offsetItem = new MarkerClusters(points, con.getId(), "NOTHING", "Pickup Location date:" + "NOTHING", con);
            clustersClusterManager.addItem(offsetItem);

        }
        clustersClusterManager.cluster();

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        return;
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


        String distance = null;
        String destinationText = null;
        String pickupText = null;

        for(int i=0;i<route.size();i++)
        {
            PolylineOptions polylineOptions=new PolylineOptions();
            polylineOptions.color(getResources().getColor(R.color.colorAccent));
            polylineOptions.width(5+i*3);
            polylineOptions.addAll(route.get(i).getPoints());
            Polyline polyline=mMap.addPolyline(polylineOptions);
            polylines.add(polyline);
            distance=route.get(i).getDistanceText();
            destinationText=route.get(i).getEndAddressText();

        }
        consignment.setDistance(distance);
        consignment.setDestinationText(destinationText);
        pickupMarker.title(distance);


    }


    @Override
    public void onRoutingCancelled() {

    }
}
