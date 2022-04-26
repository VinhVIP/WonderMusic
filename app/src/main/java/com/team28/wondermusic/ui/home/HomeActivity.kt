package com.team28.wondermusic.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.EventBusModel
import com.team28.wondermusic.adapter.EventBusModel.MusicPlayingEvent
import com.team28.wondermusic.adapter.EventBusModel.SongInfoEvent
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityHomeBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.account.AccountActivity
import com.team28.wondermusic.ui.home.discover.DiscoverFragment
import com.team28.wondermusic.ui.home.highlight.HighLightFragment
import com.team28.wondermusic.ui.home.individual.IndividualFragment
import com.team28.wondermusic.ui.notification.NotificationActivity
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val viewModel by viewModels<HomeViewModel>()

    override fun onStart() {
        super.onStart()
        Helper.setStatusBarGradiant(this, R.drawable.bg_main)
        EventBus.getDefault().register(this)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("vinh", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result
            Log.d("vinh", token)
        })
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        setupViewPager()
        setupBottomNavigation()

        binding.playerMini.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }

        binding.toolbar.imgAvatar.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java).apply {
                putExtra(Constants.Account, TempData.myAccount)
            })
        }

        binding.toolbar.imgNotification.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        viewModel.isPlaying.observe(this) {
            binding.btnPlayPause.setImageResource(
                if (it) R.drawable.ic_pause
                else R.drawable.ic_play
            )
        }

        viewModel.song.observe(this) { song ->
            Log.d("vinh", "update song")
            if (song == null) {
                binding.playerMini.visibility = View.INVISIBLE
            } else {
                binding.playerMini.visibility = View.VISIBLE
                binding.tvSongName.text = song.name
                binding.tvSingerName.text = song.singersToString()
                Picasso.get().load(song.image).into(binding.imgSongAvatar)
            }
        }

        binding.btnPlayPause.setOnClickListener {
            Log.d("vinh", "action_play")
            sendMusicAction(MusicService.ACTION_PLAY)
        }

        binding.btnNext.setOnClickListener {
            Log.d("vinh", "action_next")
            sendMusicAction(MusicService.ACTION_NEXT)
        }
    }

    private fun sendMusicAction(
        action: Int,
        song: Song? = null,
        songList: ArrayList<Song> = arrayListOf()
    ) {
        val intent = Intent(applicationContext, MusicService::class.java)

        intent.putExtra("action", action)
        song?.let {
            val bundle = Bundle().apply {
                putParcelable(Constants.Song, it)
                putParcelableArrayList(Constants.SongList, songList)
            }
            intent.putExtra(Constants.Data, bundle)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onSongInfo(event: SongInfoEvent) {
        Log.d("vinh", "home recieve song: ${event.song.name}")
        viewModel.song.postValue(event.song)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onMusicPlaying(event: MusicPlayingEvent) {
        viewModel.isPlaying.postValue(event.isPlaying)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    fun onClearMusic(event: EventBusModel.ClearMusic) {
        viewModel.song.postValue(null)
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMusicTime(timeEvent: EventBusModel.MusicTimeEvent) {
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun onMusicDuration(event: EventBusModel.MusicDurationEvent) {
    }

    override fun onResume() {
        super.onResume()
        binding.btnPlayPause.setImageResource(
            if (viewModel.isPlaying.value!!) R.drawable.ic_pause
            else R.drawable.ic_play
        )
    }

    private fun setupViewPager() {
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val fragmentsList: ArrayList<Fragment> =
            arrayListOf(
                IndividualFragment(),
                HighLightFragment(),
                DiscoverFragment()
            )
        binding.viewPager.adapter = ViewPagerAdapter(fragmentsList, this)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.bottomNav.selectedItemId = items[position]
            }
        })
    }

    var items =
        arrayListOf(R.id.navigation_home, R.id.navigation_highlight, R.id.navigation_follow)

    private fun setupBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener {
            for (i in 0 until items.size) {
                if (items[i] == it.itemId) {
                    binding.viewPager.currentItem = i
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}