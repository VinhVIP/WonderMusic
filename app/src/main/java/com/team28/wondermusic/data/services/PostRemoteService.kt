package com.team28.wondermusic.data.services

import com.team28.wondermusic.data.apis.PostAPI
import com.team28.wondermusic.data.entities.Post
import javax.inject.Inject

class PostRemoteService @Inject constructor(private val postAPI: PostAPI) {

    suspend fun getPosts(): List<Post>? {
        val response = postAPI.getPosts()
        if (response.isSuccessful) {
            return response.body()
        } else {
            throw Exception(response.message())
        }
    }
}