package ke.co.ximmoz.cargotruck.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.cargotruck.R;
import ke.co.ximmoz.cargotruck.utils.CustomMarker;
import ke.co.ximmoz.cargotruck.utils.GeofenceTransitionService;

public class TruckMapsFragment extends Fragment {
    private static final int PERMISSSION_REQUEST_CODE = 34;
    private static final String CONG = "GET_ME_HERE";
    private Consignment consignment;
    private GeofencingClient geofencingClient;
    private NavController navController;
    private static MarkerOptions truckMarker;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PendingIntent geofencePendingIntent;
    private GeofencingRequest geofencingRequest;
    private ArrayList<Geofence> geofences = new ArrayList<>();
    private String api;
    private GoogleMap map;
    private ArrayList<Polyline> polylines = new ArrayList<>();
    private boolean ViewPoint = false;
    private ClusterManager<CustomMarker> clusterManager;
    private Unbinder unbinder;


    @OnClick(R.id.bill_of_ladding) void GetBillOfLadding()
    {
       navController.navigate(TruckMapsFragmentDirections.actionTruckMapsFragmentToBillofLaddingFragment(consignment));
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {


            map = googleMap;
            clusterManager=new ClusterManager<>(getActivity(),map);
            map.setOnMarkerClickListener(clusterManager);
            map.setOnCameraIdleListener(clusterManager);


            LocationRequest locationRequest = new LocationRequest()
                    .setInterval(10000)
                    .setFastestInterval(3000)
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                RequestPermissions();
                return;
            }
            
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        UpdateMap(location);
                    }
                }
            }, null);




            LatLng destinationCoordinates = new LatLng(consignment.getDestinationLat(), consignment.getDestinationLng());
            LatLng startCoordinates = new LatLng(consignment.getPickupLat(), consignment.getPickupLng());
            map.addMarker(new MarkerOptions().position(destinationCoordinates).title("Destination"));
            map.addMarker(new MarkerOptions().position(startCoordinates).title(" Pickup Point"));

            CreateGeofence();
            geofencingRequest=getGeofencingRequest();

            geofencePendingIntent=getGeofencePendingIntent();

            geofencingClient.addGeofences(geofencingRequest,geofencePendingIntent).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    CircleOptions circleOptions=new CircleOptions()
                            .center(startCoordinates)
                            .strokeColor(R.color.colorAccent)
                            .fillColor(Color.argb(50,70,70,70))
                            .radius(1000.0f);
                    map.addCircle(circleOptions);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error adding geofences", Toast.LENGTH_SHORT).show();
                }
            });




            RouteCreator(destinationCoordinates, startCoordinates);
        }
    };

    private Geofence CreateGeofence() {
        return new Geofence.Builder()
                .setRequestId(consignment.getId())
                .setCircularRegion(
                        consignment.getPickupLat(),
                        consignment.getPickupLng(),
                        1000.0f)
                .setLoiteringDelay(1000)
                .setNotificationResponsiveness(1000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER|Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .build();


    }

   private PendingIntent getGeofencePendingIntent()
   {
       if(geofencePendingIntent!=null)
       {
           return geofencePendingIntent;
       }
       Intent intent=new Intent(getActivity(), GeofenceTransitionService.class);
       intent.setAction(CONG);
       return PendingIntent.getService(getActivity(),90,intent,PendingIntent.FLAG_UPDATE_CURRENT);

   }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(CreateGeofence());
        return builder.build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        truckMarker=new MarkerOptions();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        navController= Navigation.findNavController(container);
        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        api = getResources().getString(R.string.google_api_key);


        View view= inflater.inflate(R.layout.fragment_truck_maps, container, false);
        unbinder= ButterKnife.bind(this,view);
        return view;
    }

    private void UpdateMap(Location location) {

        clusterManager.clearItems();

        LatLng truckCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        CustomMarker truckLocationMarker=new CustomMarker(truckCoordinates,consignment.getId(),"Your Current Location","");
        clusterManager.addItem(truckLocationMarker);
        map.setBuildingsEnabled(true);
        if (!ViewPoint) {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(truckCoordinates, 12));
            ViewPoint = true;
        } else {
            map.animateCamera(CameraUpdateFactory.newLatLng(truckCoordinates));
        }


    }


    RoutingListener polylineRouterListener = new RoutingListener() {
        @Override
        public void onRoutingFailure(RouteException e) {

        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

            if (polylines.size() > 0) {
                for (Polyline polyline : polylines) {
                    polyline.remove();
                }
            }

            for (int i = 0; i < route.size(); i++) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(getResources().getColor(R.color.colorPrimary));
                polylineOptions.width(8 + i * 3);
                polylineOptions.addAll(route.get(i).getPoints());
                Polyline polyline = map.addPolyline(polylineOptions);
                polylines.add(polyline);

            }

        }


        @Override
        public void onRoutingCancelled() {

        }
    };

    private void RouteCreator(LatLng destinationCoordinates, LatLng startCoordinates) {

        Routing routing = new Routing.Builder()
                .key(api)
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .waypoints(startCoordinates, destinationCoordinates)
                .alternativeRoutes(false)
                .withListener(polylineRouterListener)
                .build();
        routing.execute();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        consignment = TruckMapsFragmentArgs.fromBundle(getArguments()).getConsignment();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    private void RequestPermissions() {
        String[] locationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};
        ActivityCompat.requestPermissions(getActivity(), locationPermissions, PERMISSSION_REQUEST_CODE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}