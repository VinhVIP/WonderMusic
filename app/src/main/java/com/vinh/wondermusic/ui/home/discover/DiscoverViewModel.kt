package com.vinh.wondermusic.ui.home.discover

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Account
import com.vinh.wondermusic.data.models.Playlist
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.data.repositories.AccountRepository
import com.vinh.wondermusic.data.repositories.PlaylistRepository
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    var types = MutableLiveData<List<Type>>()
    var newestSongs = MutableLiveData<List<Song>>()
    var followSongs = MutableLiveData<List<Song>>()
    var bestSongs = MutableLiveData<List<Song>>()
    var topPlaylists = MutableLiveData<List<Playlist>>()
    var topAccounts = MutableLiveData<List<Account>>()

    fun getTypes() {
        viewModelScope.launch {
            types.postValue(songRepository.getAllTypes())
        }
    }

    fun getNewestSongs(page: Int) {
        viewModelScope.launch {
            val list = songRepository.getNewestSongs(page)
            newestSongs.postValue(list)
        }
    }

    fun getFollowSongs(page: Int) {
        viewModelScope.launch {
            val list = songRepository.getSongsOfFollowing(page)
            followSongs.postValue(list)
        }
    }

    fun getBestSongs() {
        viewModelScope.launch {
            var list = songRepository.getBestSongs()
            if (list.size > 10) list = list.subList(0, 9)
            bestSongs.postValue(list)
        }
    }

    fun getTopPlaylists() {
        viewModelScope.launch {
            topPlaylists.postValue(playlistRepository.getTopPlaylists())
        }
    }

    fun getTopAccounts() {
        viewModelScope.launch {
            topAccounts.postValue(accountRepository.getTopAccounts())
        }
    }
}