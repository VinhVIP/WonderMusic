package com.team28.wondermusic.ui.home.personal.followings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowingsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    var followings = MutableLiveData<List<Account>>()

    fun getFollowings(idAccount: Int) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            followings.postValue(accountRepository.getFollowings(idAccount))
        }
        registerEventParentJobFinish()
    }
}