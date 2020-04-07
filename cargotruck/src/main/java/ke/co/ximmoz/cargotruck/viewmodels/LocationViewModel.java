package ke.co.ximmoz.cargotruck.viewmodels;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ke.co.ximmoz.cargotruck.models.LocationObject;
import ke.co.ximmoz.cargotruck.repositories.LocationRepository;

public class LocationViewModel extends AndroidViewModel
{
    LocationRepository locationRepository;
    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository=new LocationRepository();

    }

    public LiveData<String> SetLocationOfTruck(LocationObject locationObject){
        return locationRepository.SetLocationOfTruck(locationObject);
    }
}
