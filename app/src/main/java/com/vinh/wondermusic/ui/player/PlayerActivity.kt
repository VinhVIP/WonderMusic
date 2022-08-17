package com.vinh.wondermusic.ui.player

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.slider.Slider
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.EventBusModel.*
import com.vinh.wondermusic.adapter.ViewPagerAdapter
import com.vinh.wondermusic.base.activities.BaseActivity
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.data.models.toTimeFormat
import com.vinh.wondermusic.databinding.ActivityPlayerBinding
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.comment.CommentActivity
import com.vinh.wondermusic.ui.menubottom.MenuBottomFragment
import com.vinh.wondermusic.ui.player.songinfo.SongInfoFragment
import com.vinh.wondermusic.ui.player.songlyrics.SongLyricsFragment
import com.vinh.wondermusic.ui.player.songmain.SongMainFragment
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class PlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModels<PlayerViewModel>()

    private var isSeekBarPressed = false

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        binding.sliderSong.valueTo = 1_000_000_000f

        if (!isOnline()) {
            showErrorDialog("Không có kết nối internet")
        }
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
        event.song?.let { song ->
            Log.d("vinh", "recieve song: ${song.name}")
            viewModel.song.postValue(song)

            binding.toolbar.apply {
                tvSongName.text = song.name
                tvSingerName.text = song.singersToString()

                btnMore.setOnClickListener {
                    MenuBottomFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(Constants.Song, viewModel.song.value)
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
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onSongList(event: SongListEvent) {
        Log.d("vinh", "recieve list: ${event.songList.size}")

        viewModel.songList.postValue(event.songList)
        viewModel.songListOrigin.postValue(event.songList)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMusicTime(timeEvent: MusicTimeEvent) {
        binding.tvSongDuration.text = (timeEvent.duration / 1000).toInt().toTimeFormat()

        // Update slider
        if (!isSeekBarPressed) binding.sliderSong.value = timeEvent.time.toFloat()
        binding.sliderSong.valueTo = timeEvent.duration.toFloat()

        viewModel.currentSongTime.postValue(timeEvent.time.toInt())
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onMusicPlaying(event: MusicPlayingEvent) {
        Log.d("vinh", "receive isPlaying: ${event.isPlaying}")
        viewModel.isPlaying.postValue(event.isPlaying)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
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
                startActivity(Intent(this@PlayerActivity, CommentActivity::class.java).apply {
                    putExtra(Constants.Song, viewModel.song.value!!)
                })
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

        getSettings()
    }

    private fun getSettings() {
        viewModel.getShuffle()

        binding.btnShuffle.setOnClickListener {
            val value = viewModel.isShuffle.value ?: false
            viewModel.setShuffle(!value)
        }

        binding.btnRepeat.setOnClickListener {
            val value = viewModel.isRepeat.value ?: false
            viewModel.setRepeat(!value)
        }

        viewModel.isShuffle.observe(this) {
            if (it) {
                binding.btnShuffle.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.icon_tint_highlight
                    )
                )
            } else {
                binding.btnShuffle.setColorFilter(ContextCompat.getColor(this, R.color.icon_tint))

            }
        }

        viewModel.isRepeat.observe(this) {
            if (it) {
                binding.btnRepeat.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.icon_tint_highlight
                    )
                )
            } else {
                binding.btnRepeat.setColorFilter(ContextCompat.getColor(this, R.color.icon_tint))

            }
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
            (value / 1000).toInt().toTimeFormat()
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
                EventBus.getDefault().post(MusicTimeSeekEvent(slider.value.toLong()))
                viewModel.currentSongTime.postValue(slider.value.toInt())
            }
        })
    }

}