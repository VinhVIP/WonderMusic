package com.vinh.wondermusic.di

import android.content.Context
import androidx.room.Room
import com.vinh.wondermusic.data.database.AppDB
import com.vinh.wondermusic.data.database.daos.*
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
    fun provideAlbumDao(appDB: AppDB): AlbumDao {
        return appDB.albumDao()
    }

    @Provides
    fun provideTypeDao(appDB: AppDB): TypeDao {
        return appDB.typeDao()
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