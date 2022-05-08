package com.team28.wondermusic.ui.home.personal.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.repositories.PlaylistRepository
import com.team28.wondermusic.data.services.playlist.PlaylistModal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : BaseViewModel() {

    var message = MutableLiveData<String?>(null)

    var playlists = MutableLiveData<List<Playlist>>()
    var updateStatus: MutableLiveData<Boolean?> = MutableLiveData()
    var addStatus: MutableLiveData<Boolean?> = MutableLiveData()
    var deleteStatus: MutableLiveData<Boolean?> = MutableLiveData()
    var changePosition: Int? = null

    var playlistUpdate: Playlist? = null

    fun getMyPlaylists() {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            playlists.postValue(playlistRepository.getMyPlaylists())
        }
        registerEventParentJobFinish()
    }

    fun addPlaylist(modal: PlaylistModal) {
        parentJob = viewModelScope.launch {
            val result = playlistRepository.addPlaylist(modal)
            if (result is NetworkResult.Success) {
                message.postValue(result.body.message)
            } else if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
            }
            addStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun updatePlaylist(idPlaylist: Int, position: Int, modal: PlaylistModal) {
        parentJob = viewModelScope.launch {
            val result = playlistRepository.updatePlaylist(idPlaylist, modal)
            if (result is NetworkResult.Success) {
                message.postValue(result.body.message)
            } else if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
            }

            changePosition = position
            updateStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun deletePlaylist(playlist: Playlist, position: Int) {
        viewModelScope.launch {
            val result = playlistRepository.deletePlaylist(playlist.idPlaylist)
            if (result is NetworkResult.Success) {
                message.postValue(result.body.message)
            } else if (result is NetworkResult.Error) {
                message.postValue(result.responseError.message)
            }

            changePosition = position
            deleteStatus.postValue(result is NetworkResult.Success)
        }
    }
}