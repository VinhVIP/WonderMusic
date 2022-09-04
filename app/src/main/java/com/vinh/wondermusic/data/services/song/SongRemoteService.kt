package com.vinh.wondermusic.data.services.song

import com.vinh.wondermusic.base.network.BaseRemoteService
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.apis.SongAPI
import com.vinh.wondermusic.data.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SongRemoteService @Inject constructor(
    private val songAPI: SongAPI
) : BaseRemoteService() {

    suspend fun search(keyword: String): SearchJson? {
        val result = callApi { songAPI.search(keyword) }
        return if (result is NetworkResult.Success) {
            result.body.data
        } else {
            null
        }
    }

    suspend fun getAllTypes(): List<Type> {
        val result = callApi { songAPI.getAllTypes() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListType()
        } else {
            emptyList()
        }
    }

    suspend fun getSongsOfType(idType: Int, page: Int): List<Song> {
        val result = callApi { songAPI.getSongsOfType(idType, page) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getLoveSongs(page: Int): List<Song> {
        val result = callApi { songAPI.getLoveSongs(page) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getSongsOfFollowing(page: Int): List<Song> {
        val result = callApi { songAPI.getSongsOfFollowing(page) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getTopTenSongs(): List<Song> {
        val result = callApi { songAPI.getTopTenSongs() }
        return if (result is NetworkResult.Success) {
            result.body.data.toSong()
        } else {
            emptyList()
        }
    }

    suspend fun getTop100Songs(): List<Song> {
        val result = callApi { songAPI.getTop100Songs() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getTop3Songs(): List<SongListen> {
        val result = callApi { songAPI.getTop3Songs() }
        return if (result is NetworkResult.Success) {
            result.body.data
        } else {
            emptyList()
        }
    }

    suspend fun getBestSongs(): List<Song> {
        val result = callApi { songAPI.getBestSongs() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getNewestSongs(page: Int): List<Song> {
        val result = callApi { songAPI.getNewestSongs(page) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getTopListenInRangeDate(
        startDate: String,
        endDate: String,
        all: Int,
        type: String
    ): List<Song> {
        val result = callApi { songAPI.getTopListenInRange(startDate, endDate, all, type) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun listen(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.listen(idSong) }
    }

    suspend fun getSong(idSong: Int): Song? {
        val result = callApi { songAPI.getSong(idSong) }
        if (result is NetworkResult.Success) {
            return result.body.data.toSong()
        } else {
            return null
        }
    }

    suspend fun loveSong(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.loveSong(idSong) }
    }

    suspend fun unLoveSong(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.unLoveSong(idSong) }
    }

    suspend fun deleteSong(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.deleteSong(idSong) }
    }

    suspend fun addSong(song: SongPost): NetworkResult<MessageJson> {
        return callApi {
            var songMultipart: MultipartBody.Part? = null
            var imageMultipart: MultipartBody.Part? = null

            song.songFile?.let {
                val songFileRequestBody = it.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                songMultipart = MultipartBody.Part.createFormData(
                    "song",
                    song.songFile.name,
                    songFileRequestBody
                )
            }

            song.imageSong?.let {
                val imageFileRequestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
                imageMultipart = MultipartBody.Part.createFormData(
                    "img",
                    song.imageSong.name,
                    imageFileRequestBody
                )
            }

            songAPI.addSong(
                songFile = songMultipart!!,
                img = imageMultipart,
                name = song.songName.toRequestBody(MultipartBody.FORM),
                description = song.description!!.toRequestBody(MultipartBody.FORM),
                lyrics = song.lyrics!!.toRequestBody(MultipartBody.FORM),
                idAlbum = song.album!!.idAlbum,
                types = song.types.toListIdType(),
                accounts = song.accounts.toListIdAccount()
            )
        }
    }

    suspend fun updateSong(song: SongPost): NetworkResult<MessageJson> {
        return callApi {
//            var songMultipart: MultipartBody.Part? = null
            var imageMultipart: MultipartBody.Part? = null

//            song.songFile?.let {
//                val songFileRequestBody = it.asRequestBody("audio/mpeg".toMediaTypeOrNull())
//                songMultipart = MultipartBody.Part.createFormData(
//                    "song",
//                    song.songFile.name,
//                    songFileRequestBody
//                )
//            }

            song.imageSong?.let {
                val imageFileRequestBody = it.asRequestBody("image/*".toMediaTypeOrNull())
                imageMultipart = MultipartBody.Part.createFormData(
                    "img",
                    song.imageSong.name,
                    imageFileRequestBody
                )
            }

            songAPI.updateSong(
                idSong = song.idSong!!,
//                songFile = songMultipart,
                img = imageMultipart,
                name = song.songName.toRequestBody(MultipartBody.FORM),
                description = song.description!!.toRequestBody(MultipartBody.FORM),
                lyrics = song.lyrics!!.toRequestBody(MultipartBody.FORM),
                idAlbum = song.album!!.idAlbum,
                types = song.types.toListIdType(),
                accounts = song.accounts.toListIdAccount()
            )
        }
    }
}

fun ArrayList<Type>.toListIdType(): ArrayList<Int> {
    val list = arrayListOf<Int>()
    for (type in this) {
        list.add(type.idType)
    }
    return list
}

fun ArrayList<Account>.toListIdAccount(): ArrayList<Int> {
    val list = arrayListOf<Int>()
    for (account in this) {
        list.add(account.idAccount)
    }
    return list
}