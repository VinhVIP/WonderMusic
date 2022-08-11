package com.vinh.wondermusic.ui.forgetpassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.ForgetChangeModal
import com.vinh.wondermusic.data.models.ForgetCodeModal
import com.vinh.wondermusic.data.models.ForgetEmailModal
import com.vinh.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    var message: String? = ""

    var actionNextStep = MutableLiveData(false)
    var currentStep = 0

    var email = ""
    var code = ""

    var loadingEmail = MutableLiveData(false)
    var loadingCode = MutableLiveData(false)
    var loadingChange = MutableLiveData(false)

    var emailStatus = MutableLiveData<Boolean?>(null)
    var codeStatus = MutableLiveData<Boolean?>(null)
    var changeStatus = MutableLiveData<Boolean?>(null)


    fun forgetEmail(modal: ForgetEmailModal) {
        Log.d("vinh", "email: ${modal.email}")
        loadingEmail.postValue(true)
        viewModelScope.launch {
            val result = accountRepository.forgetEmail(modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            emailStatus.postValue(result is NetworkResult.Success)
            loadingEmail.postValue(false)
        }
    }

    fun forgetCode(modal: ForgetCodeModal) {
        loadingCode.postValue(true)
        viewModelScope.launch {
            val result = accountRepository.forgetCode(modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            codeStatus.postValue(result is NetworkResult.Success)
            loadingCode.postValue(false)
        }
    }

    fun forgetChange(modal: ForgetChangeModal) {
        loadingChange.postValue(true)
        viewModelScope.launch {
            val result = accountRepository.forgetChangePassword(modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            changeStatus.postValue(result is NetworkResult.Success)
            loadingChange.postValue(false)
        }
    }
}