package com.shaparapatah.lesson2_hm_2.view.details

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.shaparapatah.lesson2_hm_2.*
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class DetailsService(name: String = "details") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_EXTRA, -1.0)
            val lon = it.getDoubleExtra(LONGITUDE_EXTRA, -1.0)
            loadWeather(lat, lon)
        }


    }


    private fun loadWeather(lat: Double, lon: Double) {
        try {
            val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
            Thread {
                val urlConnection = url.openConnection() as HttpsURLConnection
                try {
                    urlConnection.requestMethod = "GET"
                    urlConnection.addRequestProperty(
                        YANDEX_API_KEY_NAME,
                        BuildConfig.WEATHER_API_KEY
                    )
                    urlConnection.readTimeout = 10000
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)

                    val mySendIntent = Intent(DETAILS_INTENT_FILTER)
                    mySendIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, weatherDTO)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(mySendIntent)



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