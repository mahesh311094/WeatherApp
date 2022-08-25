package com.weather.network.response

// model class for current weather
data class WeatherDetails(
    val main: WeatherMain,
    val weather: ArrayList<Weather>,
    val wind: WeatherWind
)

data class Weather(
    val main: String,
    val description: String,
    val icon: String,
)

data class WeatherMain(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double,
)

data class WeatherWind(
    val speed: Double,
    val deg: Double,
)