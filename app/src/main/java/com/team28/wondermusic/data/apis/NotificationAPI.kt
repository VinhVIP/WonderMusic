package com.team28.wondermusic.data.apis

import com.team28.wondermusic.common.Config.ApiVersion
import com.team28.wondermusic.data.models.MessageJson
import com.team28.wondermusic.data.models.NotificationCountJson
import com.team28.wondermusic.data.models.NotificationListJson
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationAPI {

    // Lấy tất cả thông báo
    @GET("$ApiVersion/notification/all")
    suspend fun getAllNotifications(): Response<NotificationListJson>


    // Đánh dấu 1 thông báo đã đọc
    @GET("$ApiVersion/notification/{idNotification}/read")
    suspend fun readNotification(
        @Path("idNotification") idNotification: Int,
    ): Response<MessageJson>


    // Đánh dấu đã đọc tất cả thông báo
    @GET("$ApiVersion/notification/read_all")
    suspend fun readAllNotification(): Response<MessageJson>


    // Xóa 1 thông báo
    @DELETE("$ApiVersion/notification/{idNotification}/delete")
    suspend fun deleteNotification(
        @Path("idNotification") idNotification: Int,
    ): Response<MessageJson>


    // Xóa tất cả thông báo
    @GET("$ApiVersion/notification/delete_all")
    suspend fun deleteAllNotification(): Response<MessageJson>

    // Lấy số lượng thông báo chưa đọc
    @GET("$ApiVersion/notification/count")
    suspend fun countUnreadNotification(): Response<NotificationCountJson>
}