package com.weather.network.response

// model class for next 7 day weather forecast
data class WeatherForecast(
    val list: ArrayList<WeatherList>,
)

data class WeatherList(
    val temp: Temp,
    val humidity: Double,
    val speed: Double,
    val weather: List<Weathers>,
)

data class Temp(
    val day: Double,
    val night: Double,
)

data class Weathers(
    val main: String,
    val description: String,
    val icon: String,
)