package com.vinh.wondermusic.data.models

import android.os.Parcelable
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.database.entities.*
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

    val avatarFile: File? = null,
) : Parcelable {
    fun toAccountEntity(): AccountEntity {
        return AccountEntity(
            idAccount = idAccount,
            email = email,
            accountName = accountName,
            avatar = avatar,
            created = dateCreated,
            totalSong = totalSongs,
            totalLove = totalLikes,
            totalFollowers = totalFollowers,
            totalFollowings = totalFollowings,

            avatarPath = avatarFile?.path ?: ""
        )
    }
}

@Parcelize
data class Song(
    val idSong: Int = 0,
    val name: String = "",
    val link: String = "",
    val image: String = "",
    val lyrics: String = "",
    val description: String = "",
    val dateCreated: String = Helper.getCurrentDateTime(),
    val songStatus: Int = 0,
    val like: Int = 0,
    val listen: Int = 0,
    var loveStatus: Boolean = false,
    var downloaded: Boolean = false,

    val account: Account,
    val album: Album,
    val singers: List<Account> = emptyList(),
    val types: List<Type> = emptyList(),

    var songFile: File? = null,
    var imageFile: File? = null,
) : Parcelable {

    fun toSongFullEntity(): SongFullEntity {
        return SongFullEntity(
            song = toSongEntity(),
            account = account.toAccountEntity(),
            singers = singers.toListAccountEntity(),
            types = types.toListTypeEntity(),
            album = album.toAlbumEntity()
        )
    }

    fun toSongEntity(): SongEntity {
        return SongEntity(
            idSong = idSong,
            nameSong = name,
            created = dateCreated,
            description = description,
            link = link,
            listen = listen,
            lyrics = lyrics,
            loveStatus = loveStatus,
            imageSong = image,
            songStatus = songStatus,
            totalLove = like,

            idAccount = account.idAccount,
            idAlbum = album.idAlbum,

            downloaded = downloaded,
            filePath = songFile?.path ?: "",
            imagePath = imageFile?.path ?: "",
        )
    }
}

@Parcelize
data class Album(
    val idAlbum: Int = 0,
    var name: String = "",
    val dateCreated: String = "",
    val account: Account? = null,
    var songs: List<Song> = emptyList()
) : Parcelable {
    fun toAlbumEntity(): AlbumEntity {
        return AlbumEntity(
            idAlbum = idAlbum,
            nameAlbum = name,
            created = dateCreated,
            idAccount = account?.idAccount ?: 0
        )
    }
}

@Parcelize
data class Comment(
    val idComment: Int = 0,
    val account: Account,
    val content: String = "",
    val dateTime: String = "",
    val children: List<Comment> = emptyList()
) : Parcelable

@Parcelize
data class Type(
    val idType: Int = 0,
    val name: String = "",
    val description: String = ""
) : Parcelable {
    fun toTypeEntity(): TypeEntity {
        return TypeEntity(
            idType = idType,
            nameType = name,
            description = description,
        )
    }
}

@Parcelize
data class Playlist(
    val idPlaylist: Int,
    var name: String,
    val account: Account,
    var playlistStatus: Int,
    var songs: List<Song>?
) : Parcelable

@Parcelize
data class Notification(
    val idNotification: Int,
    val content: String,
    val action: String,
    var notificationStatus: Int,
    val notificationTime: String,
    val account: Account? = null
) : Parcelable {
    fun toNotificationEntity(): NotificationEntity {
        return NotificationEntity(
            idNotification = idNotification,
            content = content,
            action = action,
            notificationStatus = notificationStatus,
            notificationTime = notificationTime,
            idAccount = account?.idAccount ?: 0,
            accountName = account?.accountName ?: ""
        )
    }
}


data class SongPost(
    val idSong: Int? = null,
    val songFile: File? = null,
    val songName: String,
    val imageSong: File? = null,
    val description: String?,
    val lyrics: String?,
    val album: Album?,
    val types: ArrayList<Type>,
    val accounts: ArrayList<Account>,
)

data class AccountUpdate(
    val idAccount: Int,
    val name: String,
    val avatar: File?,
)