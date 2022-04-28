package com.team28.wondermusic.data.services.comment

import com.team28.wondermusic.base.network.BaseRemoteService
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.apis.CommentAPI
import com.team28.wondermusic.data.models.CommentJson
import com.team28.wondermusic.data.models.CommentParentJson
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.repositories.CommentModal
import javax.inject.Inject

class CommentRemoteService @Inject constructor(
    private val commentAPI: CommentAPI
) : BaseRemoteService() {

    suspend fun getAllComments(idSong: Int): List<CommentJson>? {
        val result = callApi { commentAPI.getAllComments(idSong) }
        return if (result is NetworkResult.Success)
            result.body.data
        else null
    }

    suspend fun getCommentAndChildren(idCmt: Int): CommentParentJson? {
        val result = callApi { commentAPI.getCommentAndChildren(idCmt) }
        return if (result is NetworkResult.Success)
            result.body.data
        else null
    }

    suspend fun addComment(idSong: Int, modal: CommentModal): NetworkResult<MessageJson> {
        return callApi { commentAPI.addComment(idSong, modal) }
    }

    suspend fun updateComment(
        idSong: Int,
        idCmt: Int,
        modal: CommentModal
    ): NetworkResult<MessageJson> {
        return callApi { commentAPI.updateComment(idSong, idCmt, modal) }
    }

    suspend fun deleteComment(
        idSong: Int,
        idCmt: Int,
    ): NetworkResult<MessageJson> {
        return callApi { commentAPI.deleteComment(idSong, idCmt) }
    }

    suspend fun replyComment(
        idSong: Int,
        idCmtParent: Int,
        modal: CommentModal
    ): NetworkResult<MessageJson> {
        return callApi { commentAPI.addReplyComment(idSong, idCmtParent, modal) }
    }
}