package com.gojimo.gojimotest.models;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;


public class Qualification {
    @SerializedName("id")
    private String idQualification;
    @SerializedName("name")
    private String name;
    @SerializedName("subjects")
    private LinkedList<Subject> subjectsLinkedList;
    @SerializedName("country")
    private Country country;

    public String getIdQualification() {
        return idQualification;
    }

    public void setIdQualification(String idQualification) {
        this.idQualification = idQualification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Subject> getSubjectsLinkedList() {
        return subjectsLinkedList;
    }

    public void setSubjectsLinkedList(LinkedList<Subject> subjectsLinkedList) {
        this.subjectsLinkedList = subjectsLinkedList;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
