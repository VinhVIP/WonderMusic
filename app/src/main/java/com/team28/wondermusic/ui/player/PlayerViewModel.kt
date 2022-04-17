package com.team28.wondermusic.ui.player

import androidx.lifecycle.MutableLiveData
import com.team28.wondermusic.base.BaseViewModel
import com.team28.wondermusic.data.models.Song

class PlayerViewModel : BaseViewModel() {

    var isClear: Boolean = false

    var isPlaying: MutableLiveData<Boolean> = MutableLiveData(false)

    var song: MutableLiveData<Song> = MutableLiveData()

    var songList: MutableLiveData<ArrayList<Song>> = MutableLiveData()

    var songListOrigin: MutableLiveData<ArrayList<Song>> = MutableLiveData()

    var currentSongTime: MutableLiveData<Int> = MutableLiveData(0)

    var isUserTouchedSlider = false

    var currentRotate = 0f

}