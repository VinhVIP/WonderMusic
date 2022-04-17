package com.team28.wondermusic.ui.home

import androidx.lifecycle.MutableLiveData
import com.team28.wondermusic.base.BaseViewModel
import com.team28.wondermusic.data.models.Song

class HomeViewModel : BaseViewModel() {

    var song: MutableLiveData<Song?> = MutableLiveData()
    var isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)
}