package ke.co.ximmoz.fleet.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import ke.co.ximmoz.fleet.Models.Consignment;
import ke.co.ximmoz.fleet.Repositories.ConsignmentRepository;

public class ConsignmentViewmodel extends AndroidViewModel {
    private ConsignmentRepository consignmentRepository;
    public ConsignmentViewmodel(@NonNull Application application) {
        super(application);
        consignmentRepository=new ConsignmentRepository();
    }
    public LiveData<String> SaveConsignment(Consignment consignment)
    {
        return consignmentRepository.SaveConsignment(consignment);
    }
    public MutableLiveData<Consignment> GetConsignments(Consignment consignment)
    {
        return consignmentRepository.GetConsignments();
    }
}
