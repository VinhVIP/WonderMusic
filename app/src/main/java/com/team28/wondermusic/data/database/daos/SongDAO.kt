package com.team28.wondermusic.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team28.wondermusic.data.database.entities.*

@Dao
interface SongDAO {

    // ----- Top Song -----
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListTopSongs(songs: List<TopSongEntity>)

    @Query("SELECT * FROM TopSongEntity")
    suspend fun getListTopSongs(): List<TopSong>

    @Query("DELETE FROM TopSongEntity")
    suspend fun deleteListTopSongs()


    // ----- Favorite Song -----
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListFavoriteSongs(songs: List<FavoriteSongEntity>)

    @Query("SELECT * FROM FavoriteSongEntity")
    suspend fun getListFavoriteSongs(): List<FavoriteSong>

    @Query("DELETE FROM FavoriteSongEntity")
    suspend fun deleteListFavoriteSongs()


    // ----- My Song -----
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListMySongs(songs: List<MySongEntity>)

    @Query("SELECT * FROM MySongEntity")
    suspend fun getListMySongs(): List<MySong>

    @Query("DELETE FROM MySongEntity")
    suspend fun deleteListMySongs()


    // Song - Singers
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSongSingers(songSingers: SongSingersEntity)

}