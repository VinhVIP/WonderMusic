package com.team28.wondermusic.data.models

import com.squareup.moshi.Json

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
    val data: List<PlaylistJson>? = emptyList(),
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
