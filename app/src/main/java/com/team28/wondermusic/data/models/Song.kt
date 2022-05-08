package com.team28.wondermusic.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

data class LineLyric(val startTime: Int, val text: String)

@Parcelize
data class Account(
    val idAccount: Int = -1,
    val email: String = "",
    val accountName: String = "",
    val avatar: String = "",
    val dateCreated: String = "",
    val role: Int = 0,
    val accountStatus: Int = 0,
    val totalSongs: Int = 0,
    val totalLikes: Int = 0,
    val totalFollowers: Int = 0,
    val totalFollowings: Int = 0,
    val followStatus: Boolean = false,
) : Parcelable

@Parcelize
data class Song(
    val idSong: Int,
    val name: String,
    val link: String,
    val image: String,
    val lyrics: String,
    val description: String,
    val dateCreated: String,
    val songStatus: Int,
    val like: Int,
    val listen: Int,
    val account: Account?,
    val album: Album? = null,
    var loveStatus: Boolean = false,
    val singers: List<Account>? = null,
    val types: List<Type>? = null,
) : Parcelable

@Parcelize
data class Album(
    val idAlbum: Int,
    var name: String? = "",
    val dateCreated: String? = "",
    val account: Account? = null,
    var songs: List<Song>? = null
) : Parcelable

@Parcelize
data class Comment(
    val idComment: Int,
    val account: Account,
    val content: String,
    val dateTime: String,
    val children: List<Comment>
) : Parcelable

@Parcelize
data class Type(
    val idType: Int,
    val name: String? = "",
    val description: String? = ""
) : Parcelable

@Parcelize
data class Playlist(
    val idPlaylist: Int,
    var name: String,
    val account: Account,
    var playlistStatus: Int,
    val songs: List<Song>?
) : Parcelable

@Parcelize
data class Notification(
    val idNotification: Int,
    val content: String,
    val action: String,
    var notificationStatus: Int,
    val notificationTime: String,
    val account: Account? = null
) : Parcelable


data class SongPost(
    val songFile: File?,
    val songName: String,
    val imageSong: File?,
    val description: String?,
    val lyrics: String?,
    val album: Album?,
    val types: ArrayList<Type>,
    val accounts: ArrayList<Account>,
)