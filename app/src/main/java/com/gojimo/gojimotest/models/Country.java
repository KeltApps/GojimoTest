package com.gojimo.gojimotest.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sergio on 12/05/16 for KelpApps.
 */
public class Country {
    @SerializedName("name")
    private String nameCountry;

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }
}
