package com.vinh.wondermusic.base.network

import com.vinh.wondermusic.common.DataLocal

object NetworkHelper {

    fun getAuthorizationHeader(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
//        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "Bearer ${DataLocal.ACCESS_TOKEN}"
        return headers.toMap()
    }

}