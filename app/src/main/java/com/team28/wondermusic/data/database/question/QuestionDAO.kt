package com.team28.wondermusic.data.database.question

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuestionDAO {
    @Insert
    suspend fun insertAll(questions: List<QuestionEntity>)

    @Query("SELECT * FROM question")
    suspend fun getAll(): List<QuestionEntity>

    @Query("DELETE FROM question")
    suspend fun deleteAll()
}