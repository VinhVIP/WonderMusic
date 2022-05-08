package com.team28.wondermusic.ui.splash

import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.common.AppSharedPreferences
import com.team28.wondermusic.common.DataLocal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    fun getSavedData() {
        DataLocal.IS_LOGGED_IN = appSharedPreferences.isLoggedIn()
        DataLocal.ACCESS_TOKEN = appSharedPreferences.getAccessToken()
        appSharedPreferences.getUser()?.let {
            DataLocal.myAccount = it
        }
    }
}