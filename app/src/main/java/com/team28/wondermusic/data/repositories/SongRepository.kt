package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.data.database.entities.*
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.services.account.AccountLocalService
import com.team28.wondermusic.data.services.song.SongLocalService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val songLocalService: SongLocalService,
    private val accountLocalService: AccountLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

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