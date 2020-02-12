package ke.co.ximmoz.fleet.Viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.maps.model.LatLng;

import ke.co.ximmoz.fleet.Models.LocationObject;
import ke.co.ximmoz.fleet.Repositories.LocationRepository;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository locationRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationRepository=new LocationRepository();
    }
    public LiveData<LocationObject> GetTruckLocation(String consignmentID)
    {
        return locationRepository.GetLocationOfTruck(consignmentID);
    }

}
