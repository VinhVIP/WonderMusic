package com.vinh.wondermusic.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.common.AppSharedPreferences
import com.vinh.wondermusic.data.models.SignupModal
import com.vinh.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    var message: String? = null
    var status = MutableLiveData<Boolean?>(null)

    fun signup(modal: SignupModal) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = accountRepository.signup(modal)
            if (result is NetworkResult.Success) {
                appSharedPreferences.saveLogin(modal.email, modal.password)
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }

            status.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }
}