package com.team28.wondermusic.ui.home.highlight

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighLightViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    var topSongs = MutableLiveData<List<Song>>()

    fun fetchData() {
        parentJob = viewModelScope.launch {
            topSongs.postValue(songRepository.getListTopSongs())
        }
        registerEventParentJobFinish()
    }

    fun genData() {
        viewModelScope.launch {
            songRepository.saveListTopSongs(
                listOf(
                    TempData.songs[0],
                    TempData.songs[1],
                    TempData.songs[2],
                )
            )
            fetchData()
        }
    }
}