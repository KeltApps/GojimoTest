package com.gojimo.gojimotest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergio on 12/05/16 for KelpApps.
 */
public class Subject {
    @SerializedName("id")
    private String idSubject;
    @SerializedName("title")
    private String title;
    @SerializedName("colour")
    private String colour;

    public String getIdSubject() {
        return idSubject;
    }

    public void setIdSubject(String idSubject) {
        this.idSubject = idSubject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
