package com.hackdtu.healthhistory.model;

/**
 * Created by dell on 2/11/2017.
 */

public class UserHistory {
    private String history_pic;

    private String title;

    private String description;
    private String Disease;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHistory_pic() {
        return history_pic;
    }

    public void setHistory_pic(String history_pic) {
        this.history_pic = history_pic;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }
}
