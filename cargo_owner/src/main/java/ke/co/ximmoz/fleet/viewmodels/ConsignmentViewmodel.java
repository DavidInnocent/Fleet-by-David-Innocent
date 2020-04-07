package ke.co.ximmoz.fleet.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


import co.ke.ximmoz.commons.models.Consignment;
import co.ke.ximmoz.commons.utils.ConsignmentStatus;
import ke.co.ximmoz.fleet.repositories.ConsignmentRepository;


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

        consignment.setStatus(ConsignmentStatus.PENDING_PAYMENT.name());
        String number  = consignment.getDistance();
        String stripedValue = (number.replaceAll("[\\s+a-zA-Z : ,]",""));
        double dbl = Double.parseDouble(stripedValue);
        double finalAmountDue=(dbl*ratePerKm)+transactionCharge;
        consignment.setAmount(String.valueOf(finalAmountDue));
        consignment.setOwner(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return consignmentRepository.SaveConsignment(consignment);


    }
//    public MutableLiveData<List<Consignment>> GetConsignments()
//    {
////        return consignmentRepository.GetConsignments();
//    }

    public LiveData<Consignment> UpdateConsignment(Consignment consignment) {
        return consignmentRepository.UpdateConsignment(consignment);
    }

    public MutableLiveData<List<Consignment>> GetOwnerConsignments() {
        return consignmentRepository.GetOwnerConsignments();
    }
    public LiveData<QuerySnapshot> GetOwnerUpdatedConsignments() {
        return consignmentRepository.UpdateOwnerConsignments();
    }
}
