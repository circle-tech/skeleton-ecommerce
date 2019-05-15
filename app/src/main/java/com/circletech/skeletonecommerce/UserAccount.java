package com.circletech.skeletonecommerce;

import java.io.Serializable;

public class UserAccount implements Serializable {

    String userName;
    String email;

    public UserAccount(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    //Getters
    public String getUserName() { return userName; }

    public String getEmail() { return email; }

    //Setters
    public void setUserName(String name) { this.userName = name; }

    public void setEmail(String email) { this.email = email; }
}
