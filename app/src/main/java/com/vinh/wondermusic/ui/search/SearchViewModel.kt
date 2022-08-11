package com.vinh.wondermusic.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.database.entities.SearchHistoryEntity
import com.vinh.wondermusic.data.models.*
import com.vinh.wondermusic.data.repositories.SearchHistoryRepository
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val searchHistoryRepository: SearchHistoryRepository
) : BaseViewModel() {

    val isSearchDone = MutableLiveData(false)

    val searchHistoryList = MutableLiveData<List<SearchHistoryEntity>>()

    val songs = MutableLiveData<List<Song>>()
    val playlists = MutableLiveData<List<Playlist>>()
    val accounts = MutableLiveData<List<Account>>()


    fun getSearchHistory() {
        viewModelScope.launch {
            searchHistoryList.postValue(searchHistoryRepository.getAllSearchHistory())
        }
    }

    fun saveSearchKeyword(keyword: String) {
        viewModelScope.launch {
            searchHistoryRepository.insert(SearchHistoryEntity(keyword, Date()))
        }
    }

    fun search(keyword: String) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = songRepository.search(keyword)
            result?.let {
                songs.postValue(it.songs.toListSong())
                playlists.postValue(it.playlists.toListPlaylist())
                accounts.postValue(it.accounts.toListAccount())
            }
        }
        isSearchDone.postValue(true)
        registerEventParentJobFinish()
    }
}