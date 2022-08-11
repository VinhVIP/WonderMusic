package com.vinh.wondermusic.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.common.AppSharedPreferences
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.repositories.AccountRepository
import com.vinh.wondermusic.data.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val accountRepository: AccountRepository,
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    var song: MutableLiveData<Song?> = MutableLiveData()
    var isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)

    var numUnreadNotification = MutableLiveData(0)

    init {
        countUnreadNotification()
        getSettings()
    }

    private fun getSettings(){
        viewModelScope.launch {
            DataLocal.IS_SHUFFLE = appSharedPreferences.isShuffle()
            DataLocal.IS_REPEAT = appSharedPreferences.isRepeat()
        }
    }

    fun countUnreadNotification() {
        viewModelScope.launch {
            numUnreadNotification.postValue(notificationRepository.countUnreadNotification())
        }
    }

    fun sendAccountDevice(deviceToken: String) {
        viewModelScope.launch {
            accountRepository.sendAccountDevice(deviceToken)
        }
    }

}