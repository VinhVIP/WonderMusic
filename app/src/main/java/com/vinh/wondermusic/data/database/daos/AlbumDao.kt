package com.vinh.wondermusic.data.database.daos

import androidx.room.*
import com.vinh.wondermusic.data.database.entities.AlbumEntity

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(albumEntity: AlbumEntity)

    @Update
    suspend fun updateAlbum(albumEntity: AlbumEntity)

    @Delete
    suspend fun deleteAlbum(albumEntity: AlbumEntity)

    @Query("DELETE FROM AlbumEntity WHERE idAlbum = :idAlbum")
    suspend fun deleteAlbumById(idAlbum: Int)

    @Query("SELECT * FROM AlbumEntity")
    suspend fun getAllAlbum(): List<AlbumEntity>

    @Query("DELETE FROM AlbumEntity")
    suspend fun deleteAllAlbum()
}