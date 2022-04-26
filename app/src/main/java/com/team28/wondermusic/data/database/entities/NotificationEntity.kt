package com.team28.wondermusic.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Notification

@Entity
data class NotificationEntity(
    @PrimaryKey val idNotification: Int,
    val content: String,
    val action: String,
    var notificationStatus: Int,
    val notificationTime: String,
    val idAccount: Int? = null,
    val accountName: String? = null
) {
    fun toNotification(): Notification {
        return Notification(
            idNotification = this.idNotification,
            content = this.content,
            action = this.action,
            notificationStatus = this.notificationStatus,
            notificationTime = this.notificationTime,
            account = Account(
                idAccount = this.idAccount ?: 0,
                accountName = this.accountName ?: "",
            )
        )
    }
}

fun List<NotificationEntity>.toListNotification(): List<Notification> {
    return map { it.toNotification() }
}