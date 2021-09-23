package com.shaparapatah.lesson2_hm_2.utils

import com.shaparapatah.lesson2_hm_2.domain.Weather
import com.shaparapatah.lesson2_hm_2.domain.getDefaultCity
import com.shaparapatah.lesson2_hm_2.repository.WeatherDTO

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    return listOf(
        Weather(
            getDefaultCity(),
            weatherDTO.fact.temp,
            weatherDTO.fact.feels_like,
            weatherDTO.fact.condition
        )
    )
}