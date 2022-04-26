package com.team28.wondermusic.base.network

import com.team28.wondermusic.common.DataLocal

object NetworkHelper {

    fun getAuthorizationHeader(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Bearer ${DataLocal.ACCESS_TOKEN}"
        return headers.toMap()
    }

}