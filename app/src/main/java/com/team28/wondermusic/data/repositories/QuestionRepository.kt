package com.team28.wondermusic.data.repositories

import com.team28.wondermusic.data.entities.Question
import com.team28.wondermusic.data.entities.toListQuestionEntities
import com.team28.wondermusic.data.entities.toListQuestions
import com.team28.wondermusic.data.services.QuestionLocalService
import com.team28.wondermusic.data.services.QuestionRemoteService
import com.team28.wondermusic.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val remoteService: QuestionRemoteService,
    private val localService: QuestionLocalService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getListQuestion(): List<Question> = withContext(dispatcher) {
        val savedQuestion = localService.getAllQuestion()

        if (savedQuestion.isNotEmpty()) {
            savedQuestion.toListQuestions()
        } else {
            getNewAndSave()
        }
    }

    suspend fun refresh(): List<Question> = withContext(dispatcher) {
        getNewAndSave()
    }

    private suspend fun getNewAndSave(): List<Question> {
        val questionList = remoteService.getListQuestion(currentPage = 1, pageSize = 1)
        val newListQuestion = questionList?.items ?: emptyList()

        if (newListQuestion.isNotEmpty()) {
            localService.deleteAllQuestion()
            localService.saveListQuestion(newListQuestion.toListQuestionEntities())
        }

        return newListQuestion
    }
}