package com.team28.wondermusic.ui.formsong

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team28.wondermusic.data.models.Album

class FormSongViewModel : ViewModel() {

    val album: MutableLiveData<Album> = MutableLiveData()

}