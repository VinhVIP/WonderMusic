package com.team28.wondermusic.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team28.wondermusic.data.models.Account

@Entity
class AccountEntity(
    @PrimaryKey override val idAccount: Int,
    override val email: String?,
    override val accountName: String?,
    override val avatar: String?,
    override val dateCreated: String?,
    override val role: Int?,
    override val accountStatus: Int?,
    override val totalSongs: Int?,
    override val totalLikes: Int?,
    override val totalFollowers: Int?,
    override val totalFollowings: Int?,
    override val avatarPath: String?
) : BaseAccountEntity {

    fun toAccount(): Account {
        return Account(
            idAccount = this.idAccount,
            email = this.email ?: "",
            accountName = this.accountName ?: "",
            avatar = this.avatar ?: "",
            dateCreated = this.dateCreated ?: "",
            role = this.role ?: 0,
            accountStatus = this.accountStatus ?: 0,
            totalSongs = this.totalSongs ?: 0,
            totalLikes = this.totalLikes ?: 0,
            totalFollowers = this.totalFollowers ?: 0,
            totalFollowings = this.totalFollowings ?: 0,
        )
    }
}

fun List<AccountEntity>.toListAccounts(): List<Account> {
    return map { it.toAccount() }
}

