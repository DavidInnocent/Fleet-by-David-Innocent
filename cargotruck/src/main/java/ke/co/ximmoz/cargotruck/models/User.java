package ke.co.ximmoz.cargotruck.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String uid;
    private String isDriver;
    private String password;
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public void setIsDriver(String isDriver) {
        this.isDriver = isDriver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User() {
    }


}
