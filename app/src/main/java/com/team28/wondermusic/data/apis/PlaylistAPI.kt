package com.team28.wondermusic.data.apis

import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.ListPlaylistJson
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.services.playlist.PlaylistModal
import retrofit2.Response
import retrofit2.http.*

interface PlaylistAPI {

    // Thêm playlist mới
    @POST("$ApiVersion/playlist")
    suspend fun addPlaylist(@Body modal: PlaylistModal): Response<MessageJson>

    // Chỉnh sửa playlist
    @PUT("$ApiVersion/playlist/{idPlaylist}")
    suspend fun updatePlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Body modal: PlaylistModal,
    ): Response<MessageJson>

    // Lấy tất cả playlist của bản thân
    @GET("$ApiVersion/playlist")
    suspend fun getMyPlaylists(): Response<ListPlaylistJson>

    // Lấy top playlist nổi bật
    @GET("$ApiVersion/playlist_song/prominent")
    suspend fun getTopPlaylists(): Response<ListPlaylistJson>

    // Xóa 1 playlist
    @DELETE("$ApiVersion/playlist/{idPlaylist}")
    suspend fun deletePlaylist(@Path("idPlaylist") idPlaylist: Int): Response<MessageJson>

    // Thêm bài hát vào playlist
    @POST("$ApiVersion/playlist_song/{idPlaylist}/song/{idSong}")
    suspend fun addSongToPlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Path("idSong") idSong: Int
    ): Response<MessageJson>

    // Xóa bài hát khỏi playlist
    @DELETE("$ApiVersion/playlist_song/{idPlaylist}/song/{idSong}")
    suspend fun deleteSongFromPlaylist(
        @Path("idPlaylist") idPlaylist: Int,
        @Path("idSong") idSong: Int
    ): Response<MessageJson>
}