package com.hackdtu.healthhistory.model;

/**
 * Created by dell on 2/11/2017.
 */

public class UserHistory {


    private String history_pic;

    private String user;

    private String time;
    private String image_title;
    private String Disease;
    private String blood_sugar_level;
    public String getHistory_pic() {
        return history_pic;
    }

    public void setHistory_pic(String history_pic) {
        this.history_pic = history_pic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImage_title() {
        return image_title;
    }

    public void setImage_title(String image_title) {
        this.image_title = image_title;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getBlood_sugar_level() {
        return blood_sugar_level;
    }

    public void setBlood_sugar_level(String blood_sugar_level) {
        this.blood_sugar_level = blood_sugar_level;
    }


}
