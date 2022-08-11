package com.vinh.wondermusic.data.repositories

import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.models.Album
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.toListAlbum
import com.vinh.wondermusic.data.services.album.AlbumModal
import com.vinh.wondermusic.data.services.album.AlbumRemoteService
import com.vinh.wondermusic.di.IoDispatcher
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