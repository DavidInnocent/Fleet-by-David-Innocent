package ke.co.ximmoz.fleet.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.ximmoz.fleet.models.LocationObject;

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
