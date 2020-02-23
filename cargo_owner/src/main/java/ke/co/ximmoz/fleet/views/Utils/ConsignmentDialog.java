package ke.co.ximmoz.fleet.views.Utils;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ximmoz.fleet.models.Consignment;
import ke.co.ximmoz.fleet.R;
import ke.co.ximmoz.fleet.viewmodels.ConsignmentViewmodel;
import ke.co.ximmoz.fleet.views.LocationUpdateService;

import static ke.co.ximmoz.fleet.views.FleetRequestsActivity.hasPermissions;

public class ConsignmentDialog extends DialogFragment {

    private static final int PERMISSION_ALL =1 ;
    TextView destinationDistance;
    private Consignment consignment;
    private Button confirmPickup;
    private ConsignmentViewmodel consignmentViewmodel;
    View vieww;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DatabaseReference databaseReference;



    public ConsignmentDialog(Consignment consignment,FusedLocationProviderClient fusedLocationProviderClient) {
        this.consignment=consignment;
        this.fusedLocationProviderClient=fusedLocationProviderClient;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vieww=view;
        destinationDistance=view.findViewById(R.id.distanceToDestination);
        confirmPickup=view.findViewById(R.id.confirmPickup);
        destinationDistance.setText(consignment.getDistance());

        confirmPickup.setOnClickListener((v)->{
            confirmPickup(consignment);
        });

    }

    private void confirmPickup(Consignment consignment)
    {
        consignment.setStatus(ConsignmentStatus.IN_TRANSIT);
        consignment.setDriver(FirebaseAuth.getInstance().getCurrentUser().getUid());
        consignmentViewmodel.UpdateConsignment(consignment).observe(getViewLifecycleOwner(), consignmentt-> {

                if(consignmentt!=null)
                {
/*
* Start location tracking service update consignment driver
*
*
* */


                    String[] PERMISSIONS={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_BACKGROUND_LOCATION};

                    if(!hasPermissions(vieww.getContext(),PERMISSIONS))
                    {
                        ActivityCompat.requestPermissions(getActivity(),PERMISSIONS,PERMISSION_ALL);
                    }

                    SetupLocationServicesTracking();

                }
                else
                {
                    Toast.makeText(getActivity(), "shidaaa", Toast.LENGTH_SHORT).show();
                }

        });

    }

    private void SetupLocationServicesTracking() {
        Intent intent=new Intent(getActivity(), LocationUpdateService.class);
        intent.putExtra("Consignment",consignment);
        getActivity().startService(intent);
        Toast.makeText(getActivity(), "Started Succesfully", Toast.LENGTH_SHORT).show();
        this.setCancelable(true);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.consignment_dialog,container,false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        consignmentViewmodel= ViewModelProviders.of(this).get(ConsignmentViewmodel.class);
        databaseReference=FirebaseDatabase.getInstance().getReference();
    }
}
