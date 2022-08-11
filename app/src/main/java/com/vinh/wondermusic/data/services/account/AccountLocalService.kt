package com.vinh.wondermusic.data.services.account

import com.vinh.wondermusic.data.database.daos.AccountDAO
import com.vinh.wondermusic.data.database.entities.AccountEntity
import javax.inject.Inject

class AccountLocalService @Inject constructor(private val accountDAO: AccountDAO) {

    suspend fun getAllAccounts(): List<AccountEntity> {
        return accountDAO.getAll()
    }

    suspend fun deleteAllAccounts() {
        accountDAO.deleteAll()
    }

    suspend fun saveListAccounts(accounts: List<AccountEntity>) {
        accountDAO.insertAll(accounts)
    }

    suspend fun saveAccount(account: AccountEntity) {
        accountDAO.insert(account)
    }

    suspend fun getAccount(idAccount: Int): AccountEntity? {
        return accountDAO.getAccount(idAccount)
    }
}