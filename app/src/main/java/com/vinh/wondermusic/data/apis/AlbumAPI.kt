package com.vinh.wondermusic.data.apis

import com.vinh.wondermusic.common.Config.ApiVersion
import com.vinh.wondermusic.data.models.ListAlbumJson
import com.vinh.wondermusic.data.models.ListSongJson
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.services.album.AlbumModal
import retrofit2.Response
import retrofit2.http.*

interface AlbumAPI {

    @POST("$ApiVersion/album")
    suspend fun addAlbum(@Body modal: AlbumModal): Response<MessageJson>

    @PUT("$ApiVersion/album/{idAlbum}")
    suspend fun updateAlbum(
        @Path("idAlbum") idAlbum: Int,
        @Body modal: AlbumModal,
    ): Response<MessageJson>

    @DELETE("$ApiVersion/album/{idAlbum}")
    suspend fun deleteAlbum(@Path("idAlbum") idAlbum: Int): Response<MessageJson>

    @GET("$ApiVersion/album/all")
    suspend fun getAllMyAlbum(): Response<ListAlbumJson>

    @GET("$ApiVersion/album/{idAlbum}/songs")
    suspend fun getSongsOfAlbum(
        @Path("idAlbum") idType: Int
    ): Response<ListSongJson>
}