package com.team28.wondermusic.di

import android.content.Context
import androidx.room.Room
import com.team28.wondermusic.data.database.AppDB
import com.team28.wondermusic.data.database.question.QuestionDAO
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
        return Room.databaseBuilder(appContext, AppDB::class.java, "appdp").build()
    }

    @Provides
    fun provideQuestionDao(appDB: AppDB): QuestionDAO {
        return appDB.questionDao()
    }
}