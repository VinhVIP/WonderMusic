package com.team28.wondermusic.base.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job

open class BaseViewModel : ViewModel() {

    protected var parentJob: Job? = null

    var isLoading = MutableLiveData(false)
        private set

    protected fun registerEventParentJobFinish() {
        parentJob?.invokeOnCompletion {
            isLoading.postValue(false)
        }
    }

    var exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("Hilt", "Exception ${throwable.message}")
    }
}