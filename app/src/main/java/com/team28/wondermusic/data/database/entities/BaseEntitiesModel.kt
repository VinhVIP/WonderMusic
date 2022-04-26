package com.team28.wondermusic.data.database.entities

import androidx.room.Entity

interface BaseSongEntity {
    val idSong: Int
    val nameSong: String?
    val link: String?
    val lyrics: String?
    val description: String?
    val created: String?
    val songStatus: Int?
    val like: Int?
    val listen: Int?
    val loveStatus: Boolean?
    val idAccount: Int?
    val idAlbum: Int?
    val imageSong: String?
    val downloaded: Boolean?
    val filePath: String?
    val imagePath: String?
}

interface BaseAccountEntity {
    val idAccount: Int
    val email: String?
    val accountName: String?
    val avatar: String?
    val dateCreated: String?
    val role: Int?
    val accountStatus: Int?
    val totalSongs: Int?
    val totalLikes: Int?
    val totalFollowers: Int?
    val totalFollowings: Int?
    val avatarPath: String?
}

@Entity(primaryKeys = ["idSong", "idAccount"])
data class SongSingersEntity(
    val idSong: Int,
    val idAccount: Int
)