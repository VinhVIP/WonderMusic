package com.team28.wondermusic.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.common.AppSharedPreferences
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.data.models.LoginModal
import com.team28.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    var message: String? = null
    var isLoginSuccess = MutableLiveData<Boolean?>(null)

    fun getSavedLoginModal(): LoginModal? {
        return appSharedPreferences.getLoginModal()
    }

    fun login(modal: LoginModal) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = accountRepository.login(modal)
            if (result is NetworkResult.Success) {
                DataLocal.myAccount = result.body.data!!.toAccount()
                DataLocal.ACCESS_TOKEN = result.body.accessToken!!
                DataLocal.IS_LOGGED_IN = true

                appSharedPreferences.saveLogin(modal.email, modal.password)
                appSharedPreferences.saveAccessToken(DataLocal.ACCESS_TOKEN)
                appSharedPreferences.saveUser(DataLocal.myAccount)
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            isLoginSuccess.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }
}