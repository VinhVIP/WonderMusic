package com.team28.wondermusic.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.common.AppSharedPreferences
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val appSharedPreferences: AppSharedPreferences
) : BaseViewModel() {

    var message: String? = null
    lateinit var dataAccount: Account
    var account = MutableLiveData<Account>()
    var followResponseStatus = MutableLiveData<Boolean?>(null)

    var songs = MutableLiveData<List<Song>>()
    var albums = MutableLiveData<List<Album>>()
    var playlists = MutableLiveData<List<Playlist>>()

    var updateStatus = MutableLiveData(false)

    fun logout() {
        appSharedPreferences.logOut()
    }

    fun getAccountInfo() {
        viewModelScope.launch {
            val acc = accountRepository.getAccount(dataAccount.idAccount)
            acc?.let {
                dataAccount = it
                account.postValue(it)

                if (it.idAccount == DataLocal.myAccount.idAccount) {
                    DataLocal.myAccount = it
                    appSharedPreferences.saveUser(it)
                }
            }
        }
    }

    fun followAccount() {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = accountRepository.followAccount(dataAccount.idAccount)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            followResponseStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun unFollowAccount() {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = accountRepository.unFollowAccount(dataAccount.idAccount)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            followResponseStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun getSongsOfAccount() {
        viewModelScope.launch {
            songs.postValue(accountRepository.getSongsOfAccount(dataAccount.idAccount))
        }
    }

    fun getAlbumsOfAccount() {
        viewModelScope.launch {
            albums.postValue(accountRepository.getAlbumsOfAccount(dataAccount.idAccount))
        }
    }

    fun getPlaylistsOfAccount() {
        viewModelScope.launch {
            playlists.postValue(accountRepository.getPlaylistsOfAccount(dataAccount.idAccount))
        }
    }
}