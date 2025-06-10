package com.example.weadther_a.remote_data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit_Bulder {


    private Retrofit_Bulder() {

    }


    private  static  WeatherApi instance;

    public static  WeatherApi getInstance() {
        if (instance == null) {
            instance = createRetrofit();

        }

    return instance ;
    }

    private  static WeatherApi createRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(WeatherApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi.class);

        
    }





}
