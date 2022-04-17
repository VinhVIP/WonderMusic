package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.data.entities.Post
import com.team28.wondermusic.data.services.PostRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val postRemoteService: PostRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getPosts(): List<Post>? = withContext(dispatcher) {
        postRemoteService.getPosts()
    }
}