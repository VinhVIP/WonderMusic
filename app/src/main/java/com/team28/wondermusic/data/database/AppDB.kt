package com.team28.wondermusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team28.wondermusic.data.database.daos.AccountDAO
import com.team28.wondermusic.data.database.daos.NotificationDAO
import com.team28.wondermusic.data.database.daos.SongDAO
import com.team28.wondermusic.data.database.entities.*

@Database(
    entities = [
        TopSongEntity::class,
        MySongEntity::class,
        FavoriteSongEntity::class,
        AccountEntity::class,
        SongSingersEntity::class,
        NotificationEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDB : RoomDatabase() {

    abstract fun songDao(): SongDAO
    abstract fun accountDao(): AccountDAO
    abstract fun notificationDao(): NotificationDAO
}