package com.team28.wondermusic.data.services.account

import com.team28.wondermusic.base.network.BaseRemoteService
import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.apis.AccountAPI
import com.team28.wondermusic.data.models.*
import javax.inject.Inject

class AccountRemoteService @Inject constructor(
    private val accountAPI: AccountAPI
) : BaseRemoteService() {

    suspend fun login(modal: LoginModal): NetworkResult<LoginResponseJson> {
        return callApi { accountAPI.login(modal) }
    }

    suspend fun getSongsOfAccount(idAccount: Int): List<Song> {
        val result = callApi { accountAPI.getSongsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getMySongs(): List<Song> {
        val result = callApi { accountAPI.getMySongs() }
        return if (result is NetworkResult.Success) {
            result.body.data.toListSong()
        } else {
            emptyList()
        }
    }

    suspend fun getFollowers(idAccount: Int): List<Account> {
        val result = callApi { accountAPI.getFollowers(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getFollowings(idAccount: Int): List<Account> {
        val result = callApi { accountAPI.getFollowings(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAccount()
        } else {
            emptyList()
        }
    }

    suspend fun getAlbumsOfAccount(idAccount: Int): List<Album> {
        val result = callApi { accountAPI.getAlbumsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListAlbum()
        } else {
            emptyList()
        }
    }

    suspend fun getPlaylistsOfAccount(idAccount: Int): List<Playlist> {
        val result = callApi { accountAPI.getPlaylistsOfAccount(idAccount) }
        return if (result is NetworkResult.Success) {
            result.body.data.toListPlaylist()
        } else {
            emptyList()
        }
    }
}