package com.team28.wondermusic.data.apis

import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.ListSongJson
import com.team28.wondermusic.data.models.MessageJson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SongAPI {

    @Multipart
    @POST("$ApiVersion/song")
    suspend fun addSong(
        @Part songFile: MultipartBody.Part,
        @Part img: MultipartBody.Part,
        @Part("name_song") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lyrics") lyrics: RequestBody,
        @Part("id_album") idAlbum: Int,
        @Part("types") types: ArrayList<Int>,
        @Part("accounts") accounts: ArrayList<Int>,
    ): Response<MessageJson>

    @GET("$ApiVersion/song/new-list")
    suspend fun getNewestSongs(
        @Query("page") page: Int,
    ): Response<ListSongJson>

    @GET("$ApiVersion/song/best-list")
    suspend fun getBestSongs(): Response<ListSongJson>

    @GET("$ApiVersion/song/type/{idType}")
    suspend fun getSongsOfType(
        @Path("idType") idType: Int,
        @Query("page") page: Int,
    ): Response<ListSongJson>

    @GET("$ApiVersion/love/love-song")
    suspend fun getLoveSongs(
        @Query("page") page: Int,
    ): Response<ListSongJson>

    @POST("$ApiVersion/love/follow/{idSong}")
    suspend fun loveSong(
        @Path("idSong") idSong: Int,
    ): Response<MessageJson>

    @DELETE("$ApiVersion/love/unfollow/{idSong}")
    suspend fun unLoveSong(
        @Path("idSong") idSong: Int,
    ): Response<MessageJson>

}