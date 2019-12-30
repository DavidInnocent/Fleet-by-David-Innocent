package ke.co.ximmoz.fleet.Views;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Views.Utils.ConsignmentDialog;
import ke.co.ximmoz.fleet.Views.Utils.MarkerClusters;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.Viewmodels.ConsignmentViewmodel;

public class FleetRequestsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<MarkerClusters> clustersClusterManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mLocation;
    LatLng currentPosition;
    private Consignment consignment;
    private ConsignmentViewmodel consignmentViewmodel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_requests);


        consignmentViewmodel= ViewModelProviders.of(FleetRequestsActivity.this).get(ConsignmentViewmodel.class);
        int PERMISSION_ALL=1;
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

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
                currentPosition=new LatLng(location.getLatitude(),location.getLongitude());

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
        // Add a marker in Sydney and move the camera


        clustersClusterManager.setOnClusterInfoWindowClickListener(new ClusterManager.OnClusterInfoWindowClickListener<MarkerClusters>() {
            @Override
            public void onClusterInfoWindowClick(Cluster<MarkerClusters> cluster) {

                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                Fragment fragment=getSupportFragmentManager().findFragmentByTag("dialog");
                for(MarkerClusters markerCluster:cluster.getItems())
                {

                    if(fragment!=null)
                    {
                        fragmentTransaction.remove(fragment);
                    }
                    fragmentTransaction.addToBackStack(null);
                    DialogFragment dialogFragment=new ConsignmentDialog();
                    dialogFragment.show(fragmentTransaction,"dialog");
                    Toast.makeText(FleetRequestsActivity.this, markerCluster.getConsignment().getContainer_size(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        clustersClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerClusters>() {
            @Override
            public boolean onClusterItemClick(MarkerClusters markerClusters) {

                return false;
            }
        });


        consignmentViewmodel.GetConsignments().observe(this, new Observer<List<Consignment>>() {
            @Override
            public void onChanged(List<Consignment> consignments) {
                addItems(consignments);
            }
        });

    }

    private void addItems(List<Consignment> consignmentReturned) {


        for(Consignment con:consignmentReturned) {
            LatLng points = new LatLng(con.getDestination_lng(),con.getDestination_lat() );
            MarkerClusters offsetItem = new MarkerClusters(points, "None", "Pickup time", "Delivery date:" + con.getDate_of_pickup(), con);
            clustersClusterManager.addItem(offsetItem);

        }
        clustersClusterManager.cluster();


    }
}
