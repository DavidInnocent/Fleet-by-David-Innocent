package ke.co.ximmoz.fleet.Models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Consignment implements Serializable {
    String id,container_size, amount,status,owner,driver,date_of_pickup,date_delivered,pickup_location;
    double destination_lat;

    public double getDestination_lat() {
        return destination_lat;
    }

    public void setDestination_lat(double destination_lat) {
        this.destination_lat = destination_lat;
    }

    public double getDestination_lng() {
        return destination_lng;
    }

    public void setDestination_lng(double destination_lng) {
        this.destination_lng = destination_lng;
    }

    double destination_lng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainer_size() {
        return container_size;
    }

    public void setContainer_size(String container_size) {
        this.container_size = container_size;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDate_of_pickup() {
        return date_of_pickup;
    }

    public void setDate_of_pickup(String date_of_pickup) {
        this.date_of_pickup = date_of_pickup;
    }

    public String getDate_delivered() {
        return date_delivered;
    }

    public void setDate_delivered(String date_delivered) {
        this.date_delivered = date_delivered;
    }


    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }


    public Consignment() {

    }

}

