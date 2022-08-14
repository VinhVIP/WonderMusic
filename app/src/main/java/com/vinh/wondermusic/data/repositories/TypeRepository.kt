package com.vinh.wondermusic.data.repositories

import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.data.services.type.TypeRemoteService
import com.vinh.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TypeRepository @Inject constructor(
    private val remoteService: TypeRemoteService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun addType(type: Type): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.addType(type)
        }
    }

    suspend fun updateType(type: Type): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.updateType(type)
        }
    }

    suspend fun deleteType(type: Type): NetworkResult<MessageJson> {
        return withContext(dispatcher) {
            remoteService.deleteType(type)
        }
    }
}