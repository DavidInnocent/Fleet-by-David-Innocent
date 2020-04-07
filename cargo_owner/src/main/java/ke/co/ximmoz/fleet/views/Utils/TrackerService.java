package ke.co.ximmoz.fleet.views.Utils;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


import ke.co.ximmoz.fleet.models.LocationObject;
import ke.co.ximmoz.fleet.R;

public class TrackerService extends Service implements LifecycleOwner {
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
        super.onStartCommand(intent, flags, startId);
        Bundle bundle=intent.getExtras();
        consignmentID=intent.getStringExtra("Consignment");
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void buildNotification() {

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Firefy Channel";
            NotificationChannel channel = new NotificationChannel(channelId,"Firefy notifications",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Firefy notifications keeps  you in the loop.");
            mNotificationManager.createNotificationChannel(channel);
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
        requestLocationUpdates(consignmentID);
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

                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        LocationObject locationObject=new LocationObject();
                        locationObject.setLatitude(location.getLatitude());
                        locationObject.setLongitude(location.getLongitude());
                        locationObject.setTruckID(uid);
//                        locationViewmodel.SetLocationOfTruck(locationObject).observeForever(s -> {
//                            Toast.makeText(TrackerService.this, s, Toast.LENGTH_SHORT).show();
//                        });
                    }
                }
            }, null);
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.getLifecycle();
    }
}
