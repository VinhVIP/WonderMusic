package com.team28.wondermusic.ui.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Notification
import com.team28.wondermusic.data.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : BaseViewModel() {

    var message = MutableLiveData<String?>(null)

    var notifications = MutableLiveData<List<Notification>>()
    var readStatus = MutableLiveData(false)
    var deleteStatus = MutableLiveData(false)
    var readAllStatus = MutableLiveData(false)
    var deleteAllStatus = MutableLiveData(false)
    var changePosition: Int? = null

    fun getAllNotifications() {
        parentJob = viewModelScope.launch(exceptionHandler) {
            val allNotifications = notificationRepository.getAllNotifications()
            allNotifications?.let {
                notifications.postValue(it)
            }
        }
    }

    fun refreshNotifications() {
        parentJob = viewModelScope.launch(exceptionHandler) {
            notifications.postValue(notificationRepository.refresh())
        }
    }

    fun readNotification(notification: Notification) {
        parentJob = viewModelScope.launch {
            val result = notificationRepository.readNotification(notification)
            if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
                readStatus.postValue(false)
            }
            readStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun deleteNotification(notification: Notification) {
        parentJob = viewModelScope.launch {
            val result = notificationRepository.deleteNotification(notification)
            if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
                deleteStatus.postValue(false)
            }
            deleteStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun readAllNotification() {
        parentJob = viewModelScope.launch {
            val result = notificationRepository.readAllNotification()
            if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
            }
            readAllStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun deleteAllNotification() {
        parentJob = viewModelScope.launch {
            val result = notificationRepository.deleteAllNotification()
            if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
            }
            deleteAllStatus.postValue(result is NetworkResult.Success)
        }
    }
}