package com.example.weadther_a.remote_data;

import com.example.weadther_a.models.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {

    String URL_KEY = "71b42590f9c1204972f6786e6ec22fac";

    String BASE_URL = "https://api.openweathermap.org";

    @GET("/data/2.5/weather")
    Call<Model> getCurrentWeather(
            @Query("q")String name,
            @Query("appid")String key);

}

