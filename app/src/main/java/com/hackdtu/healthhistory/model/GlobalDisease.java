package com.hackdtu.healthhistory.model;

import java.util.ArrayList;

/**
 * Created by piyush on 26/10/17.
 */

public class GlobalDisease {
    private String diseaseName;
    private ArrayList<String> personList;
    private int numCount;

    public GlobalDisease(String diseaseName, ArrayList<String> personList, int numCount) {
        this.diseaseName = diseaseName;
        this.personList = personList;
        this.numCount = numCount;
    }

    public int getNumCount() {

        return numCount;
    }

    public void setNumCount(int numCount) {
        this.numCount = numCount;
    }

    public GlobalDisease() {
    }

    public String getDiseaseName() {

        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }


    public ArrayList<String> getPersonList() {

        return personList;
    }

    public void setPersonList(ArrayList<String> personList) {
        this.personList = personList;
    }
}
