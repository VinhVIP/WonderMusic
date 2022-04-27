package com.team28.wondermusic.data.apis

import com.team28.wondermusic.base.network.NetworkHelper
import com.team28.wondermusic.common.Config
import com.team28.wondermusic.data.models.ListPlaylistJson
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.services.playlist.PlaylistModal
import retrofit2.Response
import retrofit2.http.*

interface PlaylistAPI {

    // Thêm playlist mới
    @POST(Config.ApiVersion + "/playlist")
    suspend fun addPlaylist(
        @Body modal: PlaylistModal,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>

    // Chỉnh sửa playlist
    @PUT(Config.ApiVersion + "/playlist/{idPlaylist}")
    suspend fun updatePlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Body modal: PlaylistModal,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>

    // Lấy tất cả playlist của bản thân
    @GET(Config.ApiVersion + "/playlist")
    suspend fun getMyPlaylists(
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<ListPlaylistJson>

    // Xóa 1 playlist
    @DELETE(Config.ApiVersion + "/playlist/{idPlaylist}")
    suspend fun deletePlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>
}