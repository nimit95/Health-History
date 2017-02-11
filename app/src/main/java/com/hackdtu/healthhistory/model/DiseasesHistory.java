package com.hackdtu.healthhistory.model;

import java.util.List;

/**
 * Created by dell on 2/11/2017.
 */

public class DiseasesHistory {
    List<Diseases> diseasesList;

    public List<Diseases> getDiseasesList() {
        return diseasesList;
    }

    public void setDiseasesList(List<Diseases> diseasesList) {
        this.diseasesList = diseasesList;
    }
}
