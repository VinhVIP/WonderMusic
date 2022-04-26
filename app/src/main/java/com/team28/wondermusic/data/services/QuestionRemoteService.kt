package com.team28.wondermusic.data.services

//import com.team28.wondermusic.data.apis.QuestionAPI
//import com.team28.wondermusic.data.database.old_entities.QuestionList
//import javax.inject.Inject

//class QuestionRemoteService @Inject constructor(private val questionAPI: QuestionAPI) {
//
//    suspend fun getListQuestion(currentPage: Int, pageSize: Int): QuestionList? {
//        val parameters = mutableMapOf<String, String>()
//
//        parameters["site"] = "stackoverflow"
//        parameters["pagesize"] = "$pageSize"
//        parameters["page"] = "$currentPage"
//
//        val response = questionAPI.getListQuestion(parameters = parameters)
//
//        if (response.isSuccessful) {
//            return response.body()
//        } else {
//            throw Exception(response.message())
//        }
//    }
//}