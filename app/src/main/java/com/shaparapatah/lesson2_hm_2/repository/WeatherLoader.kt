package com.shaparapatah.lesson2_hm_2.repository

import android.os.Handler
import android.os.Looper
import android.util.Log

import com.google.gson.Gson
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

    companion object {
        private const val YOUR_API_KEY = "X-Yandex-API-Key"
        private const val KEY = "06e57aba-81f3-4ca5-93d2-d50ce5b048a3"
    }

    fun loadWeather() {
        try {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            val handler = Handler(Looper.getMainLooper())
            val urlConnection = url.openConnection() as HttpsURLConnection
            Thread {
                try {

                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        YOUR_API_KEY,
                        KEY
                    )
                    urlConnection.readTimeout = 10000
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)

                    handler.post { listener.onLoaded(weatherDTO) }
                    urlConnection.disconnect()
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
        }
    }
}