package ke.co.ximmoz.fleet.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import ke.co.ximmoz.fleet.models.Consignment;
import ke.co.ximmoz.fleet.repositories.ConsignmentRepository;
import ke.co.ximmoz.fleet.views.Utils.ConsignmentStatus;

public class ConsignmentViewmodel extends AndroidViewModel {
    public Consignment consignment;

    private int transactionCharge=3000;
    private  int ratePerKm=200;

    private ConsignmentRepository consignmentRepository;
    public ConsignmentViewmodel(@NonNull Application application) {
        super(application);
        consignmentRepository=new ConsignmentRepository();
        consignment=new Consignment();
    }
    public LiveData<String> SaveConsignment()
    {

        consignment.setStatus(ConsignmentStatus.PENDING_PAYMENT);
        String number  = consignment.getDistance().replaceAll("[^0-9]", "");
        int distanceBetweenPickupAndDestination=Integer.parseInt(number);
        int finalAmountDue=(distanceBetweenPickupAndDestination*ratePerKm)+transactionCharge;
        consignment.setAmount(String.valueOf(finalAmountDue));
        consignment.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return consignmentRepository.SaveConsignment(consignment);
    }
    public MutableLiveData<List<Consignment>> GetConsignments()
    {
        return consignmentRepository.GetConsignments();
    }

    public LiveData<Consignment> UpdateConsignment(Consignment consignment) {
        return consignmentRepository.UpdateConsignment(consignment);
    }

    public MutableLiveData<List<Consignment>> GetOwnerConsignments() {
        return consignmentRepository.GetOwnerConsignments();
    }
}
