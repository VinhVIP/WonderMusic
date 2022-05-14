package com.team28.wondermusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.team28.wondermusic.data.database.daos.AccountDAO
import com.team28.wondermusic.data.database.daos.NotificationDAO
import com.team28.wondermusic.data.database.daos.SearchHistoryDAO
import com.team28.wondermusic.data.database.daos.SongDAO
import com.team28.wondermusic.data.database.entities.*
import java.util.*

@Database(
    entities = [
        TopSongEntity::class,
        MySongEntity::class,
        FavoriteSongEntity::class,
        AccountEntity::class,
        SongSingersEntity::class,
        NotificationEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {

    abstract fun songDao(): SongDAO
    abstract fun accountDao(): AccountDAO
    abstract fun notificationDao(): NotificationDAO
    abstract fun searchHistoryDao(): SearchHistoryDAO
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}