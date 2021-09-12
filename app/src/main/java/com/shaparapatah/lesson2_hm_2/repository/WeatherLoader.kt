package com.shaparapatah.lesson2_hm_2.repository

import android.os.Handler
import android.os.Looper
import android.util.Log

import com.google.gson.Gson
import com.shaparapatah.lesson2_hm_2.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(
    private val listener: WeatherLoaderListener,
    private val lat: Double,
    private val lon: Double

) {

    fun loadWeather() {
        try {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val handler = Handler(Looper.getMainLooper())
            Thread {
                val urlConnection = url.openConnection() as HttpsURLConnection
                try {
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        "X-Yandex-API-Key",
                        BuildConfig.WEATHER_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)

                    handler.post { listener.onLoaded(weatherDTO) }
                    urlConnection.disconnect()
                } catch (e: Exception) {
                    Log.e("myLogs", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("myLogs", "Fail URI", e)
            e.printStackTrace()
        }
    }
}