package com.example.weadther_a.models;

import com.google.gson.annotations.SerializedName;

public class WeatherModel {

    @SerializedName("icon")
    String icon;

    @SerializedName("cod")
    Integer cod;


    public String getIcon() {
        return icon;
    }

    public Integer getCod() {
        return cod;
    }
}
