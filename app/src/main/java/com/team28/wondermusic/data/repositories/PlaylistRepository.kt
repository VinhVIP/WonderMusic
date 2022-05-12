package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.toListPlaylist
import com.team28.wondermusic.data.services.playlist.PlaylistModal
import com.team28.wondermusic.data.services.playlist.PlaylistRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    private val remoteService: PlaylistRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun addPlaylist(modal: PlaylistModal): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.addPlaylist(modal)
        }
    }

    suspend fun updatePlaylist(idPlaylist: Int, modal: PlaylistModal): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.updatePlaylist(idPlaylist, modal)
        }
    }

    suspend fun deletePlaylist(idPlaylist: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.deletePlaylist(idPlaylist)
        }
    }

    suspend fun getMyPlaylists(): List<Playlist> = withContext(dispatcher) {
        val list = remoteService.getMyPlaylists()
        list?.toListPlaylist() ?: emptyList()
    }

    suspend fun getTopPlaylists(): List<Playlist> = withContext(dispatcher) {
        val list = remoteService.getTopPlaylists()
        list?.toListPlaylist() ?: emptyList()
    }
}