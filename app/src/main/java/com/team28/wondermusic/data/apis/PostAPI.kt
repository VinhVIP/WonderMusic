package com.team28.wondermusic.data.apis

import com.team28.wondermusic.data.entities.Post
import retrofit2.Response
import retrofit2.http.GET

interface PostAPI {

    @GET("/posts")
    suspend fun getPosts():Response<List<Post>>
}