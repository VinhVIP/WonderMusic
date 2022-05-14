package com.team28.wondermusic.data.services.search_history

import com.team28.wondermusic.data.database.daos.SearchHistoryDAO
import com.team28.wondermusic.data.database.entities.SearchHistoryEntity
import javax.inject.Inject

class SearchHistoryLocalService @Inject constructor(
    private val searchHistoryDAO: SearchHistoryDAO
) {

    suspend fun getAllSearchHistory(): List<SearchHistoryEntity> {
        return searchHistoryDAO.getAllSearchHistory()
    }

    suspend fun insert(searchHistoryEntity: SearchHistoryEntity) {
        searchHistoryDAO.insert(searchHistoryEntity)
    }

    suspend fun deleteAll() {
        searchHistoryDAO.deleteAll()
    }
}