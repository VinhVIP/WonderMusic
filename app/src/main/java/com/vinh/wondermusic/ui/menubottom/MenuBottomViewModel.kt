package com.vinh.wondermusic.ui.menubottom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuBottomViewModel @Inject constructor(
    private val songRepository: SongRepository,
) : BaseViewModel() {

    var position: Int? = null

    var message: String? = null
    var actionLoveStatus = MutableLiveData<Boolean?>(null)

    fun loveSong(song: Song) {
        viewModelScope.launch {
            val result = songRepository.loveSong(song.idSong)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            actionLoveStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun unLoveSong(song: Song) {
        viewModelScope.launch {
            val result = songRepository.unLoveSong(song.idSong)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            actionLoveStatus.postValue(result is NetworkResult.Success)
        }
    }
}