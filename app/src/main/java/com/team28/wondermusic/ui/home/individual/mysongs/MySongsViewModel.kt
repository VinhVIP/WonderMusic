package com.team28.wondermusic.ui.home.individual.mysongs

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
class MySongsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {
    var mySongs = MutableLiveData<List<Song>>()

    fun fetchData() {
        parentJob = viewModelScope.launch {
            mySongs.postValue(songRepository.getListMySongs())
        }
        registerEventParentJobFinish()
    }

    fun genData() {
        viewModelScope.launch {
            songRepository.saveListMySongs(
                listOf(
                    TempData.songs[2],
                    TempData.songs[4],
                    TempData.songs[7],
                )
            )
            fetchData()
        }
    }
}