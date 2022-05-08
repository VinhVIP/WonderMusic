package com.team28.wondermusic.ui.home.personal.mysongs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.AccountRepository
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