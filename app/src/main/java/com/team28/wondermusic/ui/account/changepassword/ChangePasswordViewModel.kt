package com.team28.wondermusic.ui.account.changepassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.ChangePasswordModal
import com.team28.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    var message: String? = null
    var status = MutableLiveData<Boolean?>(null)

    fun changePassword(modal: ChangePasswordModal) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = accountRepository.changePassword(modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            status.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }
}