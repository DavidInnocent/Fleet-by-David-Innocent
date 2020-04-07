package ke.co.ximmoz.cargotruck.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import ke.co.ximmoz.cargotruck.models.LocationObject;


public class LocationRepository {

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    public static final String trackingDocument = "Tracking";


    public LiveData<String> SetLocationOfTruck(LocationObject locationObject) {
        MutableLiveData<String> truckLocation=new MutableLiveData<>();
        firestore.collection(trackingDocument).document(locationObject.getConsignmentID()).set(locationObject).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                truckLocation.setValue("Success");
            }
            else
            {
                truckLocation.setValue(task.getException().getMessage());
            }
        });
        return truckLocation;
    }



}
