package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.base.network.NetworkResult
import com.team28.wondermusic.data.database.entities.*
import com.team28.wondermusic.data.models.*
import com.team28.wondermusic.data.services.account.AccountLocalService
import com.team28.wondermusic.data.services.song.SongLocalService
import com.team28.wondermusic.data.services.song.SongRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val songLocalService: SongLocalService,
    private val songRemoteService: SongRemoteService,
    private val accountLocalService: AccountLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun search(keyword: String): SearchJson? {
        return songRemoteService.search(keyword)
    }

    suspend fun addSong(song: SongPost): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.addSong(song)
        }
    }

    suspend fun updateSong(song: SongPost): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.updateSong(song)
        }
    }

    suspend fun deleteSong(idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.deleteSong(idSong)
        }
    }

    suspend fun getAllTypes(): List<Type> {
        return withContext(dispatcher) {
            songRemoteService.getAllTypes()
        }
    }

    suspend fun listen(idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.listen(idSong)
        }
    }

    suspend fun getSong(idSong: Int): Song? {
        return withContext(dispatcher) {
            songRemoteService.getSong(idSong)
        }
    }

    suspend fun loveSong(idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.loveSong(idSong)
        }
    }

    suspend fun unLoveSong(idSong: Int): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            songRemoteService.unLoveSong(idSong)
        }
    }

    suspend fun getTopTenSongs(): List<Song> = withContext(dispatcher) {
        songRemoteService.getTopTenSongs()
    }

    suspend fun getTop3Songs(): List<SongListen> = withContext(dispatcher) {
        songRemoteService.getTop3Songs()
    }

    suspend fun getSongsOfFollowing(page: Int): List<Song> = withContext(dispatcher) {
        songRemoteService.getSongsOfFollowing(page)
    }

    suspend fun getSongsOfType(idType: Int, page: Int): List<Song> = withContext(dispatcher) {
        songRemoteService.getSongsOfType(idType, page)
    }

    suspend fun getLoveSongs(page: Int): List<Song> = withContext(dispatcher) {
        songRemoteService.getLoveSongs(page)
    }

    suspend fun getNewestSongs(page: Int): List<Song> = withContext(dispatcher) {
        songRemoteService.getNewestSongs(page)
    }

    suspend fun getBestSongs(): List<Song> = withContext(dispatcher) {
        songRemoteService.getBestSongs()
    }

    suspend fun getListTopSongs(): List<Song> = withContext(dispatcher) {
        val savedSongs = songLocalService.getListTopSongs()
        savedSongs.toListSongs()
    }

    suspend fun getListMySongs(): List<Song> = withContext(dispatcher) {
        val savedSongs = songLocalService.getListMySongs()
        savedSongs.toListSongs()
    }

    suspend fun getListFavoriteSongs(): List<Song> = withContext(dispatcher) {
        val savedSongs = songLocalService.getListFavoriteSongs()
        savedSongs.toListSongs()
    }

    suspend fun saveListTopSongs(songs: List<Song>) {
        songs.forEach { song ->
            song.account?.let { accountLocalService.saveAccount(it.toAccountEntity()) }
            song.singers?.let {
                it.forEach { account ->
                    accountLocalService.saveAccount(account.toAccountEntity())
                    songLocalService.saveSongSingers(
                        SongSingersEntity(
                            song.idSong,
                            account.idAccount
                        )
                    )
                }
            }
        }
        songLocalService.saveListTopSongs(songs.toListTopSongsEntity())
    }

    suspend fun saveListMySongs(songs: List<Song>) {
        songs.forEach { song ->
            song.account?.let { accountLocalService.saveAccount(it.toAccountEntity()) }
            song.singers?.let {
                it.forEach { account ->
                    accountLocalService.saveAccount(account.toAccountEntity())
                    songLocalService.saveSongSingers(
                        SongSingersEntity(
                            song.idSong,
                            account.idAccount
                        )
                    )
                }
            }
        }
        songLocalService.saveListMySongs(songs.toListMySongsEntity())
    }

    suspend fun saveListFavoriteSongs(songs: List<Song>) {
        songs.forEach { song ->
            song.account?.let { accountLocalService.saveAccount(it.toAccountEntity()) }
            song.singers?.let {
                it.forEach { account ->
                    accountLocalService.saveAccount(account.toAccountEntity())
                    songLocalService.saveSongSingers(
                        SongSingersEntity(
                            song.idSong,
                            account.idAccount
                        )
                    )
                }
            }
        }
        songLocalService.saveListFavoriteSongs(songs.toListFavoriteSongsEntity())
    }

    suspend fun deleteListTopSongs() {
        songLocalService.deleteListTopSongs()
    }

    suspend fun deleteListMySongs() {
        songLocalService.deleteListMySongs()
    }

    suspend fun deleteListFavoriteSongs() {
        songLocalService.deleteListFavoriteSongs()
    }
}