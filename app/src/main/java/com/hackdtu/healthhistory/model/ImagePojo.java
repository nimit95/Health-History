package com.hackdtu.healthhistory.model;

/**
 * Created by piyush on 25/10/17.
 */

public class ImagePojo {
    String title, timeStamp, url, description, imgType;

    public ImagePojo(){}
    public ImagePojo(String title, String timeStamp, String url, String description, String imgType) {
        this.title = title;
        this.timeStamp = timeStamp;
        this.url = url;
        this.description = description;
        this.imgType = imgType;
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
}
