package ke.co.ximmoz.fleet.Repositories;

import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Views.CargoOwnerActitivy;
import ke.co.ximmoz.fleet.Views.DestinationChooserActivity;

public class ConsignmentRepository {
    public LiveData<String> SaveConsignment(Consignment consignment) {
        final MutableLiveData<String> successMessage=new MutableLiveData<>();

        FirebaseDatabase.getInstance().getReference().child("PendingConsignments").push().setValue(consignment).addOnCompleteListener(new OnCompleteListener<Void>() {
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
}
