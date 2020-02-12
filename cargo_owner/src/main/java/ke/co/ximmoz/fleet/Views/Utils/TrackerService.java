package ke.co.ximmoz.fleet.Views.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ximmoz.fleet.Models.LocationObject;
import ke.co.ximmoz.fleet.R;

public class TrackerService extends Service {
    private static final String CHANNEL_ID = "Tracking LocationObject";
    private String consignmentID;

    public TrackerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        loginToFirebase();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.consignmentID=intent.getStringExtra("ConsignmentID");
        super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void buildNotification() {
//        String stop = "stop";
//        registerReceiver(stopReceiver, new IntentFilter(stop));
//        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
//                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
//        // Create the persistent notification
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText("Tracking Enabled")
//                .setOngoing(true)
//                .setContentIntent(broadcastIntent)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSmallIcon(R.drawable.ic_map_marker);
//        startForeground(1, builder.build());


        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "Firefy Channel");
        Intent ii = new Intent(getApplicationContext(), TrackerService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(false)
                .setContentText("Fleet is currently monitoring your location periodically.")
                .setContentTitle("TRACKING ENABLED")
                .setColorized(true)
                .setSound(defaultSoundUri)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(false)
                .setContentIntent(pendingIntent);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Firefy Channel";
            NotificationChannel channel = new NotificationChannel(channelId,"Firefy notifications",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Firefy notifications keeps  you in the loop.");
            mNotificationManager.createNotificationChannel(channel);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void loginToFirebase() {
        requestLocationUpdates(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    private void requestLocationUpdates(String uid) {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tracking");
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        LocationObject locationObject=new LocationObject();
                        locationObject.setLatitude(location.getLatitude());
                        locationObject.setLongitude(location.getLongitude());
                        locationObject.setTruckID(uid);


                        ref.child(consignmentID).setValue(locationObject);
                    }
                }
            }, null);
        }
    }
}
