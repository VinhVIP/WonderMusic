package com.team28.wondermusic.ui.comment.reply

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentReplyViewModel @Inject constructor(
    private val commentRepository: CommentRepository
) : BaseViewModel() {

    var parentComment = MutableLiveData<Comment>()
    var message: String? = null

    var commentUpdate: Comment? = null

    var sendStatus = MutableLiveData<Boolean?>(null)
    var deleteStatus = MutableLiveData<Boolean?>(null)

    fun getCommentAndChildren(comment: Comment) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            parentComment.postValue(commentRepository.getCommentAndChildren(comment.idComment))
        }
        registerEventParentJobFinish()
    }

    fun replyComment(song: Song, comment: Comment, content: String) {
        viewModelScope.launch {
            val result = commentRepository.replayComment(song.idSong, comment.idComment, content)
            if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            sendStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun updateComment(song: Song, comment: Comment, content: String) {
        viewModelScope.launch {
            val result = commentRepository.updateComment(song.idSong, comment.idComment, content)
            if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            sendStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun deleteComment(song: Song, comment: Comment) {
        viewModelScope.launch {
            val result = commentRepository.deleteComment(song.idSong, comment.idComment)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            deleteStatus.postValue(result is NetworkResult.Success)
        }
    }

}