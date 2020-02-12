package ke.co.ximmoz.fleet.Views;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ximmoz.fleet.Models.Consignment;

public class LocationUpdateService extends Service {

    FusedLocationProviderClient client;
    LocationRequest locationRequest;
    Consignment consignment;
    private DatabaseReference databaseReference;

    public LocationUpdateService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        consignment=(Consignment)intent.getSerializableExtra("Consignment");
        StartLocation();
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference= FirebaseDatabase.getInstance().getReference();


    }
    private void StartLocation()
    {
        locationRequest=new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        client= LocationServices.getFusedLocationProviderClient(this);
        client.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location=locationResult.getLastLocation();
                if(location!=null)
                {
                    SendLocationToFirebase(location);
                }
            }
        },null);
    }

    private void SendLocationToFirebase(Location location) {
        databaseReference.child("OngoingPickups").child(consignment.getId()).setValue(location);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
