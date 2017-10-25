package com.hackdtu.healthhistory.model;

/**
 * Created by piyush on 25/10/17.
 */

public class SugarLevel {
    private String time;
    private String value;

    public SugarLevel() {
    }

    public SugarLevel(String time, String value) {
        this.time = time;
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
