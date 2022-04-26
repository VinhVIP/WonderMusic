package com.team28.wondermusic.base.network

import com.beust.klaxon.Klaxon

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val body: T) : NetworkResult<T>()
    data class Error(val responseError: ResponseError) : NetworkResult<Nothing>()
}

data class ResponseError(val message: String? = null, val code: Int = -1) {
    companion object{
        fun fromJson(errorBody: String, code: Int): ResponseError {
            val response = Klaxon().parse<ResponseMessage>(errorBody)
            return ResponseError(response?.message, code)
        }
    }
}

data class ResponseMessage(val message: String)