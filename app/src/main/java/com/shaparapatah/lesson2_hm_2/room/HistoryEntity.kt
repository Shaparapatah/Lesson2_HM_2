package com.shaparapatah.lesson2_hm_2.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val temperature: Long,
    val condition: String
)
