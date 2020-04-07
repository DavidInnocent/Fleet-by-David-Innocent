package ke.co.ximmoz.cargotruck.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.cargotruck.MainActivity;
import ke.co.ximmoz.cargotruck.R;
import ke.co.ximmoz.cargotruck.models.LocationObject;
import ke.co.ximmoz.cargotruck.viewmodels.LocationViewModel;
import ke.co.ximmoz.cargotruck.views.ConsignmentsFragmentDirections;


public class ConsignmentAdapter extends RecyclerView.Adapter<ConsignmentAdapter.ConsignmentViewHolder> {


    private static final int PERMISSSION_REQUEST_CODE = 12;
    private Context context;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private List<Consignment> consignments;
    private LocationViewModel locationViewModel;
    private NavController navController;


    public class ConsignmentViewHolder extends RecyclerView.ViewHolder {
        TextView distanceToDestination, Destination, pickup;
        ConstraintLayout consignment_detail;
        Button confirm;


        public ConsignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            distanceToDestination = itemView.findViewById(R.id.distanceToDestination);
            Destination = itemView.findViewById(R.id.Destination);
            pickup = itemView.findViewById(R.id.pickup);
            consignment_detail = itemView.findViewById(R.id.consignment_detail);
            confirm = itemView.findViewById(R.id.confirm);
        }
    }

    public ConsignmentAdapter(Context context, List<Consignment> consignments, NavController frag) {
        this.context = context;
        this.consignments = consignments;
        this.navController = frag;


    }


    @NonNull
    @Override
    public ConsignmentAdapter.ConsignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consignment_list_view, parent, false);
        return new ConsignmentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ConsignmentViewHolder holder, int position) {

        Consignment consignment = consignments.get(position);
        holder.distanceToDestination.setText(consignment.getDistance());
        holder.Destination.setText(consignment.getDestinationName());
        holder.pickup.setText(consignment.getPickupName());
        holder.confirm.setOnClickListener(v ->
                {

                    AlertDialog.Builder builder=new AlertDialog.Builder(context)
                            .setIcon(R.drawable.logo_fleet)
                            .setTitle("Confirm Delivery Request")
                            .setMessage("Acknowledge that you are on your way to pickup the consignment?")
                            .setPositiveButton("Confirm", (dialog, which) -> {
                                InitializeDependencies();
                                MakeLocationRequest(consignment);
                                navController.navigate(ConsignmentsFragmentDirections.actionConsignmentsFragmentToTruckMapsFragment(consignment));
                                dialog.dismiss();
                            }).setNeutralButton("Reject", (dialog, which) -> dialog.dismiss());

                    builder.show();


                }

        );
    }

    private void MakeLocationRequest(Consignment consignment) {
        buildNotification();
        locationRequest.setInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            AlertDialog.Builder builder=new AlertDialog.Builder(context)
                    .setIcon(R.drawable.logo_fleet)
                    .setTitle("Location Access")
                    .setMessage("Fleet needs location access for consignment tracking purpose")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestPermissions();
                            dialog.dismiss();
                        }
                    }).setNeutralButton("Reject", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            builder.show();


            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    SendLocationToFirebase(location,consignment);
                }
            }
        }, null);
    }

    private void RequestPermissions() {
        String[] locationPermissions={Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions((MainActivity)context,locationPermissions,PERMISSSION_REQUEST_CODE);
    }

    private void SendLocationToFirebase(Location location,Consignment consignment) {
        LocationObject locationObject=new LocationObject();
        locationObject.setLongitude(location.getLongitude());
        locationObject.setLatitude(location.getLatitude());
        locationObject.setTruckID("DaVid Truck");
        locationObject.setConsignmentID(consignment.getId());
        locationViewModel.SetLocationOfTruck(locationObject);
    }

    private void InitializeDependencies() {
        this.locationViewModel= ViewModelProviders.of((FragmentActivity)context).get(LocationViewModel.class);
        this.fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        this.locationRequest=new LocationRequest();
    }


    @Override
    public int getItemCount() {
        return consignments.size();
    }

    public void clear(){
        consignments.clear();
    }

    /**Function to return the Data List**/
    public List<Consignment> getOnlineList(){
        return this.consignments;
    }

    /**Function to update the specific Item**/
    public void updateTextView(int position, Consignment newItem){
        consignments.set(position, newItem); //set the item
        notifyItemChanged(position, newItem); //notify the adapter for changes
    }
    public void RemoveTextView(int position){
        consignments.remove(position); //set the item
        notifyItemRemoved(position);
    }

    public void AddedTextView(Consignment conAdded) {
        consignments.add(consignments.size()+1,conAdded);
        notifyDataSetChanged();
    }


    /*Get the position of item inside data list base on the given ID*/
    public int getPositionBaseOnItemID(String theID) {
        int length = consignments.size();
        for (int i = 0; i < length; i ++) {
            if(consignments.get(i).getId().equals(theID)) {
                return i;
            }
        }
        return -1; //Item not found
    }

    private void buildNotification() {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, "Firefy Channel");
        Intent ii = new Intent(context, TrackerService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ii, 0);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.logo_fleet)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.logo_fleet))
                .setAutoCancel(false)
                .setContentText("Fleet is currently monitoring your location periodically.")
                .setContentTitle("TRACKING ENABLED")
                .setColorized(true)
                .setSound(defaultSoundUri)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setOnlyAlertOnce(false)
                .setContentIntent(pendingIntent);

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

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

}