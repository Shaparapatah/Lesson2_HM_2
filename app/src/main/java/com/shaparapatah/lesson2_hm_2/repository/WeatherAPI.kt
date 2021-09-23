package com.shaparapatah.lesson2_hm_2.repository

import com.shaparapatah.lesson2_hm_2.utils.YANDEX_API_URL_END_POINT
import retrofit2.http.GET

interface WeatherAPI {

    @GET(YANDEX_API_URL_END_POINT)
    fun getWeather()
}