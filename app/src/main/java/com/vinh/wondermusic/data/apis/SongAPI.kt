package com.vinh.wondermusic.data.apis

import com.vinh.wondermusic.common.Config.ApiVersion
import com.vinh.wondermusic.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface SongAPI {

    @GET("$ApiVersion/search")
    suspend fun search(@Query("k") keyword: String): Response<SearchResponse>

    @Multipart
    @POST("$ApiVersion/song")
    suspend fun addSong(
        @Part songFile: MultipartBody.Part,
        @Part img: MultipartBody.Part?,
        @Part("name_song") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lyrics") lyrics: RequestBody,
        @Part("id_album") idAlbum: Int,
        @Part("types") types: ArrayList<Int>,
        @Part("accounts") accounts: ArrayList<Int>,
    ): Response<MessageJson>

    @Multipart
    @PUT("$ApiVersion/song/{idSong}")
    suspend fun updateSong(
        @Path("idSong") idSong: Int,
//        @Part songFile: MultipartBody.Part?,
        @Part img: MultipartBody.Part?,
        @Part("name_song") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("lyrics") lyrics: RequestBody,
        @Part("id_album") idAlbum: Int,
        @Part("types") types: ArrayList<Int>,
        @Part("accounts") accounts: ArrayList<Int>,
    ): Response<MessageJson>

    @DELETE("$ApiVersion/song/{idSong}")
    suspend fun deleteSong(@Path("idSong") idSong: Int): Response<MessageJson>

    @POST("$ApiVersion/song/listen/{idSong}")
    suspend fun listen(@Path("idSong") idSong: Int): Response<MessageJson>

    @GET("$ApiVersion/song/{idSong}")
    suspend fun getSong(@Path("idSong") idSong: Int): Response<SingleSongJson>

    @GET("$ApiVersion/type/all")
    suspend fun getAllTypes(): Response<ListTypeJson>

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

    @GET("$ApiVersion/song/top-100")
    suspend fun getTopTenSongs(): Response<ListTopListenSongs>

    @GET("$ApiVersion/song/top-100")
    suspend fun getTop100Songs(): Response<ListSongJson>

    @GET("$ApiVersion/song/top-3-listen")
    suspend fun getTop3Songs(): Response<ListTopListenSongs>

    @GET("$ApiVersion/account/songs_following")
    suspend fun getSongsOfFollowing(@Query("page") page: Int): Response<ListSongJson>

    @GET("$ApiVersion/love/love-song")
    suspend fun getLoveSongs(
        @Query("page") page: Int,
    ): Response<ListSongJson>

    @POST("$ApiVersion/love/{idSong}")
    suspend fun loveSong(
        @Path("idSong") idSong: Int,
    ): Response<MessageJson>

    @DELETE("$ApiVersion/love/{idSong}")
    suspend fun unLoveSong(
        @Path("idSong") idSong: Int,
    ): Response<MessageJson>

}