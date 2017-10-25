package com.hackdtu.healthhistory.model;

import java.util.ArrayList;

/**
 * Created by piyush on 25/10/17.
 */

public class ImagePojo {
    private String title, timeStamp, url, description, imgType;
    private ArrayList<SugarLevel> sugarLevelFasting, sugarLevelPP;
    private ArrayList<BloodPressure> bloodPressures;


    public ImagePojo(String title, String timeStamp, String url, String description, String imgType, ArrayList<SugarLevel> sugarLevelFasting, ArrayList<SugarLevel> sugarLevelPP, ArrayList<BloodPressure> bloodPressures) {
        this.title = title;
        this.timeStamp = timeStamp;
        this.url = url;
        this.description = description;
        this.imgType = imgType;
        this.sugarLevelFasting = sugarLevelFasting;
        this.sugarLevelPP = sugarLevelPP;
        this.bloodPressures = bloodPressures;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public ArrayList<SugarLevel> getSugarLevelFasting() {
        return sugarLevelFasting;
    }

    public void setSugarLevelFasting(ArrayList<SugarLevel> sugarLevelFasting) {
        this.sugarLevelFasting = sugarLevelFasting;
    }

    public ArrayList<SugarLevel> getSugarLevelPP() {
        return sugarLevelPP;
    }

    public void setSugarLevelPP(ArrayList<SugarLevel> sugarLevelPP) {
        this.sugarLevelPP = sugarLevelPP;
    }

    public ArrayList<BloodPressure> getBloodPressures() {
        return bloodPressures;
    }

    public void setBloodPressures(ArrayList<BloodPressure> bloodPressures) {
        this.bloodPressures = bloodPressures;
    }

    public ImagePojo() {

    }
}