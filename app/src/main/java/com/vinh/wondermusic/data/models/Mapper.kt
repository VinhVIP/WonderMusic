package com.vinh.wondermusic.data.models

import com.vinh.wondermusic.data.database.entities.*


// Chuyển 1 dòng String lyric thành đối tượng object LineLyric
fun String.convertToLineLyric(): LineLyric {
    val closeBracketIndex = indexOf(']')
    if (closeBracketIndex != -1) {
        val time = substring(1, closeBracketIndex)

        val twoDot = time.indexOf(':')
        val dot = time.indexOf('.')

        val minute = time.substring(1, twoDot).toInt()
        val second = time.substring(twoDot + 1, dot).toInt()
        val millis = time.substring(dot + 1).toInt()

        val timeMillis = minute * 60 * 1000 + second * 1000 + millis * 10

        return LineLyric(timeMillis, substring(closeBracketIndex + 1).trim())
    }

    return LineLyric(0, this)
}

fun Song.singersToString(): String {
    var names = ""
    singers.let {
        for (singer in it) {
            names += singer.accountName + ", "
        }
        if (names.length > 1) names = names.substring(0, names.length - 2)
    }
    return names
}

fun Int.toTimeFormat(): String {
    val hour = this / 3600
    val minute = (this % 3600) / 60
    val second = (this % 3600) % 60

    return if (hour == 0) {
        if (minute < 10) String.format("%d:%02d", minute, second)
        else String.format("%02d:%02d", minute, second)
    } else {
        String.format("%d:%02d:%02d", hour, minute, second)
    }
}

fun List<Type>.toStringTypes(): String {
    var str = ""
    for (i in 0 until this.size - 1) {
        str += this[i].name + ", "
    }
    if (this.isNotEmpty()) {
        str += this.last().name
    }
    return str
}

fun List<Account>.toListAccountEntity(): List<AccountEntity> {
    return map { it.toAccountEntity() }
}

fun List<Type>.toListTypeEntity(): List<TypeEntity> {
    return map { it.toTypeEntity() }
}

fun List<Song>.toListSongFullEntity(): List<SongFullEntity> {
    return map { it.toSongFullEntity() }
}

@JvmName("toListAccountAccountEntity")
fun List<AccountEntity>.toListAccount(): List<Account> {
    return map { it.toAccount() }
}

@JvmName("toListTypeTypeEntity")
fun List<TypeEntity>.toListType(): List<Type> {
    return map { it.toType() }
}

@JvmName("toListAlbumAlbumEntity")
fun List<AlbumEntity>.toListAlbum(): List<Album> {
    return map { it.toAlbum() }
}

@JvmName("toListSongSongFullEntity")
fun List<SongFullEntity>.toListSong(): List<Song> {
    return map { it.toSong() }
}

fun List<NotificationJson>.toListNotification(): List<Notification> {
    return map { it.toNotification() }
}

fun List<Notification>.toListNotificationEntity(): List<NotificationEntity> {
    return map { it.toNotificationEntity() }
}

fun List<PlaylistJson>.toListPlaylist(): List<Playlist> {
    return map { it.toPlaylist() }
}

fun List<CommentJson>.toListComment(): List<Comment> {
    return map { it.toComment() }
}

fun List<SongJson>.toListSong(): List<Song> {
    return map { it.toSong() }
}

fun List<TypeJson>.toListType(): List<Type> {
    return map { it.toType() }
}

fun List<AlbumJson>.toListAlbum(): List<Album> {
    return map { it.toAlbum() }
}

fun List<AccountJson>.toListAccount(): List<Account> {
    return map { it.toAccount() }
}