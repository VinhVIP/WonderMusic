package com.vinh.wondermusic.ui.type

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypeViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    val songsOfType = MutableLiveData<MutableList<Song>>()
    var hasMore = true

    fun getSongsOfType(type: Type) {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            if (hasMore) {
                var nextPage = 1;
                songsOfType.value?.let {
                    nextPage = it.size.div(Constants.SIZE_PER_PAGE) + 1
                }

                Log.d("vinh", "get songs of type: page = $nextPage")


                val list = songRepository.getSongsOfType(type.idType, nextPage)
                if (list.size < Constants.SIZE_PER_PAGE) hasMore = false

                val currentList = songsOfType.value ?: mutableListOf()
                currentList.addAll(list)

                songsOfType.postValue(currentList)
            }
        }
        registerEventParentJobFinish()
    }
}