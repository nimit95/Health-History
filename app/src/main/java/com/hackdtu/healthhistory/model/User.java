package com.hackdtu.healthhistory.model;

/**
 * Created by dell on 2/11/2017.
 */

public class User {
    private String first_name;
    private String last_name;
    private String phone;
    private String blood_group;
    private String status;
    //Status left
    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
