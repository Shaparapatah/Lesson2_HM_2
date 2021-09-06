package ru.geekbrains.lesson_1423_2_2_main.repository

import com.shaparapatah.lesson2_hm_2.domain.Weather

interface Repository {
    fun getWeatherFromRemoteSource(): Weather
    fun getWeatherFromLocalSource(): Weather

    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>

}