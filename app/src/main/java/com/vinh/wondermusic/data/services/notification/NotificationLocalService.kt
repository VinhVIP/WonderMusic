package com.vinh.wondermusic.data.services.notification

import com.vinh.wondermusic.data.database.daos.NotificationDAO
import com.vinh.wondermusic.data.database.entities.NotificationEntity
import javax.inject.Inject

class NotificationLocalService @Inject constructor(
    private val notificationDAO: NotificationDAO
) {

    suspend fun getListNotifications(): List<NotificationEntity> {
        return notificationDAO.getListNotifications()
    }

    suspend fun saveListNotifications(notifications: List<NotificationEntity>) {
        notificationDAO.insertListNotification(notifications)
    }

    suspend fun deleteListNotifications() {
        notificationDAO.deleteListNotifications()
    }

    suspend fun readNotification(notification: NotificationEntity) {
        notification.notificationStatus = 1
        notificationDAO.updateNotification(notification)
    }

    suspend fun deleteNotification(notification: NotificationEntity) {
        notificationDAO.deleteNotification(notification)
    }

    suspend fun readAllNotification() {
        notificationDAO.readAllNotification()
    }

    suspend fun deleteAllNotification() {
        notificationDAO.deleteListNotifications()
    }
}