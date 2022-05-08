package com.team28.wondermusic.ui.account.album_detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : BaseViewModel() {

    var songs = MutableLiveData<List<Song>>()

    fun getSongsOfAlbum(idAlbum: Int) {
        isLoading.postValue(true)
        viewModelScope.launch {
            songs.postValue(albumRepository.getSongsOfAlbum(idAlbum))
        }
        registerEventParentJobFinish()
    }
}