package ke.co.ximmoz.fleet.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String uid;
    private String isDriver;


    private String phone;

    public User() {
    }

    public User(String username, String email, String uid, String isDriver, String phone) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.isDriver = isDriver;
        this.phone = phone;
    }




    public User(String username, String email, String uid, String phone) {
        this.username = username;
        this.email = email;
        this.uid = uid;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
    public String getuid() {
        return uid;
    }

    public String getIsDriver() {
        return isDriver;
    }
    public String getPhone() {
        return phone;
    }


    public void setIsDriver(String isDriver) {
        this.isDriver=isDriver;
    }
}
