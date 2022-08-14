package com.vinh.wondermusic.service

import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.exoplayer.trackselection.AdaptiveTrackSelection
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.trackselection.TrackSelection
import androidx.media3.exoplayer.upstream.BandwidthMeter
import androidx.media3.exoplayer.upstream.DefaultBandwidthMeter
import androidx.media3.extractor.DefaultExtractorsFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mp3.Mp3Extractor
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.vinh.wondermusic.CustomApplication
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.EventBusModel.*
import com.vinh.wondermusic.broadcast.MusicBroadcast
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.ui.player.PlayerActivity
import com.vinh.wondermusic.widget.MusicWidget
import com.vinh.wondermusic.widget.SongWidget
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Throwable
import kotlin.Boolean
import kotlin.Exception
import kotlin.Int
import kotlin.OptIn
import kotlin.also
import kotlin.apply
import kotlin.let
import kotlin.run


class MusicService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1803

        const val INTENT_ACTION = "com.wonder.music"

        const val ACTION_PLAY = 12
        const val ACTION_PREV = 13
        const val ACTION_NEXT = 14
        const val ACTION_CLEAR = 15
        const val ACTION_DO_NOTHING = 16
        const val ACTION_ADD_SONG_NEXT = 21
        const val ACTION_ADD_SONG_TAIL = 22
    }

    private var player: ExoPlayer? = null

    private var jobTime: Job? = null
    private var songList: ArrayList<Song> = arrayListOf()

    private var currentSong: Song? = null
    private var currentSongIndex: Int = 0

    private lateinit var defaultBitmap: Bitmap

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this)
        defaultBitmap = BitmapFactory.decodeResource(resources, R.drawable.bitmap_music)

        val notification =
            NotificationCompat.Builder(this@MusicService, CustomApplication.CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_small_music)
                .setAutoCancel(false)
                .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("vinh", "onStartCommand")
        val action = intent.getIntExtra(Constants.Action, 0)
        val data = intent.getBundleExtra(Constants.Data)

        data?.let { data ->
            val song: Song? = data.getParcelable(Constants.Song)
            song?.let {
                if (action == ACTION_ADD_SONG_NEXT) {
                    addSongNext(it)
                    return START_NOT_STICKY
                } else if (action == ACTION_ADD_SONG_TAIL) {
                    addSongTail(it)
                    return START_NOT_STICKY
                }
            }
        }


        data?.let { data ->
            val song: Song? = data.getParcelable(Constants.Song)
            val songs: ArrayList<Song>? = data.getParcelableArrayList(Constants.SongList)

            songs?.let {
                songList = songs

                if (DataLocal.IS_SHUFFLE) {
                    Log.d("vinhdm", "shuffle")
                    songList = it.shuffled() as ArrayList<Song>
                }

                // Khi cập nhật danh sách phát thì phải lưu lại vị trí của bài hát đang phát hiện tại
                // trong danh sách mới được cập nhật
                if (song == null) {
                    currentSong?.let {
                        currentSongIndex = indexSong(it, songList)
                        Log.d("vinh", "new song index: $currentSongIndex")
                    }
                }
            }

            song?.let {
                currentSongIndex = indexSong(it, songList)
                Log.d("vinh", "song index: $currentSongIndex")
                changeMusic(currentSongIndex)
            }
        }

        when (intent.getIntExtra(Constants.Action, 0)) {
            ACTION_PLAY -> {
                playPauseMusic()
            }
            ACTION_PREV -> {
                prev()
            }
            ACTION_NEXT -> {
                next()
            }
            ACTION_CLEAR -> {
                sendSongInfoToWidget(null)
                EventBus.getDefault().postSticky(ClearMusic())
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun sendSongInfoToWidget(song: SongWidget?) {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val ids = appWidgetManager.getAppWidgetIds(ComponentName(this, MusicWidget::class.java))

        ids?.let {
            for (appWidgetId in it) {
                MusicWidget.updateAppWidget(this, appWidgetManager, appWidgetId, song)
            }
        }
    }

    private fun indexSong(song: Song, list: ArrayList<Song>): Int {
        for (i in 0 until list.size) {
            if (list[i].idSong == song.idSong) return i
        }
        return -1;
    }

    private fun addSongNext(song: Song) {
        val index = indexSong(song, songList)
        if (index == -1) {
            if (currentSongIndex == songList.size - 1) {
                songList.add(song)
            } else {
                songList.add(currentSongIndex + 1, song)
            }
            EventBus.getDefault().postSticky(SongListEvent(songList))
        }
    }

    private fun addSongTail(song: Song) {
        val index = indexSong(song, songList)
        if (index == -1) {
            songList.add(song)

            EventBus.getDefault().postSticky(SongListEvent(songList))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun timeSend() {
        jobTime?.cancel()

        jobTime = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                player?.let { player ->
                    if (player.currentPosition / 1000 >= player.duration / 1000) {
//                        next()
                    } else {
                        EventBus.getDefault()
                            .postSticky(
                                MusicTimeEvent(
                                    player.currentPosition,
                                    player.duration,
                                )
                            )
                    }
                }
                delay(200)
            }
        }

        jobTime?.start()
    }

    private fun preparePlay(song: Song) {
        try {
            player = ExoPlayer.Builder(this)
                .setMediaSourceFactory(
                    DefaultMediaSourceFactory(this@MusicService)
//                        .setLiveTargetOffsetMs(1000)
//                        .setLiveMinOffsetMs(500)
//                        .setLiveMaxOffsetMs(300000)
                ).build().also {
                    var mediaItem = MediaItem.fromUri(song.link)
//                    if (song.songFile != null) {
//                        if (song.songFile!!.exists()) {
//                            Log.d("vinhmus", "${song.songFile?.path}")
//                            mediaItem = MediaItem.fromUri(song.songFile!!.toUri())
//                        }
//                    }

                    val dataSourceFactory: DataSource.Factory =
                        DefaultDataSource.Factory(this)
                    val extractorsFactory =
                        DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)
                    val progressiveMediaSource =
                        ProgressiveMediaSource.Factory(
                            dataSourceFactory,
                            extractorsFactory
                        )
                            .createMediaSource(mediaItem)
                    it.setMediaSource(progressiveMediaSource)
                    it.prepare()

                    it.play()

                    Log.d("vnhtime", "send init")
                    sendInit(it)
                }

            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    EventBus.getDefault().post(MusicPlayingEvent(isPlaying))
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Can't play this song", Toast.LENGTH_SHORT).show()
            stopSelf()
        }

    }

    private fun sendInit(player: ExoPlayer) {
        Log.d("vinhtime", "send init ne")
        EventBus.getDefault().postSticky(MusicPlayingEvent(true))
        timeSend()
        sendNotification()
        EventBus.getDefault().postSticky(AudioSessionIdEvent(player.audioSessionId))

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun seekTime(event: MusicTimeSeekEvent) {
        Log.d("vinh", "Seek to ${event.timeMillis}")
        player?.seekTo(event.timeMillis.toLong())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRequestSongInfo(event: RequestSongEvent) {
        if (currentSongIndex != -1 && currentSongIndex < songList.size) {
            EventBus.getDefault().postSticky(SongInfoEvent(songList[currentSongIndex]))
            EventBus.getDefault().postSticky(SongListEvent(songList))

            player?.let {
                EventBus.getDefault().postSticky(MusicPlayingEvent(it.isPlaying))
            }
        }
    }

    private fun playPauseMusic() {
        player?.let {
            if (it.isPlaying) it.pause()
            else it.play()

            EventBus.getDefault().postSticky(MusicPlayingEvent(it.isPlaying))
        }

        sendNotification()
    }

    private fun prev() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            changeMusic(currentSongIndex)
        }
    }

    private fun next() {
        if (currentSongIndex + 1 < songList.size) {
            currentSongIndex++
            changeMusic(currentSongIndex)
        } else {
            if (DataLocal.IS_REPEAT) {
                currentSongIndex = 0
                changeMusic(currentSongIndex)
            } else {
                Log.d("vinh", "dung lai dc roi")
                player?.pause()
                EventBus.getDefault().postSticky(MusicPlayingEvent(false))
                sendNotification()
            }
        }
    }

    private fun changeMusic(index: Int) {
        Log.d("vinh", "change music")
        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }

        val song = songList[index]
        currentSong = song

        EventBus.getDefault().postSticky(SongInfoEvent(song))

        preparePlay(song)
        sendNotification()
    }

    override fun onDestroy() {
        super.onDestroy()

        player?.release()
        player = null

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.cancel(NOTIFICATION_ID)

        jobTime?.cancel()

        Log.d("VINHMUSIC", "destroy music")
        EventBus.getDefault().postSticky(SongInfoEvent(null))
        EventBus.getDefault().unregister(this)
    }

    private fun getPendingIntent(context: Context, action: Int): PendingIntent {
        val intent = Intent(this, MusicBroadcast::class.java)
        intent.putExtra(INTENT_ACTION, action)
        return PendingIntent.getBroadcast(
            context.applicationContext,
            action,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun sendNotification() {
        GlobalScope.launch(Dispatchers.Main) {
            val loader = ImageLoader(this@MusicService)
            val request = ImageRequest.Builder(this@MusicService)
                .data(songList[currentSongIndex].image)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            var bitmap: Bitmap =
                BitmapFactory.decodeResource(applicationContext.resources, R.drawable.icon)
            try {
                val result = (loader.execute(request) as SuccessResult).drawable
                bitmap = (result as BitmapDrawable).bitmap
            } catch (e: Exception) {
            }

            // Send song info to app widget
            sendSongInfoToWidget(SongWidget(currentSong!!, bitmap, player?.isPlaying ?: false))

            player?.let { mediaPlayer ->
                val song = songList[currentSongIndex]

                val resultIntent = Intent(this@MusicService, PlayerActivity::class.java)

                val resultPendingIntent: PendingIntent? =
                    TaskStackBuilder.create(this@MusicService).run {
                        addNextIntentWithParentStack(resultIntent)
                        getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                        )
                    }

//                val mediaSessionCompat = setUpMedia()

                val notification =
                    NotificationCompat.Builder(this@MusicService, CustomApplication.CHANNEL_ID)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_small_music)
                        .setContentIntent(resultPendingIntent)
                        .addAction(
                            R.drawable.ic_baseline_skip_previous_24,
                            "Prev",
                            getPendingIntent(this@MusicService, ACTION_PREV)
                        )
                        .addAction(
                            if (mediaPlayer.isPlaying) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24,
                            "Play",
                            getPendingIntent(this@MusicService, ACTION_PLAY)
                        )
                        .addAction(
                            R.drawable.ic_baseline_skip_next_24,
                            "Next",
                            getPendingIntent(this@MusicService, ACTION_NEXT)
                        )
                        .addAction(
                            R.drawable.ic_baseline_clear_24,
                            "Clear",
                            getPendingIntent(this@MusicService, ACTION_CLEAR)
                        )
                        .setStyle(
                            androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
//                                .setMediaSession(mediaSessionCompat.sessionToken)
                        )
                        .setProgress(
                            mediaPlayer.duration.toInt(),
                            mediaPlayer.currentPosition.toInt(),
                            false
                        )
                        .setContentTitle(song.name)
                        .setContentText(song.singersToString())
                        .setLargeIcon(bitmap)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .build()

                startForeground(NOTIFICATION_ID, notification)
            }
        }

    }

    private fun setUpMedia(): MediaSessionCompat {
        return MediaSessionCompat(this, "tag").apply {
            setMetadata(
                MediaMetadataCompat.Builder()
//                    .putLong(MediaMetadata.METADATA_KEY_DURATION, player!!.duration.toLong())
                    .build()
            )
        }

    }
}