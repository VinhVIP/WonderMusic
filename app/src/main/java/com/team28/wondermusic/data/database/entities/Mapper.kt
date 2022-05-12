package com.team28.wondermusic.data.database.entities

import com.team28.wondermusic.data.models.*


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
    singers?.let {
        for (singer in it) {
            names += singer.accountName + ", "
        }
        if (names.length > 1) names = names.substring(0, names.length - 2)
        return names
    }
    return ""
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


fun Account.toAccountEntity(): AccountEntity {
    return AccountEntity(
        idAccount = this.idAccount,
        email = this.email,
        accountName = this.accountName,
        avatar = this.avatar,
        dateCreated = this.dateCreated,
        role = this.role,
        accountStatus = this.accountStatus,
        totalSongs = this.totalSongs,
        totalLikes = this.totalLikes,
        totalFollowers = this.totalFollowers,
        totalFollowings = this.totalFollowings,
        avatarPath = "",
    )
}

fun Song.toTopSongEntity(): TopSongEntity {
    return TopSongEntity(
        idSong = this.idSong,
        nameSong = this.name,
        link = this.link,
        lyrics = this.lyrics,
        description = this.description,
        created = this.dateCreated,
        songStatus = this.songStatus,
        like = this.like,
        listen = this.listen,
        loveStatus = this.loveStatus,
        idAccount = this.account?.idAccount,
        idAlbum = this.album?.idAlbum,
        imageSong = this.image,
        downloaded = false,
        filePath = null,
        imagePath = null,
    )
}

fun Song.toMySongEntity(): MySongEntity {
    return MySongEntity(
        idSong = this.idSong,
        nameSong = this.name,
        link = this.link,
        lyrics = this.lyrics,
        description = this.description,
        created = this.dateCreated,
        songStatus = this.songStatus,
        like = this.like,
        listen = this.listen,
        loveStatus = this.loveStatus,
        idAccount = this.account?.idAccount,
        idAlbum = this.album?.idAlbum,
        imageSong = this.image,
        downloaded = false,
        filePath = null,
        imagePath = null,
    )
}

fun Song.toFavoriteSongEntity(): FavoriteSongEntity {
    return FavoriteSongEntity(
        idSong = this.idSong,
        nameSong = this.name,
        link = this.link,
        lyrics = this.lyrics,
        description = this.description,
        created = this.dateCreated,
        songStatus = this.songStatus,
        like = this.like,
        listen = this.listen,
        loveStatus = this.loveStatus,
        idAccount = this.account?.idAccount,
        idAlbum = this.album?.idAlbum,
        imageSong = this.image,
        downloaded = false,
        filePath = null,
        imagePath = null,
    )
}

fun Notification.toNotificationEntity(): NotificationEntity {
    return NotificationEntity(
        idNotification = this.idNotification,
        content = this.content,
        action = this.action,
        notificationStatus = this.notificationStatus,
        notificationTime = this.notificationTime,
        idAccount = this.account?.idAccount,
        accountName = this.account?.accountName,
    )
}


fun List<Song>.toListTopSongsEntity(): List<TopSongEntity> {
    return map { it.toTopSongEntity() }
}

fun List<Song>.toListMySongsEntity(): List<MySongEntity> {
    return map { it.toMySongEntity() }
}

fun List<Song>.toListFavoriteSongsEntity(): List<FavoriteSongEntity> {
    return map { it.toFavoriteSongEntity() }
}

fun List<Notification>.toListNotificationEntity(): List<NotificationEntity> {
    return map { it.toNotificationEntity() }
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