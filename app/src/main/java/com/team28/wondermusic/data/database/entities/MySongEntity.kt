package com.team28.wondermusic.data.database.entities

import androidx.room.*
import com.team28.wondermusic.data.models.Song

@Entity
data class MySongEntity(
    @PrimaryKey override val idSong: Int,
    override val nameSong: String?,
    override val link: String?,
    override val lyrics: String?,
    override val description: String?,
    override val created: String?,
    override val songStatus: Int?,
    override val like: Int?,
    override val listen: Int?,
    override val loveStatus: Boolean?,
    override val idAccount: Int?,
    override val idAlbum: Int?,
    override val imageSong: String?,
    override val downloaded: Boolean?,
    override val filePath: String?,
    override val imagePath: String?
) : BaseSongEntity

data class MySong(
    @Embedded
    val songEntity: MySongEntity,

    @Relation(parentColumn = "idAccount", entityColumn = "idAccount")
    val accountEntity: AccountEntity,

    @Relation(
        parentColumn = "idSong",
        entityColumn = "idAccount",
        associateBy = Junction(SongSingersEntity::class)
    )
    val singers: List<AccountEntity>
) {
    fun toSong(): Song {
        return Song(
            idSong = songEntity.idSong,
            name = songEntity.nameSong ?: "",
            link = songEntity.link ?: "",
            image = songEntity.imageSong ?: "",
            lyrics = songEntity.lyrics ?: "",
            description = songEntity.description ?: "",
            dateCreated = songEntity.created ?: "",
            songStatus = songEntity.songStatus ?: 0,
            like = songEntity.like ?: 0,
            listen = songEntity.listen ?: 0,
            loveStatus = songEntity.loveStatus ?: false,
            account = accountEntity.toAccount(),
            singers = singers.toListAccounts()
        )
    }
}

fun List<MySong>.toListSongs(): List<Song> {
    return map { it.toSong() }
}