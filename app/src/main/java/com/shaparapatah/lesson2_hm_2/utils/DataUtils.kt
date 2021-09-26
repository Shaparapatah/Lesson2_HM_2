package com.shaparapatah.lesson2_hm_2.utils

import com.shaparapatah.lesson2_hm_2.domain.City
import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.domain.getDefaultCity
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO
import com.shaparapatah.lesson2_hm_2.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): Weather {
    return (
            Weather(
                getDefaultCity(),
                weatherDTO.fact.temp,
                weatherDTO.fact.feels_like,
                weatherDTO.fact.condition
            )
            )
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.name, 0.0, 0.0), it.temperature, 0, it.condition)
    }

}

fun convertWeatherToHistoryEntity(weather: Weather): HistoryEntity {

    return HistoryEntity(0, weather.city.name, weather.temperature, weather.condition)
}
