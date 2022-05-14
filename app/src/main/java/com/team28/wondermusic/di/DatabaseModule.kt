package com.team28.wondermusic.di

import android.content.Context
import androidx.room.Room
import com.team28.wondermusic.data.database.AppDB
import com.team28.wondermusic.data.database.daos.AccountDAO
import com.team28.wondermusic.data.database.daos.NotificationDAO
import com.team28.wondermusic.data.database.daos.SearchHistoryDAO
import com.team28.wondermusic.data.database.daos.SongDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDB(@ApplicationContext appContext: Context): AppDB {
        return Room.databaseBuilder(appContext, AppDB::class.java, "wonder_music_db").build()
    }

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context{
        return appContext
    }

    @Provides
    fun provideSongDao(appDB: AppDB): SongDAO {
        return appDB.songDao()
    }

    @Provides
    fun provideAccountDao(appDB: AppDB): AccountDAO {
        return appDB.accountDao()
    }

    @Provides
    fun provideNotificationDao(appDB: AppDB): NotificationDAO {
        return appDB.notificationDao()
    }

    @Provides
    fun provideSearchHistoryDao(appDB: AppDB): SearchHistoryDAO {
        return appDB.searchHistoryDao()
    }
}