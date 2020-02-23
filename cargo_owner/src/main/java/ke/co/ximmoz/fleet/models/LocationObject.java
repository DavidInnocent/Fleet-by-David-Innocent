package ke.co.ximmoz.fleet.models;

import java.io.Serializable;

public class LocationObject implements Serializable {
    private double latitude,longitude;
    private String TruckID;

    public String getTruckID() {
        return TruckID;
    }

    public void setTruckID(String truckID) {
        TruckID = truckID;
    }

    public LocationObject() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitud) {
        this.latitude = latitud;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
