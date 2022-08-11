package com.vinh.wondermusic.ui.formsong

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.common.exists
import com.vinh.wondermusic.data.models.*
import com.vinh.wondermusic.data.repositories.AccountRepository
import com.vinh.wondermusic.data.repositories.AlbumRepository
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FormSongViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val accountRepository: AccountRepository,
) : BaseViewModel() {

    var isUploading = MutableLiveData(false)
    var isLoadingListSingers = MutableLiveData(false)
    var songEdit: Song? = null

    var songFile = MutableLiveData<File?>(null)
    var songName = ""
    var description = ""
    var imageFile: File? = null
    var album: MutableLiveData<Album?> = MutableLiveData()
    val types: MutableLiveData<List<Type>> = MutableLiveData()
    val singers: MutableLiveData<List<Account>> = MutableLiveData()
    var lyric: String = ""
    var songStatus = 0

    var addStatus = MutableLiveData<Boolean?>(null)
    var deleteStatus = MutableLiveData<Boolean?>(null)
    var message: String? = null

    var myAlbum = MutableLiveData<List<Album>>()
    var listAccountSearch = MutableLiveData<List<Account>>()
    var allTypes = MutableLiveData<List<Type>>()

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

    fun addSingers(account: Account) {
        val list = mutableListOf<Account>()
        singers.value?.let { list.addAll(it) }

        if (!list.exists(account)) {
            list.add(account)
            singers.postValue(list)
        }
    }

    fun removeSingers(account: Account) {
        if (account.idAccount == DataLocal.myAccount.idAccount) {
            return
        }
        val list = mutableListOf<Account>()
        singers.value?.let { list.addAll(it) }

        list.removeIf { it.idAccount == account.idAccount }
        singers.postValue(list)
    }

    fun addType(type: Type) {
        val list = mutableListOf<Type>()
        types.value?.let { list.addAll(it) }

        if (!list.exists(type)) {
            list.add(type)
            types.postValue(list)
        }
    }

    fun removeType(type: Type) {
        val list = mutableListOf<Type>()
        types.value?.let { list.addAll(it) }

        list.removeIf { it.idType == type.idType }
        types.postValue(list)
    }

    fun addSong() {
        isUploading.postValue(true)
        parentJob = viewModelScope.launch {
            val song = SongPost(
                songFile = songFile.value,
                songName = songName,
                imageSong = imageFile,
                description = description,
                lyrics = lyric,
                album = album.value!!,
                types = types.value as ArrayList<Type>,
                accounts = singers.value as ArrayList<Account>,
            )
            val result = songRepository.addSong(song)
            if (result is NetworkResult.Success) {
                message = result.body.message
                isUploading.postValue(false)
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
                isUploading.postValue(false)
            }
            addStatus.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun updateSong() {
        for (account in singers.value!!) {
            Log.d("vinhabc", account.accountName)
        }

        isUploading.postValue(true)
        parentJob = viewModelScope.launch {
            val song = SongPost(
                idSong = songEdit!!.idSong,
                songFile = songFile.value,
                songName = songName,
                imageSong = imageFile,
                description = description,
                lyrics = lyric,
                album = album.value!!,
                types = types.value as ArrayList<Type>,
                accounts = singers.value as ArrayList<Account>,
            )
            val result = songRepository.updateSong(song)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            addStatus.postValue(result is NetworkResult.Success)
            isUploading.postValue(false)
        }
        registerEventParentJobFinish()
    }

    fun deleteSong(idSong: Int) {
        viewModelScope.launch {
            val result = songRepository.deleteSong(idSong)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            deleteStatus.postValue(result is NetworkResult.Success)
        }
    }

    fun getAllMyAlbum() {
        parentJob = viewModelScope.launch {
            myAlbum.postValue(albumRepository.getAllMyAlbum())
        }
    }

    fun searchAccount(keyword: String) {
        isLoadingListSingers.postValue(true)
        viewModelScope.launch {
            listAccountSearch.postValue(accountRepository.searchAccount(keyword))
            isLoadingListSingers.postValue(false)
        }
    }

    fun getAllTypes() {
        viewModelScope.launch {
            allTypes.postValue(songRepository.getAllTypes())
        }
    }
}