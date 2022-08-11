package com.vinh.wondermusic.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vinh.wondermusic.data.models.Account
import java.io.File

@Entity
data class AccountEntity(
    @PrimaryKey val idAccount: Int = 0,
    val email: String = "",
    val accountName: String = "",
    val avatar: String = "",
    val created: String = "",
    val totalSong: Int = 0,
    val totalLove: Int = 0,
    val totalFollowers: Int = 0,
    val totalFollowings: Int = 0,

    val avatarPath: String = "",
) {
    fun toAccount(): Account {
        return Account(
            idAccount = idAccount,
            email = email,
            accountName = accountName,
            avatar = avatar,
            dateCreated = created,
            totalSongs = totalSong,
            totalLikes = totalLove,
            avatarFile = if (avatarPath.isNotEmpty()) File(avatarPath) else null
        )
    }
}

