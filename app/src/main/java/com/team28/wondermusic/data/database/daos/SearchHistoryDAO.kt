package com.team28.wondermusic.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team28.wondermusic.data.database.entities.SearchHistoryEntity

@Dao
interface SearchHistoryDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistoryEntity)

    @Query("SELECT * FROM SearchHistoryEntity ORDER BY time DESC")
    suspend fun getAllSearchHistory(): List<SearchHistoryEntity>

    @Query("DELETE FROM AccountEntity")
    suspend fun deleteAll()
}