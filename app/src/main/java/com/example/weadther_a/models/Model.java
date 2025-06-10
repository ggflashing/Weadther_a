package com.example.weadther_a.models;

import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("main")
    Main main_model;

    @SerializedName("wind")
    wind wind_model;

    @SerializedName("clouds")
    Clouds clouds_model;

    @SerializedName("sys")
    Sys sys_model;

    @SerializedName("timezone")
    long timeZone;

    public Main getMain_model() {
        return main_model;
    }

    public void setMain_model(Main main_model) {
        this.main_model = main_model;
    }

    public wind getWind_model() {
        return wind_model;
    }

    public void setWind_model(wind wind_model) {
        this.wind_model = wind_model;
    }

    public Clouds getClouds_model() {
        return clouds_model;
    }

    public void setClouds_model(Clouds clouds_model) {
        this.clouds_model = clouds_model;
    }

    public Sys getSys_model() {
        return sys_model;
    }

    public void setSys_model(Sys sys_model) {
        this.sys_model = sys_model;
    }

    public long getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(long timeZone) {
        this.timeZone = timeZone;
    }
}
