package com.team28.wondermusic.ui.home.individual.favoritesongs

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
class FavoriteSongsViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {
    var favoriteSongs = MutableLiveData<List<Song>>()

    fun fetchData() {
        parentJob = viewModelScope.launch {
            favoriteSongs.postValue(songRepository.getListFavoriteSongs())
        }
        registerEventParentJobFinish()
    }

    fun genData() {
        viewModelScope.launch {
            songRepository.saveListFavoriteSongs(
                listOf(
                    TempData.songs[0],
                    TempData.songs[6],
                    TempData.songs[2],
                    TempData.songs[5],
                )
            )
            fetchData()
        }
    }
}