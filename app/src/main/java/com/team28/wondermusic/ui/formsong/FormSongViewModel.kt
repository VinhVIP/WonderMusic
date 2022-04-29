package com.team28.wondermusic.ui.formsong

import androidx.lifecycle.MutableLiveData
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File

class FormSongViewModel : BaseViewModel() {

    var songFile: File? = null
    var songName = ""
    var description = ""
    var imageFile: File? = null
    val album: MutableLiveData<Album> = MutableLiveData()
    val singers: MutableLiveData<List<Account>> = MutableLiveData()
    var lyric: String = ""
    var songStatus = 0

}