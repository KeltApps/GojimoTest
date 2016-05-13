package com.gojimo.gojimotest.models;

import com.google.gson.annotations.SerializedName;


public class Country {
    @SerializedName("name")
    private String nameCountry;

    public Country(String nameCountry) {
        this.nameCountry = nameCountry;
    }

    public String getNameCountry() {
        return nameCountry;
    }

    public void setNameCountry(String nameCountry) {
        this.nameCountry = nameCountry;
    }
}
