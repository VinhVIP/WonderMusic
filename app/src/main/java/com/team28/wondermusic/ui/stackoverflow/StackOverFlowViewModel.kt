package com.team28.wondermusic.ui.stackoverflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.BaseViewModel
import com.team28.wondermusic.data.entities.Question
import com.team28.wondermusic.data.repositories.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StackOverFlowViewModel @Inject constructor(private val questionRepository: QuestionRepository) :
    BaseViewModel() {

    var listQuestions = MutableLiveData<List<Question>>()
        private set

    fun fetchData() {
        parentJob = viewModelScope.launch(exceptionHandler) {
            isLoading.postValue(true)
            val questions = questionRepository.getListQuestion()
            listQuestions.postValue(questions)
        }
        registerEventParentJobFinish()
    }

    fun refresh() {
        parentJob = viewModelScope.launch(exceptionHandler) {
            isLoading.postValue(true)
            val questions = questionRepository.refresh()
            listQuestions.postValue(questions)

        }
        registerEventParentJobFinish()
    }
}