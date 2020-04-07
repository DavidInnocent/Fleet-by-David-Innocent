package co.ke.ximmoz.commons.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;



public class Consignment implements Serializable {
    String id;
    String container_size;
    String amount;
    String status;
    String owner;
    String driver;
    String date_of_pickup;
    String date_delivered;
    String distance;
    String destinationAddress;
    String destinationName;
    String consignmentBillOfLadding;
    double destinationLat;
    double destinationLng;
    String pickupAddress;
    String pickupName;
    double pickupLat;
    double pickupLng;
    int consignmentTransactionNumber;

    public String getConsignmentBillOfLadding() {
        return consignmentBillOfLadding;
    }

    public void setConsignmentBillOfLadding(String consignmentBillOfLadding) {
        this.consignmentBillOfLadding = consignmentBillOfLadding;
    }

    public int getConsignmentTransactionNumber() {
        return consignmentTransactionNumber;
    }

    @ServerTimestamp
    public transient Timestamp pickupTime;

    @ServerTimestamp
    public transient Timestamp arrivalTime;


    public Timestamp getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(Timestamp pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Timestamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Timestamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupName() {
        return pickupName;
    }

    public void setPickupName(String pickupName) {
        this.pickupName = pickupName;
    }

    public double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public double getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(double pickupLng) {
        this.pickupLng = pickupLng;
    }

    public Consignment() {

    }

}

