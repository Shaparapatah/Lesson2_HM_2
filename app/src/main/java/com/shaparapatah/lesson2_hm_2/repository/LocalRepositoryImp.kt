package com.shaparapatah.lesson2_hm_2.repository

import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.room.HistoryDAO
import com.shaparapatah.lesson2_hm_2.utils.convertHistoryEntityToWeather
import com.shaparapatah.lesson2_hm_2.utils.convertWeatherToHistoryEntity

class LocalRepositoryImp(private val localDataSource: HistoryDAO) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToHistoryEntity(weather))
    }

}