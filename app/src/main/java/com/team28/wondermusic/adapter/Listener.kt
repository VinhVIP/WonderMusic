package com.team28.wondermusic.adapter

import com.team28.wondermusic.data.models.*

interface SongClickListener {
    fun onSongClick(song: Song)
    fun onOpenMenu(song: Song)
}

interface SongPlaylistListener {
    fun onSongPlaylistClick(song: Song, position: Int)
    fun onSongPlaylistReorder(songs: ArrayList<Song>)
}

interface PlaylistClickListener {
    fun onPlaylistClick(playlist: Playlist)
}

interface AccountClickListener {
    fun onAccountClick(account: Account)
}

interface AlbumClickListener {
    fun onAlbumClick(album: Album)
}

interface LyricsClickListener {
    fun onLineLyricClick(lineLyric: LineLyric)
}

class EventBusModel {
    data class MusicTimeEvent(val time: Int)

    data class MusicTimeSeekEvent(val timeMillis: Int)

    data class MusicDurationEvent(val durationSeconds: Int)

    data class MusicPlayingEvent(val isPlaying: Boolean)

    data class SongListEvent(val songList: ArrayList<Song>)

    data class SongInfoEvent(val song: Song)

    data class AudioSessionIdEvent(val sessionId: Int)

    class RequestSongEvent()

    class ClearMusic()
}

