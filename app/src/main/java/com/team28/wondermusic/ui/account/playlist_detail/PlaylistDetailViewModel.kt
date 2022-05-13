package com.team28.wondermusic.ui.account.playlist_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.repositories.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : BaseViewModel() {

    var message: String? = null
    var removeSongFromPlaylistStatus = MutableLiveData<Boolean?>(null)
    var removePosition: Int = -1

    fun removeSongFromPlaylist(idPlaylist: Int, idSong: Int) {
        viewModelScope.launch {
            val result = playlistRepository.deleteSongFromPlaylist(idPlaylist, idSong)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            removeSongFromPlaylistStatus.postValue(result is NetworkResult.Success)
        }
    }
}