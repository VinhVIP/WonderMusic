package com.vinh.wondermusic.ui.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.network.NetworkResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.data.repositories.SongRepository
import com.vinh.wondermusic.data.repositories.TypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageTypeViewModel @Inject constructor(
    private val songRepository: SongRepository,
    private val typeRepository: TypeRepository,
) : BaseViewModel() {

    var types = MutableLiveData<List<Type>>()

    var message: String? = null
    var status = MutableLiveData<Boolean?>(null)


    fun getTypes() {
        viewModelScope.launch {
            types.postValue(songRepository.getAllTypes())
        }
    }

    fun addType(type: Type) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = typeRepository.addType(type)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            status.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun updateType(type: Type) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = typeRepository.updateType(type)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            status.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }

    fun deleteType(type: Type) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            val result = typeRepository.deleteType(type)
            if (result is NetworkResult.Success) {
                message = result.body.message
            } else if (result is NetworkResult.Error) {
                message = result.responseError.message
            }
            status.postValue(result is NetworkResult.Success)
        }
        registerEventParentJobFinish()
    }
}
