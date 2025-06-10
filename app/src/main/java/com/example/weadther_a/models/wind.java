package com.example.weadther_a.models;

import com.google.gson.annotations.SerializedName;

public class wind {

    @SerializedName("speed")
    private double speed;

    public double getSpeed() {
        return speed;
    }
}
