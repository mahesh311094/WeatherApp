package com.weather.network

import com.weather.network.response.WeatherDetails
import com.weather.network.response.WeatherForecast
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // api for current weather
    @GET("2.5/weather/")
    fun fetchDetails(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Call<WeatherDetails>

    // api for next 7 day forecast weather
    @GET("2.5/forecast/daily")
    fun fetchForecastDetails(
        @Query("lat") lat: String,
        @Query("lon") lng: String,
        @Query("cnt") cnt: String,
        @Query("appid") appId: String,
        @Query("units") units: String,
    ): Call<WeatherForecast>
}