package com.team28.wondermusic.ui.player

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.slider.Slider
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.EventBusModel.*
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.database.entities.toTimeFormat
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityPlayerBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.comment.CommentActivity
import com.team28.wondermusic.ui.home.ViewPagerAdapter
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.songinfo.SongInfoFragment
import com.team28.wondermusic.ui.player.songlyrics.SongLyricsFragment
import com.team28.wondermusic.ui.player.songmain.SongMainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModels<PlayerViewModel>()

    private var isSeekBarPressed = false

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        binding.sliderSong.valueTo = 10000f
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onAudioSessionIdEvent(event: AudioSessionIdEvent) {
        Log.d("vinh", "audio session id: ${event.sessionId}")
        viewModel.audioSessionId.postValue(event.sessionId)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onSongInfo(event: SongInfoEvent) {
        val song = event.song
        Log.d("vinh", "recieve song: ${song.name}")
        viewModel.song.postValue(song)

        binding.toolbar.apply {
            tvSongName.text = song.name
            tvSingerName.text = song.singersToString()

            btnMore.setOnClickListener {
                MenuBottomFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Constants.Song, song)
                    }
                }.show(supportFragmentManager, null)
            }
        }

        runOnUiThread {
            binding.tvSongDuration.text = "..."
        }

        // Tăng số lượt nghe bài hát
        viewModel.listen(song)

        viewModel.getSong(song)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onSongList(event: SongListEvent) {
        Log.d("vinh", "recieve list: ${event.songList.size}")

        viewModel.songList.postValue(event.songList)
        viewModel.songListOrigin.postValue(event.songList)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onMusicTime(timeEvent: MusicTimeEvent) {
        if (!isSeekBarPressed) binding.sliderSong.value = (timeEvent.time / 1000).toFloat()
        viewModel.currentSongTime.postValue(timeEvent.time)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onMusicDuration(event: MusicDurationEvent) {
        Log.d("vinh", "recieve duration: ${event.durationSeconds}")
        viewModel.currentRotate = 0f
        if (event.durationSeconds > 0) {
            MainScope().launch {
                with(Dispatchers.Main) {
                    binding.sliderSong.valueTo = event.durationSeconds.toFloat()
                    binding.tvSongDuration.text = event.durationSeconds.toTimeFormat()
                }
            }

        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onMusicPlaying(event: MusicPlayingEvent) {
        Log.d("vinh", "recieve isPlaying: ${event.isPlaying}")
        viewModel.isPlaying.postValue(event.isPlaying)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onClearMusic(event: ClearMusic) {
        viewModel.isClear = true

        viewModel.isPlaying.postValue(false)
        binding.sliderSong.value = 0f
        viewModel.currentSongTime.postValue(0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewPager()
        Helper.setStatusBarGradiant(this, R.drawable.bg_song_player)

        setupSeekBar()
        action()

        binding.toolbar.apply {
            btnShowComment.setOnClickListener {
                startActivity(Intent(this@PlayerActivity, CommentActivity::class.java))
            }
            btnBack.setOnClickListener { super.onBackPressed() }
        }

        EventBus.getDefault().post(RequestSongEvent())

        viewModel.isPlaying.observe(this) {
            binding.btnPlayPause.setImageResource(
                if (it) R.drawable.ic_pause_circle_48
                else R.drawable.ic_play_circle_outline_48
            )
        }

    }

    private fun action() {
        binding.btnPlayPause.setOnClickListener {
            if (viewModel.isClear) {
                sendMusicAction(
                    MusicService.ACTION_PLAY,
                    viewModel.song.value,
                    viewModel.songList.value!!
                )
                viewModel.isClear = false
            } else {
                sendMusicAction(MusicService.ACTION_PLAY)
            }
        }

        binding.btnPrev.setOnClickListener {
            if (viewModel.isClear) {
                sendMusicAction(
                    MusicService.ACTION_PREV,
                    viewModel.song.value,
                    viewModel.songList.value!!
                )
                viewModel.isClear = false
            } else {
                sendMusicAction(MusicService.ACTION_PREV)
            }
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.isClear) {
                sendMusicAction(
                    MusicService.ACTION_NEXT,
                    viewModel.song.value,
                    viewModel.songList.value!!
                )
                viewModel.isClear = false
            } else {
                sendMusicAction(MusicService.ACTION_NEXT)
            }
        }
    }

    private fun sendMusicAction(
        action: Int,
        song: Song? = null,
        songList: ArrayList<Song> = arrayListOf()
    ) {
        val intent = Intent(applicationContext, MusicService::class.java)

        intent.putExtra(Constants.Action, action)
        song?.let {
            val bundle = Bundle().apply {
                putParcelable(Constants.Song, it)
                putParcelableArrayList(Constants.SongList, songList)
            }
            intent.putExtra(Constants.Data, bundle)
        }

        Helper.startMusicService(this, intent)
    }

    private fun setupViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val fragmentsList: ArrayList<Fragment> =
            arrayListOf(
                SongInfoFragment(),
                SongMainFragment(),
                SongLyricsFragment()
            )
        binding.viewPager.adapter = ViewPagerAdapter(fragmentsList, this)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                dotIndicator(position)
            }
        })

        binding.viewPager.currentItem = 1
    }

    private fun dotIndicator(position: Int) {
        binding.dot0.setImageResource(R.drawable.ic_dot_default)
        binding.dot1.setImageResource(R.drawable.ic_dot_default)
        binding.dot2.setImageResource(R.drawable.ic_dot_default)

        when (position) {
            0 -> binding.dot0.setImageResource(R.drawable.ic_dot_selected)
            1 -> binding.dot1.setImageResource(R.drawable.ic_dot_selected)
            2 -> binding.dot2.setImageResource(R.drawable.ic_dot_selected)
        }
    }

    private fun setupSeekBar() {
        binding.sliderSong.setLabelFormatter { value: Float ->
            value.toInt().toTimeFormat()
        }

        binding.sliderSong.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: Slider) {
                isSeekBarPressed = true
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: Slider) {
                isSeekBarPressed = false
                viewModel.isUserTouchedSlider = true
                EventBus.getDefault().post(MusicTimeSeekEvent(slider.value.toInt() * 1000))
                viewModel.currentSongTime.postValue(slider.value.toInt())
            }
        })
    }

}