package ke.co.ximmoz.fleet.views.Utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ke.co.ximmoz.fleet.models.LocationObject;
import ke.co.ximmoz.fleet.repositories.LocationRepository;

public class LocationViewmodel extends AndroidViewModel {
    public LiveData<Boolean> truckLocationSaved;
    LocationRepository locationRepository;
    public LocationViewmodel(@NonNull Application application) {
        super(application);
        locationRepository=new LocationRepository();


    }
    public LiveData<String> SetLocationOfTruck(LocationObject locationObject) {


        return locationRepository.SetLocationOfTruck(locationObject);
    }
}
