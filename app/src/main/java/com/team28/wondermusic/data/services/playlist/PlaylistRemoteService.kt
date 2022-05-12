package com.team28.wondermusic.data.services.playlist

import com.squareup.moshi.Json
import com.team28.wondermusic.base.network.BaseRemoteService
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.apis.PlaylistAPI
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.models.PlaylistJson
import javax.inject.Inject

class PlaylistRemoteService @Inject constructor(
    private val playlistAPI: PlaylistAPI
) : BaseRemoteService() {

    suspend fun addPlaylist(modal: PlaylistModal): NetworkResult<MessageJson> {
        return callApi { playlistAPI.addPlaylist(modal) }
    }

    suspend fun updatePlaylist(idPlaylist: Int, modal: PlaylistModal): NetworkResult<MessageJson> {
        return callApi { playlistAPI.updatePlaylist(idPlaylist, modal) }
    }

    suspend fun deletePlaylist(idPlaylist: Int): NetworkResult<MessageJson> {
        return callApi { playlistAPI.deletePlaylist(idPlaylist) }
    }

    suspend fun getMyPlaylists(): List<PlaylistJson>? {
        val response = callApi { playlistAPI.getMyPlaylists() }
        return if (response is NetworkResult.Success)
            response.body.data
        else null
    }

    suspend fun getTopPlaylists(): List<PlaylistJson>? {
        val response = callApi { playlistAPI.getTopPlaylists() }
        return if (response is NetworkResult.Success)
            response.body.data
        else null
    }

}

// Modal dùng để gửi request body khi thêm/sửa
data class PlaylistModal(
    @Json(name = "name_playlist") val namePlaylist: String,
    @Json(name = "playlist_status") val playlistStatus: Int
)