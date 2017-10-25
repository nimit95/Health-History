package com.hackdtu.healthhistory.model;

/**
 * Created by piyush on 25/10/17.
 */

public class BloodPressure {
    private String time;
    private String bpValue;

    public BloodPressure() {
    }

    public BloodPressure(String time, String bpValue) {

        this.time = time;
        this.bpValue = bpValue;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBpValue() {
        return bpValue;
    }

    public void setBpValue(String bpValue) {
        this.bpValue = bpValue;
    }
}
