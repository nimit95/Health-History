package com.hackdtu.healthhistory.model;

/**
 * Created by dell on 2/11/2017.
 */

public class User {
    private String name, userID;
    private String phone;
    private String blood_group;
    private String status;
    //Status left


    public User(){

    }
    public User(String name, String userID, String phone, String blood_group, String status) {
        this.name = name;
        this.userID = userID;
        this.phone = phone;
        this.blood_group = blood_group;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getPhone() {
        return phone;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public String getStatus() {
        return status;
    }
}
