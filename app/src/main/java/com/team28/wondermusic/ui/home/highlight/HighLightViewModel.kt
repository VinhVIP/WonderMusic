package com.team28.wondermusic.ui.home.highlight

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.team28.wondermusic.base.viewmodels.BaseViewModel
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.common.resize
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HighLightViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    var topSongs = MutableLiveData<List<Song>>()
    var songDrawables = MutableLiveData<ArrayList<Drawable?>>()

    fun fetchData() {
        parentJob = viewModelScope.launch {
            topSongs.postValue(songRepository.getListTopSongs())
        }
        registerEventParentJobFinish()
    }

    fun genData() {
        viewModelScope.launch {
            songRepository.saveListTopSongs(
                listOf(
                    TempData.songs[0],
                    TempData.songs[1],
                    TempData.songs[2],
                )
            )
            fetchData()
        }
    }

    fun getTopSongDrawable(context: Context) {
        viewModelScope.launch {
            val loader = ImageLoader(context)
            val list = ArrayList<Drawable?>()

            for (i in 0 until (topSongs.value?.size ?: 0)) {
                val request = ImageRequest.Builder(context)
                    .data(topSongs.value?.get(i)?.image)
                    .allowHardware(false)
                    .build()
                try {
                    val result =
                        (loader.execute(request) as SuccessResult)
                            .drawable
                            .resize(
                                context,
                                Helper.dpToPixel(48f, context),
                                Helper.dpToPixel(48f, context)
                            )
                    list.add(result)
                } catch (e: Exception) {
                    list.add(null)
                }
            }
            songDrawables.postValue(list)
        }
    }
}