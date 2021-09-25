package com.shaparapatah.lesson2_hm_2.repository

import com.google.gson.GsonBuilder
import com.shaparapatah.lesson2_hm_2.BuildConfig
import com.shaparapatah.lesson2_hm_2.utils.YANDEX_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RemoteDataSource {
    private val weatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(YANDEX_API_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(WeatherAPI::class.java)
    }

    fun getWeatherDetails(lat: Double, lon: Double, callback: retrofit2.Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
}