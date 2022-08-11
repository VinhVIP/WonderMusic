package com.vinh.wondermusic.data.services.album

import com.vinh.wondermusic.data.database.daos.AlbumDao
import com.vinh.wondermusic.data.database.entities.AlbumEntity
import javax.inject.Inject

class AlbumLocalService @Inject constructor(
    private val albumDao: AlbumDao
) {

    suspend fun getAllAlbum(): List<AlbumEntity> {
        return albumDao.getAllAlbum()
    }

    suspend fun saveAlbum(albumEntity: AlbumEntity) {
        return albumDao.insertAlbum(albumEntity)
    }

    suspend fun deleteAllAlbum() {
        return albumDao.deleteAllAlbum()
    }
}