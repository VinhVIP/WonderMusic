package com.team28.wondermusic.data.apis

import com.team28.wondermusic.base.network.NetworkHelper
import com.team28.wondermusic.common.Config
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.models.NotificationListJson
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface NotificationAPI {

    // Lấy tất cả thông báo
    @GET(Config.ApiVersion + "/notification/all")
    suspend fun getAllNotifications(
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<NotificationListJson>


    // Đánh dấu 1 thông báo đã đọc
    @GET(Config.ApiVersion + "/notification/{idNotification}/read")
    suspend fun readNotification(
        @Path("idNotification") idNotification: Int,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>


    // Đánh dấu đã đọc tất cả thông báo
    @GET(Config.ApiVersion + "/notification/read_all")
    suspend fun readAllNotification(
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>


    // Xóa 1 thông báo
    @DELETE(Config.ApiVersion + "/notification/{idNotification}/delete")
    suspend fun deleteNotification(
        @Path("idNotification") idNotification: Int,
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>


    // Xóa tất cả thông báo
    @GET(Config.ApiVersion + "/notification/delete_all")
    suspend fun deleteAllNotification(
        @HeaderMap header: Map<String, String> = NetworkHelper.getAuthorizationHeader()
    ): Response<MessageJson>
}