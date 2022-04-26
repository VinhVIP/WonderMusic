package com.team28.wondermusic.data.models

import com.squareup.moshi.Json

data class NotificationListJson(
    val message: String,
    val data: List<NotificationJson>? = emptyList(),
)

data class NotificationJson(
    @Json(name = "id_notification") val idNotification: Int,
    val content: String,
    val action: String,
    @Json(name = "id_account") val idAccount: Int,
    @Json(name = "notification_status") val status: Int,
    val day: String,
    val time: String
) {
    fun toNotification(): Notification {
        return Notification(
            idNotification = this.idNotification,
            content = this.content,
            action = this.action,
            notificationStatus = this.status,
            notificationTime = "$time - $day",
            // TODO: Thiếu account
            account = Account(idAccount = this.idAccount)
        )
    }
}

fun List<NotificationJson>.toListNotification(): List<Notification> {
    return map { it.toNotification() }
}

data class MessageJson(val message: String)

data class NotificationCountJson(
    val message: String,
    val data: Int,
)