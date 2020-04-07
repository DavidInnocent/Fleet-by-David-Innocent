package ke.co.ximmoz.cargotruck.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import ke.co.ximmoz.cargotruck.MainActivity;
import ke.co.ximmoz.cargotruck.R;
import ke.co.ximmoz.cargotruck.models.LocationObject;
import ke.co.ximmoz.cargotruck.viewmodels.LocationViewModel;

public class ConsignmentTrackerService extends Service {

    private String consignmentID;
    public ConsignmentTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle=intent.getExtras();
        consignmentID=intent.getStringExtra("Consignment");
        buildNotification();
        SendToFirebase();
        return START_STICKY;
    }

    private void SendToFirebase() {

        LocationObject locationObject=new LocationObject();
        locationObject.setTruckID("Innocent truck");
        locationObject.setLatitude(1.00);
        locationObject.setLongitude(2.00);
        locationObject.setConsignmentID(consignmentID);
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Tracking").document(consignmentID).set(locationObject).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ConsignmentTrackerService.this, "sent data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void buildNotification() {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "Firefy Channel");
        Intent ii = new Intent(this, TrackerService.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_fleet);

        mBuilder.setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(false)
                .setSmallIcon(getNotificationIcon())
                .setContentText("Fleet is currently monitoring your location periodically.")
                .setContentTitle("TRACKING ENABLED")
                .setLargeIcon(largeIcon)
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

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.logo_fleet : R.drawable.equity;
    }
}
