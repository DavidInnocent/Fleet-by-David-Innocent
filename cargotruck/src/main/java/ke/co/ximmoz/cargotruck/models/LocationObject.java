package ke.co.ximmoz.cargotruck.models;

import java.io.Serializable;

public class LocationObject implements Serializable {
    private double latitude,longitude;
    private String TruckID;
    private String ConsignmentID;

    public String getConsignmentID() {
        return ConsignmentID;
    }

    public void setConsignmentID(String setConsignmentID) {
        this.ConsignmentID = setConsignmentID;
    }

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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
