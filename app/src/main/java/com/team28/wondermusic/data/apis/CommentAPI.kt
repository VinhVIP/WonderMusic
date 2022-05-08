package com.team28.wondermusic.data.apis

import com.team28.wondermusic.base.network.NetworkHelper
import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.CommentParentAndChildrenJson
import com.team28.wondermusic.data.models.ListCommentJson
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.repositories.CommentModal
import retrofit2.Response
import retrofit2.http.*

interface CommentAPI {

    // Lấy tất cả comment thuộc 1 bài hát
    @GET("$ApiVersion/song/{idSong}/comment")
    suspend fun getAllComments(
        @Path("idSong") idSong: Int,
    ): Response<ListCommentJson>

    // Lấy 1 bình luận cha và ds bình luận con
    @GET("$ApiVersion/song/comment_parent/{idCmt}")
    suspend fun getCommentAndChildren(
        @Path("idCmt") idCmt: Int,
    ): Response<CommentParentAndChildrenJson>

    // Thêm bình luận cha vào 1 bài hát
    @POST("$ApiVersion/song/{idSong}/comment")
    suspend fun addComment(
        @Path("idSong") idSong: Int,
        @Body modal: CommentModal,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>

    // Chỉnh sửa bình luận
    @PUT("$ApiVersion/song/{idSong}/comment/{idCmt}/update")
    suspend fun updateComment(
        @Path("idSong") idSong: Int,
        @Path("idCmt") idCmt: Int,
        @Body modal: CommentModal,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>

    // Xóa bình luận
    @DELETE("$ApiVersion/song/{idSong}/comment/{idCmt}/delete")
    suspend fun deleteComment(
        @Path("idSong") idSong: Int,
        @Path("idCmt") idCmt: Int,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>

    // Trả lời 1 bình luận
    @POST("$ApiVersion/song/{idSong}/comment/{idCmtParent}/reply")
    suspend fun addReplyComment(
        @Path("idSong") idSong: Int,
        @Path("idCmtParent") idCmtParent: Int,
        @Body modal: CommentModal,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>
}