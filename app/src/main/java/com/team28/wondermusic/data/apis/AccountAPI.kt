package com.team28.wondermusic.data.apis

import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountAPI {

    @POST("$ApiVersion/account/login")
    suspend fun login(@Body modal: LoginModal): Response<LoginResponseJson>

    @GET("$ApiVersion/account/{idAccount}")
    suspend fun getAccount(
        @Path("idAccount") idAccount: Int
    ): Response<AccountJson>

    @GET("$ApiVersion/account/{idAccount}/songs")
    suspend fun getSongsOfAccount(
        @Path("idAccount") idAccount: Int,
    ): Response<ListSongJson>

    @GET("$ApiVersion/account/songs")
    suspend fun getMySongs(): Response<ListSongJson>

    @GET("$ApiVersion/account/{idAccount}/follower")
    suspend fun getFollowers(
        @Path("idAccount") idAccount: Int
    ): Response<ListAccountJson>

    @GET("$ApiVersion/account/{idAccount}/following")
    suspend fun getFollowings(
        @Path("idAccount") idAccount: Int
    ): Response<ListAccountJson>

    @GET("$ApiVersion/account/{idAccount}/album")
    suspend fun getAlbumsOfAccount(
        @Path("idAccount") idAccount: Int,
    ): Response<ListAlbumJson>

    @GET("$ApiVersion/account/{idAccount}/playlist")
    suspend fun getPlaylistsOfAccount(
        @Path("idAccount") idAccount: Int,
    ): Response<ListPlaylistJson>
}