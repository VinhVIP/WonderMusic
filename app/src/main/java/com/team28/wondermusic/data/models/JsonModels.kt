package com.team28.wondermusic.data.models

import com.squareup.moshi.Json

data class LoginModal(val email: String, val password: String)

data class LoginResponseJson(
    val message: String,
    val accessToken: String?,
    val data: AccountJson?
)

data class SignupModal(
    val email: String,
    val account_name: String,
    val password: String,
    val confirmPassword: String
)

data class ChangePasswordModal(
    val old_password: String,
    val new_password: String,
)

data class NotificationListJson(
    val message: String,
    val data: List<NotificationJson>? = emptyList(),
)

data class NotificationJson(
    @Json(name = "id_notification") val idNotification: Int,
    val content: String,
    val action: String,
    @Json(name = "id_account") val idAccount: Int,
    @Json(name = "notification_status") val status: Int,
    val day: String,
    val time: String
) {
    fun toNotification(): Notification {
        return Notification(
            idNotification = this.idNotification,
            content = this.content,
            action = this.action,
            notificationStatus = this.status,
            notificationTime = "$time - $day",
            // TODO: Thiếu account
            account = Account(idAccount = this.idAccount)
        )
    }
}

fun List<NotificationJson>.toListNotification(): List<Notification> {
    return map { it.toNotification() }
}

data class MessageJson(val message: String)

data class NotificationCountJson(
    val message: String,
    val data: Int,
)


// Playlist

data class ListPlaylistJson(
    val message: String,
    val data: List<PlaylistJson> = emptyList(),
)

// TODO: Chưa đúng yêu cầu
data class PlaylistJson(
    @Json(name = "id_playlist") val idPlaylist: Int,
    @Json(name = "name_playlist") val namePlaylist: String,
    @Json(name = "playlist_status") val playlistStatus: Int
) {
    fun toPlaylist(): Playlist {
        return Playlist(
            idPlaylist = this.idPlaylist,
            name = this.namePlaylist,
            playlistStatus = this.playlistStatus,
            // TODO: Chưa có account cho playlist
            account = Account(accountName = "Không biết"),
            // TODO: Chưa có danh sách bài hát thuộc playlist
            songs = emptyList(),
        )
    }
}

fun List<PlaylistJson>.toListPlaylist(): List<Playlist> {
    return map { it.toPlaylist() }
}

// ----- Comment -----

data class CommentJson(
    @Json(name = "account") val account: AccountJson,
    @Json(name = "id_cmt") val idCmt: Int,
    val content: String,
    val day: String,
    val time: String,
    val commentChildren: List<CommentJson>?,
) {
    fun toComment(): Comment {
        return Comment(
            idComment = this.idCmt,
            content = this.content,
            dateTime = "${this.time} - ${this.day}",
            account = this.account.toAccount(),
            children = commentChildren?.toListComment() ?: emptyList(),
        )
    }
}

fun List<CommentJson>.toListComment(): List<Comment> {
    return map { it.toComment() }
}

data class ListCommentJson(
    val message: String,
    val data: List<CommentJson>? = emptyList(),
)

data class CommentParentJson(
    @Json(name = "id_account") val idAccount: Int,
    @Json(name = "id_cmt") val idCmt: Int,
    val content: String,
    @Json(name = "date_time") val dateTime: String,
    @Json(name = "id_song") val idSong: Int,
    val listChildren: List<CommentJson>?,
) {
    fun toComment(): Comment {
        return Comment(
            idComment = this.idCmt,
            content = this.content,
            dateTime = this.dateTime,
            account = Account(idAccount = this.idAccount, accountName = "Không biết"),
            children = listChildren?.toListComment() ?: emptyList(),
        )
    }
}

data class CommentParentAndChildrenJson(
    val message: String,
    val data: CommentParentJson
)

data class ListSongJson(
//    val message: String,
    val data: List<SongJson>
)

