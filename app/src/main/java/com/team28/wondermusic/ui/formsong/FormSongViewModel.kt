package com.team28.wondermusic.ui.formsong

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.*
import com.team28.wondermusic.data.repositories.AlbumRepository
import com.team28.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormSongViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
) : BaseViewModel() {

    var isUploading = MutableLiveData(false)
    var songEdit: Song? = null

    var songFile: File? = null
    var songName = ""
    var description = ""
    var imageFile: File? = null
    var album: MutableLiveData<Album> = MutableLiveData()
    val types: MutableLiveData<List<Type>> = MutableLiveData()
    val singers: MutableLiveData<List<Account>> = MutableLiveData()
    var lyric: String = ""
    var songStatus = 0

    var addStatus = MutableLiveData<Boolean?>(null)
    var message: String? = null

    var myAlbum = MutableLiveData<List<Album>>()

    fun isFormAdd(): Boolean = songEdit == null

    fun initSongInfo(song: Song) {
        songEdit = song

        songName = song.name
        description = song.description
        lyric = song.lyrics
        songStatus = song.songStatus
        song.album?.let {
            album.postValue(it)
        }
        song.singers?.let {
            singers.postValue(it)
        }
        song.types?.let {
            types.postValue(it)
        }
    }

    fun addSong() {
        isUploading.postValue(true)
        parentJob = viewModelScope.launch {
            val song = SongPost(
                songFile = songFile,
                songName = songName,
                imageSong = imageFile,
                description = description,
                lyrics = lyric,
                // TODO: Param post song
                album = Album(2),
                types = arrayListOf(Type(1), Type(2)),
                accounts = arrayListOf(Account(1), Account(2))
            )
            val result = songRepository.addSong(song)
            if (result is NetworkResult.Success) {
                message = "thành công: ${result.body.message}"
                isUploading.postValue(false)
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
                isUploading.postValue(false)
            }
            addStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun getAllMyAlbum() {
        parentJob = viewModelScope.launch {
            myAlbum.postValue(albumRepository.getAllMyAlbum())
        }
    }

}