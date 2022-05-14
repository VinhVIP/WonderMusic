package com.team28.wondermusic.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class SearchHistoryEntity(
    @PrimaryKey val keyword: String,
    val time: Date
)