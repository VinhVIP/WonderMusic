package com.vinh.wondermusic.adapter

import com.vinh.wondermusic.data.models.*

interface SongClickListener {
    fun onSongClick(song: Song)
    fun onOpenMenu(song: Song, position: Int)
}

interface RemoveSongFromPlaylistListener {
    fun onRemoveSongFromPlaylist(song: Song, position: Int)
}

interface SongPlaylistListener {
    fun onSongPlaylistClick(song: Song, position: Int)
    fun onSongPlaylistReorder(songs: ArrayList<Song>)
}

interface PlaylistClickListener {
    fun onPlaylistClick(playlist: Playlist)
    fun onPlaylistMoreMenuClick(playlist: Playlist, position: Int)
}

interface AccountClickListener {
    fun onAccountClick(account: Account)
}

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
    fun onAlbumMoreMenuClick(album: Album, position: Int)
}

interface LyricsClickListener {
    fun onLineLyricClick(lineLyric: LineLyric)
}

interface TypeClickListener {
    fun onTypeClick(type: Type)
}

class EventBusModel {
    data class MusicTimeEvent(val time: Long, val duration: Long)

    data class MusicTimeSeekEvent(val timeMillis: Long)

    data class MusicPlayingEvent(val isPlaying: Boolean)

    data class SongListEvent(val songList: ArrayList<Song>)

    data class SongInfoEvent(val song: Song?)

    data class AudioSessionIdEvent(val sessionId: Int)

    class RequestSongEvent()

    class ClearMusic()
}

