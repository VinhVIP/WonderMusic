package com.team28.wondermusic.data.services.song

import com.team28.wondermusic.data.database.daos.SongDAO
import com.team28.wondermusic.data.database.entities.*
import javax.inject.Inject

class SongLocalService @Inject constructor(
    private val songDAO: SongDAO,
) {

    // ----- Top Song -----
    suspend fun getListTopSongs(): List<TopSong> {
        return songDAO.getListTopSongs()
    }

    suspend fun saveListTopSongs(songs: List<TopSongEntity>) {
        songDAO.insertListTopSongs(songs)
    }

    suspend fun deleteListTopSongs() {
        songDAO.deleteListTopSongs()
    }

    // ----- My Song -----
    suspend fun getListMySongs(): List<MySong> {
        return songDAO.getListMySongs()
    }

    suspend fun saveListMySongs(songs: List<MySongEntity>) {
        songDAO.insertListMySongs(songs)
    }

    suspend fun deleteListMySongs() {
        songDAO.deleteListMySongs()
    }

    // ----- Favorite Song -----
    suspend fun getListFavoriteSongs(): List<FavoriteSong> {
        return songDAO.getListFavoriteSongs()
    }

    suspend fun saveListFavoriteSongs(songs: List<FavoriteSongEntity>) {
        songDAO.insertListFavoriteSongs(songs)
    }

    suspend fun deleteListFavoriteSongs() {
        songDAO.deleteListFavoriteSongs()
    }

    suspend fun saveSongSingers(songSingersEntity: SongSingersEntity) {
        songDAO.insertSongSingers(songSingersEntity)
    }

}