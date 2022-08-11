package com.vinh.wondermusic.data.database.entities

import androidx.room.*
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.*
import java.io.File

@Entity
data class SongEntity(
    @PrimaryKey val idSong: Int = 0,
    val nameSong: String = "",
    val created: String = Helper.getCurrentDateTime(),
    val description: String = "",
    val link: String = "",
    val listen: Int = 0,
    val lyrics: String = "",
    val loveStatus: Boolean = false,
    val imageSong: String = "",
    val songStatus: Int = 0,
    val totalLove: Int = 0,

    val idAccount: Int = 0,
    val idAlbum: Int = 0,

    val downloaded: Boolean = false,
    val filePath: String = "",
    val imagePath: String = "",
)

@Entity(primaryKeys = ["idSong", "idAccount"])
class SongSingersEntity(
    val idSong: Int,
    val idAccount: Int,
)

data class SongFullEntity(
    @Embedded val song: SongEntity,

    @Relation(parentColumn = "idAccount", entityColumn = "idAccount", entity = AccountEntity::class)
    val account: AccountEntity,

    @Relation(
        parentColumn = "idSong",
        entityColumn = "idAccount",
        associateBy = Junction(SongSingersEntity::class)
    )
    val singers: List<AccountEntity>,

    @Relation(
        parentColumn = "idSong",
        entityColumn = "idType",
        associateBy = Junction(SongTypeEntity::class)
    )
    val types: List<TypeEntity>,

    @Relation(entityColumn = "idAlbum", parentColumn = "idAlbum")
    val album: AlbumEntity,
) {
    fun toSong(): Song {
        return Song(
            idSong = song.idSong,
            name = song.nameSong,
            link = song.link,
            image = song.imageSong,
            lyrics = song.lyrics,
            description = song.description,
            dateCreated = song.created,
            songStatus = song.songStatus,
            like = song.totalLove,
            listen = song.listen,
            loveStatus = song.loveStatus,
            downloaded = song.downloaded,

            account = account.toAccount(),
            album = album.toAlbum(),
            singers = singers.toListAccount(),
            types = types.toListType(),

            songFile = if (song.filePath.isNotEmpty()) File(song.filePath) else null,
            imageFile = if (song.imagePath.isNotEmpty()) File(song.imagePath) else null,
        )
    }
}

@Entity
data class AlbumEntity(
    @PrimaryKey val idAlbum: Int = 0,
    val nameAlbum: String = "",
    val created: String = "",
    val idAccount: Int,
) {
    fun toAlbum(): Album {
        return Album(
            idAlbum = idAlbum,
            name = nameAlbum,
            dateCreated = created,
        )
    }
}

@Entity
data class TypeEntity(
    @PrimaryKey val idType: Int = 0,
    val nameType: String = "",
    val description: String = "",
) {
    fun toType(): Type {
        return Type(
            idType = idType,
            name = nameType,
            description = description
        )
    }
}

@Entity(primaryKeys = ["idSong", "idType"])
class SongTypeEntity(
    val idSong: Int,
    val idType: Int,
)