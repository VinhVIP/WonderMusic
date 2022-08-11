package com.vinh.wondermusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.vinh.wondermusic.data.database.daos.*
import com.vinh.wondermusic.data.database.entities.*
import java.util.*

@Database(
    entities = [
        SongEntity::class,
        AccountEntity::class,
        SongSingersEntity::class,
        AlbumEntity::class,
        TypeEntity::class,
        SongTypeEntity::class,
        NotificationEntity::class,
        SearchHistoryEntity::class,
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
    abstract fun albumDao(): AlbumDao
    abstract fun typeDao(): TypeDao
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