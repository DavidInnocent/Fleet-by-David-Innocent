package ke.co.ximmoz.fleet.Models;

import java.io.Serializable;

public class User implements Serializable {
    private String username,email,uid,isDriver;

    public User() {
    }

    public User(String username, String email, String uid, String isDriver) {
        this.username = username;
        this.email = email;
        this.uid=uid;
        this.isDriver=isDriver;
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

    public void setIsDriver(String isDriver) {
        this.isDriver=isDriver;
    }
}
