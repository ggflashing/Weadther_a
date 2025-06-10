package com.example.weadther_a.models;

import com.google.gson.annotations.SerializedName;

public class Main {


    @SerializedName("temp")
    private Double temp;

    @SerializedName("name")
    private String name;

    @SerializedName("temp_max")
    private Double tempMax;

    @SerializedName("temp_min")
    private Double tempMin;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("pressure")
    private int pressure;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }


}
