package com.hackdtu.healthhistory.model;

/**
 * Created by piyush on 26/10/17.
 */

public class Disease {
    private String title;
    private String time;
    private String symptoms,description;

    public Disease() {
    }

    public Disease(String title, String time, String symptoms, String description) {
        this.title = title;
        this.time = time;
        this.symptoms = symptoms;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
