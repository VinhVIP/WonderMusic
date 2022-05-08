package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.models.*
import com.team28.wondermusic.data.services.account.AccountRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountRemoteService: AccountRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun login(modal: LoginModal): NetworkResult<LoginResponseJson> {
        return withContext(dispatcher) {
            accountRemoteService.login(modal)
        }
    }

    suspend fun getSongsOfAccount(idAccount: Int): List<Song> = withContext(dispatcher) {
        accountRemoteService.getSongsOfAccount(idAccount)
    }

    suspend fun getMySongs(): List<Song> = withContext(dispatcher) {
        accountRemoteService.getMySongs()
    }

    suspend fun getFollowers(idAccount: Int): List<Account> = withContext(dispatcher) {
        accountRemoteService.getFollowers(idAccount)
    }

    suspend fun getFollowings(idAccount: Int): List<Account> = withContext(dispatcher) {
        accountRemoteService.getFollowings(idAccount)
    }

    suspend fun getAlbumsOfAccount(idAccount: Int): List<Album> = withContext(dispatcher) {
        accountRemoteService.getAlbumsOfAccount(idAccount)
    }

    suspend fun getPlaylistsOfAccount(idAccount: Int): List<Playlist> = withContext(dispatcher) {
        accountRemoteService.getPlaylistsOfAccount(idAccount)
    }
}