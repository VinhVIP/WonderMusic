package com.vinh.wondermusic.data.repositories

import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.models.*
import com.vinh.wondermusic.data.services.account.AccountLocalService
import com.vinh.wondermusic.data.services.song.SongLocalService
import com.vinh.wondermusic.data.services.song.SongRemoteService
import com.vinh.wondermusic.di.IoDispatcher
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

    suspend fun getAndSaveBestSongs(): List<Song> = withContext(dispatcher) {
        val songs = songRemoteService.getTopTenSongs()
        if (songs.isNotEmpty()) {
            songLocalService.deleteListTopSongs()
            songLocalService.saveListTopSongs(songs.toListSongFullEntity())
        }
        songs
    }

    suspend fun getTop100Songs(): List<Song> = withContext(dispatcher) {
        songRemoteService.getTop100Songs()
    }

    suspend fun getBestSongs(): List<Song> = withContext(dispatcher) {
        songRemoteService.getBestSongs()
    }

    suspend fun updateSongImagePath(song: Song) = withContext(dispatcher) {
        song.imageFile?.let {
            songLocalService.updateSongImagePath(song.idSong, it.path)
        }
    }

    suspend fun updateSongFilePath(song: Song) = withContext(dispatcher) {
        song.songFile?.let {
            songLocalService.updateSongFilePath(song.idSong, it.path)
        }
    }
}