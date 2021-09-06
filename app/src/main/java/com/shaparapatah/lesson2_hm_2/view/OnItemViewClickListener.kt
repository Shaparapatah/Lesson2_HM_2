package com.shaparapatah.lesson2_hm_2.view

import com.shaparapatah.lesson2_hm_2.domain.Weather

interface OnItemViewClickListener {
    fun onItemClick(weather: Weather)
}