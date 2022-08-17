package com.vinh.wondermusic.ui.home.highlight

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.vinh.wondermusic.base.viewmodels.BaseViewModel
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.common.resize
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.SongListen
import com.vinh.wondermusic.data.repositories.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class HighLightViewModel @Inject constructor(
    private val songRepository: SongRepository
) : BaseViewModel() {

    var message = MutableLiveData<String?>(null)

    var topSongs = MutableLiveData<List<Song>>()
    var topSongsChart = MutableLiveData<List<SongListen>>()
    var songDrawables = MutableLiveData<ArrayList<Drawable?>>()

    fun fetchData() {
        parentJob = viewModelScope.launch {
            val songs = songRepository.getTop100Songs()
            topSongs.postValue(songs)
            topSongsChart.postValue(songRepository.getTop3Songs())
        }
        registerEventParentJobFinish()
    }

    fun updateImagePath(song: Song) {
        viewModelScope.launch {
            songRepository.updateSongImagePath(song)
        }
    }

    fun saveSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(song.link)
                val input: InputStream = url.openStream()
                input.use { input ->
                    val fileLocal = File(
                        DataLocal.externalFileFir,
                        File.separator + song.idSong + ".mp3"
                    )
                    val output = FileOutputStream(fileLocal)
                    output.use { output ->
                        val buffer = ByteArray(1024)
                        var bytesRead = 0
                        while (input.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                            output.write(buffer, 0, bytesRead)
                        }

                        song.songFile = fileLocal
                        songRepository.updateSongFilePath(song)
                    }
                }
            } catch (e: Exception) {

            }

        }
    }

    private fun saveImage(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val url = URL(song.image)
                val input: InputStream = url.openStream()
                input.use { input ->
                    val fileLocal = File(
                        DataLocal.externalFileFir,
                        File.separator + song.idSong + ".png"
                    )
                    val output = FileOutputStream(fileLocal)
                    output.use { output ->
                        val buffer = ByteArray(1024)
                        var bytesRead = 0
                        while (input.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                            output.write(buffer, 0, bytesRead)
                        }

                        song.imageFile = fileLocal
                        updateImagePath(song)
                    }
                }
            } catch (e: Exception) {

            }

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