data class SongJson(
    @Json(name = "id_song") val idSong: Int,
    @Json(name = "name_song") val nameSong: String,
    val created: String,
    val description: String?,
    val link: String?,
    val listen: Int,
    val lyrics: String?,
    @Json(name = "lovestatus") val loveStatus: Boolean = false,
    @Json(name = "image_song") val imageSong: String?,
    @Json(name = "song_status") val songStatus: Int,
    @Json(name = "qtylove") val totalLove: String,

    @Json(name = "account") val account: AccountJson,
    @Json(name = "singers") val singers: List<AccountJson>,
    @Json(name = "types") val types: List<TypeJson>,
    @Json(name = "album") val album: AlbumJson,
) {
    fun toSong(): Song {
        return Song(
            idSong = this.idSong,
            name = this.nameSong,
            link = this.link ?: "",
            image = this.imageSong ?: "",
            lyrics = this.lyrics ?: "",
            description = this.description ?: "",
            dateCreated = this.created,
            songStatus = this.songStatus,
            listen = this.listen,
            account = this.account.toAccount(),
            album = this.album.toAlbum(),
            types = this.types.toListType(),
            singers = this.singers.toListAccount(),
            loveStatus = this.loveStatus,
            like = this.totalLove.toInt(),
        )
    }
}

fun List<SongJson>.toListSong(): List<Song> {
    return map { it.toSong() }
}

data class TypeJson(
    @Json(name = "id_type") val idType: Int,
    @Json(name = "name_type") val nameType: String
) {
    fun toType(): Type {
        return Type(
            idType = this.idType,
            name = this.nameType,
        )
    }
}

data class ListTypeJson(
    val message: String,
    val data: List<TypeJson>
)

fun List<TypeJson>.toListType(): List<Type> {
    return map { it.toType() }
}

data class AlbumJson(
    @Json(name = "create_date") val createDate: String,
    @Json(name = "id_album") val idAlbum: Int,
    @Json(name = "name_album") val nameAlbum: String,
    val account: AccountJson?,
    val songs: List<SongJson>?,
) {
    fun toAlbum(): Album {
        return Album(
            idAlbum = this.idAlbum,
            name = this.nameAlbum,
            dateCreated = this.createDate,
            account = this.account?.toAccount(),
            songs = this.songs?.toListSong(),
        )
    }
}

data class ListAlbumJson(
    val message: String,
    val data: List<AlbumJson>
)

fun List<AlbumJson>.toListAlbum(): List<Album> {
    return map { it.toAlbum() }
}

data class AccountJson(
    @Json(name = "id_account") val idAccount: Int,
    @Json(name = "account_name") val accountName: String,
    val avatar: String,
    val email: String,
    @Json(name = "account_status") val accountStatus: Int,
    @Json(name = "create_date") val createDate: String,
    val follower: String?,
    val following: String?,
    val role: Int,
    @Json(name = "total_love") val totalLove: String?,
    @Json(name = "total_song") val totalSong: String?,
    @Json(name = "follow_status") val followStatus: Boolean?,
) {
    fun toAccount(): Account {
        return Account(
            idAccount = this.idAccount,
            email = this.email,
            accountName = this.accountName,
            avatar = this.avatar,
            dateCreated = this.createDate,
            role = this.role,
            accountStatus = this.accountStatus,
            totalSongs = if (this.totalSong == null) 0 else this.totalSong.toInt(),
            totalLikes = if (this.totalLove == null) 0 else this.totalLove.toInt(),
            totalFollowers = if (this.follower == null) 0 else this.follower.toInt(),
            totalFollowings = if (this.following == null) 0 else this.following.toInt(),
            followStatus = this.followStatus ?: false,
        )
    }
}

fun List<AccountJson>.toListAccount(): List<Account> {
    return map { it.toAccount() }
}

data class ListAccountJson(
    val message: String,
    val data: List<AccountJson>
)

data class SingleAccountJson(
    val message: String,
    val data: AccountJson
)