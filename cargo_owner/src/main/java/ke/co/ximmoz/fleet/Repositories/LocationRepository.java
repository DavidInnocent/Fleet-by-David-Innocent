package ke.co.ximmoz.fleet.Repositories;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Models.LocationObject;
import ke.co.ximmoz.fleet.Views.ConsignmentTrackerActivity;

public class LocationRepository {


    public LiveData<LocationObject> GetLocationOfTruck(String consignmentID) {
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Tracking");
        MutableLiveData<LocationObject> truckLocation=new MutableLiveData<>();
        databaseReference.child(consignmentID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {

                    LocationObject locationObject=dataSnapshot.getValue(LocationObject.class);
                    truckLocation.setValue(locationObject);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return truckLocation;
    }



}
