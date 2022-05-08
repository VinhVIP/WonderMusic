package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.models.toListAlbum
import com.team28.wondermusic.data.services.album.AlbumModal
import com.team28.wondermusic.data.services.album.AlbumRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AlbumRepository @Inject constructor(
    private val remoteService: AlbumRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun addAlbum(modal: AlbumModal): NetworkResult<MessageJson> = withContext(dispatcher) {
        remoteService.addAlbum(modal)
    }

    suspend fun updateAlbum(idAlbum: Int, modal: AlbumModal): NetworkResult<MessageJson> =
        withContext(dispatcher) {
            remoteService.updateAlbum(idAlbum, modal)
        }

    suspend fun deleteAlbum(idAlbum: Int): NetworkResult<MessageJson> = withContext(dispatcher) {
        remoteService.deleteAlbum(idAlbum)
    }

    suspend fun getAllMyAlbum(): List<Album> = withContext(dispatcher) {
        val result = remoteService.getAllMyAlbum()
        if (result is NetworkResult.Success) {
            result.body.data.toListAlbum()
        } else {
            emptyList()
        }
    }

    suspend fun getSongsOfAlbum(idAlbum: Int): List<Song> = withContext(dispatcher) {
        remoteService.getSongsOfAlbum(idAlbum)
    }
}