package com.vinh.wondermusic.ui.statistic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    val listSongs = MutableLiveData<List<Song>>()

    fun getTopListenInRange(
        startDate: String,
        endDate: String,
        isAll: Boolean = false,
        isListen: Boolean = true,
    ) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            listSongs.postValue(
                songRepository.getTopListenInRangeDate(
                    startDate,
                    endDate,
                    if (isAll) 1 else 0,
                    if (isListen) "listen" else "love",
                )
            )
        }
        registerEventParentJobFinish()
    }
}