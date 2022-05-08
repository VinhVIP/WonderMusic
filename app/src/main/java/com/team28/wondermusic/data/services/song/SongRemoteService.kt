package com.team28.wondermusic.data.services.song

import com.team28.wondermusic.base.network.BaseRemoteService
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.apis.SongAPI
import com.team28.wondermusic.data.models.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SongRemoteService @Inject constructor(
    private val songAPI: SongAPI
) : BaseRemoteService() {

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

    suspend fun loveSong(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.loveSong(idSong) }
    }

    suspend fun unLoveSong(idSong: Int): NetworkResult<MessageJson> {
        return callApi { songAPI.unLoveSong(idSong) }
    }

    suspend fun addSong(song: SongPost): NetworkResult<MessageJson> {

        return callApi {
            val songFileRequestBody = song.songFile!!
                .asRequestBody("audio/mpeg".toMediaTypeOrNull())
            val imageFileRequestBody = song.imageSong!!.asRequestBody("image/*".toMediaTypeOrNull())

            songAPI.addSong(
                songFile = MultipartBody.Part.createFormData(
                    "song",
                    song.songFile.name,
                    songFileRequestBody
                ),
                img = MultipartBody.Part.createFormData(
                    "img",
                    song.imageSong.name,
                    imageFileRequestBody
                ),
                name = song.songName.toRequestBody(MultipartBody.FORM),
                description = song.description!!.toRequestBody(MultipartBody.FORM),
                lyrics = song.lyrics!!.toRequestBody(MultipartBody.FORM),
                idAlbum = song.album!!.idAlbum,
                types = song.types.toListIdType(),
                accounts = song.accounts.toListIdAccount(),
//                idAlbum = Klaxon().toJsonString(song.album!!.idAlbum)
//                    .toRequestBody(MultipartBody.FORM),
//                types = Klaxon().toJsonString(song.types.toListIdType())
//                    .toRequestBody(MultipartBody.FORM),
//                accounts = Klaxon().toJsonString(song.accounts.toListIdAccount())
//                    .toRequestBody(MultipartBody.FORM),
            )
        }
    }
}

fun ArrayList<Type>.toListIdType(): ArrayList<Int> {
    return map { it.idType } as ArrayList<Int>
}

fun ArrayList<Account>.toListIdAccount(): ArrayList<Int> {
    return map { it.idAccount } as ArrayList<Int>
}