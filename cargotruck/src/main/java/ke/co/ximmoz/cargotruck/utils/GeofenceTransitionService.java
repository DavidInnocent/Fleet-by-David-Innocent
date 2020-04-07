package ke.co.ximmoz.cargotruck.utils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import ke.co.ximmoz.cargotruck.R;

public class GeofenceTransitionService extends IntentService {
    private static final String TAG = GeofenceTransitionService.class.getSimpleName();
    public static final int GEOFENCE_NOTIFICATION_ID=8;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public GeofenceTransitionService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        GeofencingEvent geofencingEvent=GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError())
        {
            Log.e(TAG,getErrorString(geofencingEvent.getErrorCode()));
            return;
        }
        int geofenceTransition=geofencingEvent.getGeofenceTransition();
        if(geofenceTransition== Geofence.GEOFENCE_TRANSITION_ENTER||geofenceTransition==Geofence.GEOFENCE_TRANSITION_DWELL||geofenceTransition==Geofence.GEOFENCE_TRANSITION_EXIT)
        {
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Send notification and log the transition details.
            sendNotification();
        }
    }

    private void sendNotification() {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "Firefy Cgsdfgsdgfdsfghannel");
//        Intent ii = new Intent(getApplicationContext(), GeofenceTransitionService.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, ii, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.logo_fleet)
                .setAutoCancel(false)
                .setContentText("jkfdslkfjdklfjksdfjdaslkjfdskljfasly.")
                .setContentTitle("TRAjdslfjlksfkldsj lfkjsal")
                .setColorized(true)
                .setSound(defaultSoundUri)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(false)
                ;

        mNotificationManager =
                (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

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

    private static String getErrorString(int errorcode){
        switch(errorcode)
        {
            case GeofenceStatusCodes
                    .GEOFENCE_NOT_AVAILABLE:
                return "Geofence not available";
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return "Too many geofences";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Too many pending intents";
            default:
                return "unknown error";
        }
    }
}
