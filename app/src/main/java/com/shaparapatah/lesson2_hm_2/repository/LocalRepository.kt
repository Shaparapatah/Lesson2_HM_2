package com.shaparapatah.lesson2_hm_2.repository

import com.shaparapatah.lesson2_hm_2.domain.Weather

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
}