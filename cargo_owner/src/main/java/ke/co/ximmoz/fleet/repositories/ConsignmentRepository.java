package ke.co.ximmoz.fleet.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ke.co.ximmoz.fleet.models.Consignment;

public class ConsignmentRepository {


    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore= FirebaseFirestore.getInstance();
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
    public static final String consignmentDocument="Consignments";
    DocumentReference documentReference=firestore.collection(consignmentDocument).document();

    public LiveData<String> SaveConsignment(Consignment consignment) {
        final MutableLiveData<String> successMessage=new MutableLiveData<>();

        consignment.setId(documentReference.getId());
        documentReference.set(consignment).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                successMessage.setValue("1");
            }
            else
            {
                successMessage.setValue("0");

            }
        });
        return successMessage;
    }




    public MutableLiveData<List<Consignment>> GetOwnerConsignments() {
        MutableLiveData<List<Consignment>> OwnerconsignmentLiveData=new MutableLiveData<>();
        List<Consignment> consignmentss=new ArrayList<>();

        firestore.collection(consignmentDocument)
//                .whereEqualTo("owner",firebaseUser.getUid())
                .get().addOnCompleteListener(task -> {

                    if(task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            consignmentss.add(document.toObject(Consignment.class));

                        }
                    }

                });

        return OwnerconsignmentLiveData;
    }





    public MutableLiveData<List<Consignment>> GetConsignments()
    {
        MutableLiveData<List<Consignment>> consignmentLiveData=new MutableLiveData<>();
        List<Consignment> consignments=new ArrayList<>();
        firestore.collection("Consignments").get();
        Query query=databaseReference.child("PendingConsignments").orderByChild("status").equalTo("AwaitingPickup");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {

                        Consignment con=dataSnapshot.getValue(Consignment.class);
                        con.setId(dataSnapshot.getKey());
                        consignments.add(con);
                        consignmentLiveData.setValue(consignments);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Consignment con=dataSnapshot.getValue(Consignment.class);
                consignments.remove(con);
                consignmentLiveData.setValue(consignments);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                Consignment con=dataSnapshot.getValue(Consignment.class);
                consignments.remove(con);
                consignmentLiveData.setValue(consignments);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists())
//                {
//                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
//                    {
//                        Consignment con=dataSnapshot1.getValue(Consignment.class);
//                        con.setId(dataSnapshot1.getKey());
//                        consignments.add(con);
//                    }
//                    consignmentLiveData.setValue(consignments);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return consignmentLiveData;
    }

    public LiveData<Consignment> UpdateConsignment(Consignment consignment) {
        MutableLiveData<Consignment> consignmentMutableLiveData=new MutableLiveData<>();
        databaseReference.child("PendingConsignments").child(consignment.getId()).setValue(consignment).addOnCompleteListener(task->{

                if(task.isSuccessful())
                {
                    consignmentMutableLiveData.setValue(consignment);
                }

        });
        return consignmentMutableLiveData;
    }


}
