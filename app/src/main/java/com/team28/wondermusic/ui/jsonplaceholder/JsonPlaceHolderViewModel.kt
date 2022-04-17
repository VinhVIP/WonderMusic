package com.team28.wondermusic.ui.jsonplaceholder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.BaseViewModel
import com.team28.wondermusic.data.entities.Post
import com.team28.wondermusic.data.repositories.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JsonPlaceHolderViewModel @Inject constructor(private val postRepository: PostRepository) :
    BaseViewModel() {

    var latestPost = MutableLiveData<Post>()
        protected set

    fun getAllPosts() {
        parentJob = viewModelScope.launch(exceptionHandler) {
            isLoading.postValue(true)
            val posts = postRepository.getPosts()
            posts?.firstOrNull()?.let { post ->
                latestPost.postValue(post)
            }
        }
        registerEventParentJobFinish()
    }
}