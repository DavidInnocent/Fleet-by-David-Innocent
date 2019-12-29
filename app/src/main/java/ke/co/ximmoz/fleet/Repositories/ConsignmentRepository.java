package ke.co.ximmoz.fleet.Repositories;

import android.content.Intent;
import android.view.contentcapture.DataRemovalRequest;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Views.CargoOwnerActitivy;
import ke.co.ximmoz.fleet.Views.DestinationChooserActivity;

public class ConsignmentRepository {
    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("PendingConsignments");
    public LiveData<String> SaveConsignment(Consignment consignment) {
        final MutableLiveData<String> successMessage=new MutableLiveData<>();

        databaseReference.push().setValue(consignment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    successMessage.setValue("done");
                }
                else
                {
                    successMessage.setValue(null);
                    }
            }

        });
        return successMessage;
    }

    public MutableLiveData<Consignment> GetConsignments()
    {
        MutableLiveData<Consignment> consignmentLiveData=new MutableLiveData<>();
        databaseReference.orderByChild("status").equalTo("active").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Consignment consignments=dataSnapshot.getValue(Consignment.class);
                consignmentLiveData.setValue(consignments);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return consignmentLiveData;
    }
}
