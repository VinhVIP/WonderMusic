package com.vinh.wondermusic.ui.home.personal.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Album
import com.vinh.wondermusic.data.repositories.AlbumRepository
import com.vinh.wondermusic.data.services.album.AlbumModal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val albumRepository: AlbumRepository
) : BaseViewModel() {

    var message: String? = null
    var changePosition: Int? = null

    var addStatus = MutableLiveData<Boolean?>(null)
    var updateStatus: MutableLiveData<Boolean?> = MutableLiveData()
    var deleteStatus: MutableLiveData<Boolean?> = MutableLiveData()

    var myAlbum = MutableLiveData<List<Album>>()
    var albumUpdate: Album? = null

    init {
        getAllMyAlbum()
    }

    fun addAlbum(modal: AlbumModal) {
        viewModelScope.launch {
            val result = albumRepository.addAlbum(modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            addStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun updateAlbum(idAlbum: Int, position: Int, modal: AlbumModal) {
        viewModelScope.launch {
            val result = albumRepository.updateAlbum(idAlbum, modal)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            changePosition = position
            updateStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun deleteAlbum(idAlbum: Int, position: Int) {
        viewModelScope.launch {
            val result = albumRepository.deleteAlbum(idAlbum)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            changePosition = position
            deleteStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun getAllMyAlbum() {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            myAlbum.postValue(albumRepository.getAllMyAlbum())
        }
        registerEventParentJobFinish()
    }
}