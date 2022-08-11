package com.vinh.wondermusic.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vinh.wondermusic.data.database.entities.AccountEntity

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