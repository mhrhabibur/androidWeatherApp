package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiInterface {
    @GET("weather")
    Call<CurrentWeather> getCurrentWeather(@Query("q") String city, @Query("appid") String api_key);
}
