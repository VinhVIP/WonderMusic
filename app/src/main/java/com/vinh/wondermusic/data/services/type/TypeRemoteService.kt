package com.vinh.wondermusic.data.services.type

import com.vinh.wondermusic.base.network.BaseRemoteService
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.data.apis.TypeAPI
import com.vinh.wondermusic.data.apis.TypeModal
import com.vinh.wondermusic.data.models.MessageJson
import com.vinh.wondermusic.data.models.Type
import javax.inject.Inject

class TypeRemoteService @Inject constructor(
    private val typeAPI: TypeAPI
) : BaseRemoteService() {

    suspend fun addType(type: Type): NetworkResult<MessageJson> {
        return callApi { typeAPI.addType(TypeModal(type.name, type.description)) }
    }

    suspend fun updateType(type: Type): NetworkResult<MessageJson> {
        return callApi { typeAPI.updateType(type.idType, TypeModal(type.name, type.description)) }
    }

    suspend fun deleteType(type: Type): NetworkResult<MessageJson> {
        return callApi { typeAPI.deleteType(type.idType) }
    }
}