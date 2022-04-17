package com.team28.wondermusic.data.apis

import com.team28.wondermusic.data.entities.QuestionList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.QueryMap

interface QuestionAPI {

    @GET("/questions")
    suspend fun getListQuestion(
        @HeaderMap header: Map<String, String> = mapOf(),
        @QueryMap parameters: Map<String, String> = mapOf()
    ): Response<QuestionList>
}