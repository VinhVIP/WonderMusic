package com.vinh.wondermusic.ui.home.personal.mysongs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.repositories.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MySongsViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : BaseViewModel() {

    var mySongs = MutableLiveData<List<Song>>()

    fun fetchData() {
        isLoading.postValue(true)
        parentJob = viewModelScope.launch {
            mySongs.postValue(accountRepository.getMySongs())
        }
        registerEventParentJobFinish()
    }

}