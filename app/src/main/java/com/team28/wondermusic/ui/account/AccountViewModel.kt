package com.team28.wondermusic.ui.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.common.AppSharedPreferences
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

    var account: Account? = null

    var songs = MutableLiveData<List<Song>>()
    var albums = MutableLiveData<List<Album>>()
    var playlists = MutableLiveData<List<Playlist>>()

    fun logout(){
        appSharedPreferences.logOut()
    }

    fun getSongsOfAccount() {
        viewModelScope.launch {
            account?.let {
                songs.postValue(accountRepository.getSongsOfAccount(it.idAccount))
            }
        }
    }

    fun getAlbumsOfAccount() {
        viewModelScope.launch {
            account?.let {
                albums.postValue(accountRepository.getAlbumsOfAccount(it.idAccount))
            }
        }
    }

    fun getPlaylistsOfAccount() {
        viewModelScope.launch {
            account?.let {
                playlists.postValue(accountRepository.getPlaylistsOfAccount(it.idAccount))
            }
        }
    }
}