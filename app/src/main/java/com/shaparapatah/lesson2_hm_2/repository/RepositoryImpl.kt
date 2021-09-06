package ru.geekbrains.lesson_1423_2_2_main.repository

import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.domain.getRussianCities
import com.shaparapatah.lesson2_hm_2.domain.getWorldCities

class RepositoryImpl : Repository {
    override fun getWeatherFromRemoteSource(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalSource(): Weather {
        return Weather()
    }

    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return getRussianCities()
    }

    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }

}