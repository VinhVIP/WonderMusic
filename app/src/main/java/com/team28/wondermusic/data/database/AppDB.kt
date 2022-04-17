package com.team28.wondermusic.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.team28.wondermusic.data.database.question.QuestionDAO
import com.team28.wondermusic.data.database.question.QuestionEntity

@Database(entities = [QuestionEntity::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun questionDao(): QuestionDAO
}