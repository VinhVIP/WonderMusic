package com.team28.wondermusic.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team28.wondermusic.data.database.entities.AccountEntity
import com.team28.wondermusic.data.models.Account

@Dao
interface AccountDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    @Query("SELECT * FROM AccountEntity")
    suspend fun getAll(): List<AccountEntity>

    @Query("DELETE FROM AccountEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM AccountEntity WHERE idAccount = :idAccount")
    suspend fun getAccount(idAccount: Int): AccountEntity?
}