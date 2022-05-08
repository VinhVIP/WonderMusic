package com.team28.wondermusic.ui.home.discover

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    var newestSongs = MutableLiveData<List<Song>>()
    var bestSongs = MutableLiveData<List<Song>>()

    fun getNewestSongs(page: Int) {
        viewModelScope.launch {
            val list = songRepository.getNewestSongs(page)
            newestSongs.postValue(list)
        }
    }

    fun getBestSongs() {
        viewModelScope.launch {
            var list = songRepository.getBestSongs()
            if (list.size > 10) list = list.subList(0, 9)
            bestSongs.postValue(list)
        }
    }
}