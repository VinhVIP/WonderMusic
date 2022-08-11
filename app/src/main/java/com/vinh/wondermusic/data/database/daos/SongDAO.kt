package com.vinh.wondermusic.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vinh.wondermusic.data.database.entities.SongEntity
import com.vinh.wondermusic.data.database.entities.SongFullEntity
import com.vinh.wondermusic.data.database.entities.SongSingersEntity

@Dao
interface SongDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(songs: SongEntity)

    @Query("SELECT * FROM SongEntity")
    suspend fun getListTopSongs(): List<SongFullEntity>

    @Query("DELETE FROM SongEntity")
    suspend fun deleteListTopSongs()

    @Query("UPDATE SongEntity SET imagePath = :imagePath WHERE idSong = :idSong")
    suspend fun updateSongImagePath(idSong: Int, imagePath: String)

    @Query("UPDATE SongEntity SET filePath = :filePath WHERE idSong = :idSong")
    suspend fun updateSongFilePath(idSong: Int, filePath: String)

    // Song - Singers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongSingers(songSingers: SongSingersEntity)

}