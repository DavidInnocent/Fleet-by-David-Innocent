package ke.co.ximmoz.cargotruck.repositories;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import co.ke.ximmoz.commons.models.Consignment;
import ke.co.ximmoz.cargotruck.utils.ConsignmentStatus;
import ke.co.ximmoz.cargotruck.utils.ConsignmentTransactionCounter;
import ke.co.ximmoz.cargotruck.utils.Shard;


public class ConsignmentRepository {


    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    int numShards=20;
    public static final String consignmentDocument = "Consignments";
    DocumentReference documentReference = firestore.collection(consignmentDocument).document();

    public LiveData<String> SaveConsignment(Consignment consignment) {
        final MutableLiveData<String> successMessage = new MutableLiveData<>();

        consignment.setId(documentReference.getId());

        /*
        * Try to increment counter first.
        *
        *
        * */

        Task<Void> incrementCounter=incrementCounter();

        if(incrementCounter.isSuccessful())
        {
            Task<Integer> orderNumber=getCount();
            orderNumber.addOnCompleteListener(new OnCompleteListener<Integer>() {
                @Override
                public void onComplete(@NonNull Task<Integer> task) {
                    if(task.isSuccessful())
                    {

                    }

                }
            });

        }

        documentReference.set(consignment).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                successMessage.setValue("1");
            } else {
                successMessage.setValue("0");

            }
        });
        return successMessage;
    }



    public MutableLiveData<List<Consignment>> GetOwnerConsignments() {
        MutableLiveData<List<Consignment>> OwnerconsignmentLiveData = new MutableLiveData<>();
        List<Consignment> consignmentss = new ArrayList<>();

        Query query=firestore.collection(consignmentDocument).whereEqualTo("owner",firebaseUser.getUid());


        query.get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document :
                        task.getResult()) {
                    consignmentss.add(document.toObject(Consignment.class));

                }
                OwnerconsignmentLiveData.setValue(consignmentss);
            }

        });

        return OwnerconsignmentLiveData;
    }





    public MutableLiveData<List<Consignment>> GetAllConsignments() {
        MutableLiveData<List<Consignment>> AllConsignments = new MutableLiveData<>();
        List<Consignment> consignmentss = new ArrayList<>();

       firestore.collection(consignmentDocument).whereEqualTo("status", ConsignmentStatus.PENDING_PICKUP).get().addOnCompleteListener(task -> {

                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot q :
                            task.getResult()) {
                        consignmentss.add(q.toObject(Consignment.class));
                    }

                    AllConsignments.setValue(consignmentss);

                }

       });
        return AllConsignments;
    }








    public MutableLiveData<QuerySnapshot> UpdateOwnerConsignments() {
        MutableLiveData<QuerySnapshot> OwnerUpdatedconsignmentLiveData = new MutableLiveData<>();
//        QuerySnapshot consignmentss = new ArrayList<>();

        Query query=firestore.collection(consignmentDocument).whereEqualTo("owner",firebaseUser.getUid());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                {
                    return;
                }
//                for(DocumentChange dc:queryDocumentSnapshots.getDocumentChanges())
//                {
//                    switch (dc.getType()){
//                        case MODIFIED:
//                        consignmentss.add(dc.getDocument().toObject(Consignment.class));
//                        break;
//                    }
//
//                }
                OwnerUpdatedconsignmentLiveData.setValue(queryDocumentSnapshots);
            }
        });

        return OwnerUpdatedconsignmentLiveData;
    }


//    public MutableLiveData<List<Consignment>> GetConsignments() {
//        MutableLiveData<List<Consignment>> consignmentLiveData = new MutableLiveData<>();
//        List<Consignment> consignments = new ArrayList<>();
//        firestore.collection("Consignments").get();
//        Query query = databaseReference.child("PendingConsignments").orderByChild("status").equalTo("AwaitingPickup");
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                if (dataSnapshot.exists()) {
//
//                    Consignment con = dataSnapshot.getValue(Consignment.class);
//                    con.setId(dataSnapshot.getKey());
//                    consignments.add(con);
//                    consignmentLiveData.setValue(consignments);
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                Consignment con = dataSnapshot.getValue(Consignment.class);
//                consignments.remove(con);
//                consignmentLiveData.setValue(consignments);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                Consignment con = dataSnapshot.getValue(Consignment.class);
//                consignments.remove(con);
//                consignmentLiveData.setValue(consignments);
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
////        query.addValueEventListener(new ValueEventListener() {
////            @Override
////            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                if(dataSnapshot.exists())
////                {
////                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
////                    {
////                        Consignment con=dataSnapshot1.getValue(Consignment.class);
////                        con.setId(dataSnapshot1.getKey());
////                        consignments.add(con);
////                    }
////                    consignmentLiveData.setValue(consignments);
////                }
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////            }
////        });
//        return consignmentLiveData;
//    }

    public LiveData<Consignment> UpdateConsignment(Consignment consignment) {
        MutableLiveData<Consignment> consignmentMutableLiveData = new MutableLiveData<>();
        databaseReference.child("PendingConsignments").child(consignment.getId()).setValue(consignment).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                consignmentMutableLiveData.setValue(consignment);
            }

        });
        return consignmentMutableLiveData;
    }



    DocumentReference ref=firestore.collection(consignmentDocument).document("counter");
    public Task<Void> createCounter() {
        // Initialize the counter document, then initialize each shard.
        return ref.set(new ConsignmentTransactionCounter(numShards))
                .continueWithTask(new Continuation<Void, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<Void> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        List<Task<Void>> tasks = new ArrayList<>();

                        // Initialize each shard with count=0
                        for (int i = 0; i < numShards; i++) {
                            Task<Void> makeShard = ref.collection("shards")
                                    .document(String.valueOf(i))
                                    .set(new Shard(0));

                            tasks.add(makeShard);
                        }

                        return Tasks.whenAll(tasks);
                    }
                });
    }




    public Task<Void> incrementCounter() {
        int shardId = (int) Math.floor(Math.random() * numShards);
        DocumentReference shardRef = ref.collection("shards").document(String.valueOf(shardId));

        return shardRef.update("count", FieldValue.increment(1));
    }

    public Task<Integer> getCount() {
        // Sum the count of each shard in the subcollection
        return ref.collection("shards").get()
                .continueWith(new Continuation<QuerySnapshot, Integer>() {
                    @Override
                    public Integer then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        int count = 0;
                        for (DocumentSnapshot snap : task.getResult()) {
                            Shard shard = snap.toObject(Shard.class);
                            count += shard.getCount();
                        }
                        return count;
                    }
                });
    }

}







