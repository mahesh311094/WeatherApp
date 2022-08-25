package com.weather.network

import com.google.gson.GsonBuilder
import com.weather.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor.Level

object RetrofitClient {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    private var client = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
        .build()

    private var mRetrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Const.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun getClient(): ApiService {
        return mRetrofit.create(ApiService::class.java)
    }
}