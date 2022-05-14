package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.data.database.entities.SearchHistoryEntity
import com.team28.wondermusic.data.services.search_history.SearchHistoryLocalService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(
    private val localService: SearchHistoryLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getAllSearchHistory(): List<SearchHistoryEntity> {
        return withContext(dispatcher) {
            localService.getAllSearchHistory()
        }
    }

    suspend fun insert(searchHistoryEntity: SearchHistoryEntity) {
        withContext(dispatcher) {
            localService.insert(searchHistoryEntity)
        }
    }

    suspend fun deleteAll() {
        withContext(dispatcher) {
            localService.deleteAll()
        }
    }
}