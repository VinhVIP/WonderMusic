package com.vinh.wondermusic.data.repositories

import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.models.Playlist
import com.vinh.wondermusic.data.models.toListPlaylist
import com.vinh.wondermusic.data.services.playlist.PlaylistModal
import com.vinh.wondermusic.data.services.playlist.PlaylistRemoteService
import com.vinh.wondermusic.di.IoDispatcher
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

    suspend fun addSongToPlaylist(idPlaylist: Int, idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.addSongToPlaylist(idPlaylist, idSong)
        }
    }

    suspend fun deleteSongFromPlaylist(idPlaylist: Int, idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.deleteSongFromPlaylist(idPlaylist, idSong)
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