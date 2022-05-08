package com.team28.wondermusic.data.apis

import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.ListAlbumJson
import com.team28.wondermusic.data.models.ListSongJson
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.services.album.AlbumModal
import retrofit2.Response
import retrofit2.http.*

interface AlbumAPI {

    @POST("$ApiVersion/album")
    suspend fun addAlbum(@Body modal: AlbumModal): Response<MessageJson>

    @PUT("$ApiVersion/album/edit/{idAlbum}")
    suspend fun updateAlbum(
        @Path("idAlbum") idAlbum: Int,
        @Body modal: AlbumModal,
    ): Response<MessageJson>

    @DELETE("$ApiVersion/album/delete/{idAlbum}")
    suspend fun deleteAlbum(@Path("idAlbum") idAlbum: Int): Response<MessageJson>

    @GET("$ApiVersion/album/all")
    suspend fun getAllMyAlbum(): Response<ListAlbumJson>

    @GET("$ApiVersion/album/{idAlbum}/songs")
    suspend fun getSongsOfAlbum(
        @Path("idAlbum") idType: Int
    ): Response<ListSongJson>
}