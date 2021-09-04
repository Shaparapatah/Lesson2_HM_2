package com.shaparapatah.lesson2_hm_2.domain

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = -1,
    val feelsLike: Int = -5
)

private fun getDefaultCity() = City("Москва", 55.0, 37.0)
