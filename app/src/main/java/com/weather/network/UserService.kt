package com.weather.network

import android.content.Context
import android.widget.Toast
import com.weather.network.response.WeatherDetails
import com.weather.network.response.WeatherForecast
import com.weather.utils.Connectivity
import com.weather.utils.Const
import com.weather.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UserService {

    // api for current weather
    fun fetchDetails(
        context: Context,
        lat: String,
        lng: String,
        appId: String,
        onResult: (WeatherDetails?) -> Unit
    ) {
        if (Connectivity.isInternetAvailable(context)) {
            Utils.showProgressDialog(context)
            val retrofit = RetrofitClient.getClient()
            retrofit.fetchDetails(lat, lng, appId, "metric").enqueue(
                object : Callback<WeatherDetails> {
                    override fun onResponse(
                        call: Call<WeatherDetails>,
                        response: Response<WeatherDetails>
                    ) {
                        Utils.dismissDialog()
                        onResult(response.body())
                    }

                    override fun onFailure(call: Call<WeatherDetails>, t: Throwable) {
                        Utils.dismissDialog()
                        onResult(null)
                    }
                }
            )
        } else {
            Toast.makeText(context, Const.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
        }
    }

    // api for next 7 day forecast weather
    fun fetchForecastDetails(
        context: Context,
        lat: String,
        lng: String,
        appId: String,
        onResult: (WeatherForecast?) -> Unit
    ) {
        if (Connectivity.isInternetAvailable(context)) {
            Utils.showProgressDialog(context)
            val retrofit = RetrofitClient.getClient()
            retrofit.fetchForecastDetails(lat, lng,"7", appId, "metric").enqueue(
                object : Callback<WeatherForecast> {
                    override fun onResponse(
                        call: Call<WeatherForecast>,
                        response: Response<WeatherForecast>
                    ) {
                        Utils.dismissDialog()
                        onResult(response.body())
                    }

                    override fun onFailure(call: Call<WeatherForecast>, t: Throwable) {
                        Utils.dismissDialog()
                        onResult(null)
                    }
                }
            )
        } else {
            Toast.makeText(context, Const.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
        }
    }
}