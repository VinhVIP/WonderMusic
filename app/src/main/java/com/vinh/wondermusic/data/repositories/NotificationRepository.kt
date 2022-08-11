package com.vinh.wondermusic.data.repositories

import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.database.entities.toListNotification
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.models.Notification
import com.vinh.wondermusic.data.models.toListNotification
import com.vinh.wondermusic.data.models.toListNotificationEntity
import com.vinh.wondermusic.data.services.notification.NotificationLocalService
import com.vinh.wondermusic.data.services.notification.NotificationRemoteService
import com.vinh.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val remoteService: NotificationRemoteService,
    private val localService: NotificationLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getAllNotifications(): List<Notification>? = withContext(dispatcher) {
        val savedNotifications = localService.getListNotifications()

        if (savedNotifications.isNotEmpty()) {
            savedNotifications.toListNotification()
        } else {
            loadNewAndSave()
        }
    }

    suspend fun refresh(): List<Notification> = withContext(dispatcher) {
        loadNewAndSave()
    }

    private suspend fun loadNewAndSave(): List<Notification> {
        val notifications = remoteService.getAllNotifications()?.toListNotification()

        notifications?.let {
            localService.deleteListNotifications()
            localService.saveListNotifications(it.toListNotificationEntity())
        }

        return notifications ?: emptyList()
    }

    suspend fun readNotification(notification: Notification): NetworkResult<MessageJson> {
        val result = remoteService.readNotification(notification)
        if (result is NetworkResult.Success) {
            localService.readNotification(notification.toNotificationEntity())
        }
        return result
    }

    suspend fun deleteNotification(notification: Notification): NetworkResult<MessageJson> {
        val result = remoteService.deleteNotification(notification)
        if (result is NetworkResult.Success) {
            localService.deleteNotification(notification.toNotificationEntity())
        }
        return result
    }

    suspend fun readAllNotification(): NetworkResult<MessageJson> {
        val result = remoteService.readAllNotification()
        if (result is NetworkResult.Success) {
            localService.readAllNotification()
        }
        return result
    }

    suspend fun deleteAllNotification(): NetworkResult<MessageJson> {
        val result = remoteService.deleteAllNotification()
        if (result is NetworkResult.Success) {
            localService.deleteListNotifications()
        }
        return result
    }

    suspend fun countUnreadNotification(): Int {
        val result = remoteService.countUnreadNotification()
        if (result is NetworkResult.Success) {
            return result.body.data
        } else {
            return 0;
        }
    }
}