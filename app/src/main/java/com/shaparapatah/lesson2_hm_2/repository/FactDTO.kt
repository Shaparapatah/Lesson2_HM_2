package com.shaparapatah.lesson2_hm_2.repository

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FactDTO(
    val temp: Long,
    val feels_like: Long,
    val condition: String,
    val icon : String
) : Parcelable







