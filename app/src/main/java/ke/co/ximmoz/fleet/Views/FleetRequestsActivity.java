package ke.co.ximmoz.fleet.Views;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
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
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import ke.co.ximmoz.fleet.Models.MarkerClusters;
import ke.co.ximmoz.fleet.R;

public class FleetRequestsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MarkerClusters> clustersClusterManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mLocation;
    LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_requests);
        int PERMISSION_ALL=1;
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this,PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this,PERMISSIONS,PERMISSION_ALL);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style));
        clustersClusterManager=new ClusterManager<>(this,mMap);


        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                mLocation=location;
                mMap.setOnCameraIdleListener(clustersClusterManager);
                mMap.setOnMarkerClickListener(clustersClusterManager);
                currentPosition=new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().title("YOu are here").position(currentPosition));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,14));
                addItems(currentPosition);

            }
        });
        // Add a marker in Sydney and move the camera


        clustersClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<MarkerClusters>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<MarkerClusters> cluster) {
                MarkerClusters marker=(MarkerClusters)cluster;
                Toast.makeText(FleetRequestsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        clustersClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerClusters>() {
            @Override
            public boolean onClusterItemClick(MarkerClusters markerClusters) {
                Toast.makeText(FleetRequestsActivity.this, markerClusters.getMarkerId(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }

    private void addItems(LatLng test) {
        double lat=test.latitude+0.2,lng=test.longitude+0.2;
        for(int i=0;i<5;i++)
        {
            double offset=i/600d;
            lat=lat+offset;
            lng=lng+offset;
            MarkerClusters offsetItem=new MarkerClusters(new LatLng(lat,lng),"haha","Mark"+i,"hodsfjklsdjfksjfklsjfksjkflsdjklfjsdklfjsdljfklst");
            clustersClusterManager.addItem(offsetItem);
        }

    }
}
