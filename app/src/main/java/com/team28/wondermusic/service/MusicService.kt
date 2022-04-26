package com.team28.wondermusic.service

import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.MediaMetadata
import android.media.MediaPlayer
import android.media.metrics.Event
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.team28.wondermusic.CustomApplication
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.EventBusModel.*
import com.team28.wondermusic.broadcast.MusicBroadcast
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.ui.player.PlayerActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MusicService : Service(), MediaPlayer.OnPreparedListener {

    companion object {
        const val NOTIFICATION_ID = 1803

        const val INTENT_ACTION = "com.wonder.music"

        const val ACTION_PLAY = 12
        const val ACTION_PREV = 13
        const val ACTION_NEXT = 14
        const val ACTION_CLEAR = 15
        const val ACTION_DO_NOTHING = 16
    }

    private var mediaPlayer: MediaPlayer? = null
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
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("vinh", "onStartCommand")

        val data = intent.getBundleExtra(Constants.Data)
        data?.let { data ->
            val song: Song? = data.getParcelable(Constants.Song)
            val songs: ArrayList<Song>? = data.getParcelableArrayList(Constants.SongList)

            songs?.let {
                songList = songs

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
                EventBus.getDefault().postSticky(ClearMusic())
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun indexSong(song: Song, list: ArrayList<Song>): Int {
        for (i in 0 until list.size) {
            if (list[i].idSong == song.idSong) return i
        }
        return -1;
    }

    private fun timeSend(mediaPlayer: MediaPlayer) {
        if (jobTime?.isActive == true) jobTime?.cancel()

        jobTime = GlobalScope.launch {
            while (true) {
                try {
                    if (mediaPlayer.currentPosition / 1000 >= mediaPlayer.duration / 1000) {
                        next()
                    } else {
                        EventBus.getDefault()
                            .postSticky(MusicTimeEvent(mediaPlayer.currentPosition))
                    }
                    delay(200)
                } catch (e: Exception) {
                    break
                }
            }
        }
    }

    private fun prepareMediaURL(url: String): MediaPlayer {
        Log.d("vinh", url)

        return MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            setDataSource(url)
            setOnPreparedListener(this@MusicService)
            prepareAsync()
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun seekTime(event: MusicTimeSeekEvent) {
        Log.d("vinh", "Seek to ${event.timeMillis}")
        mediaPlayer?.seekTo(event.timeMillis)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRequestSongInfo(event: RequestSongEvent) {
        if (currentSongIndex != -1 && currentSongIndex < songList.size) {
            EventBus.getDefault().postSticky(SongInfoEvent(songList[currentSongIndex]))
            EventBus.getDefault().postSticky(SongListEvent(songList))

            mediaPlayer?.let {
                EventBus.getDefault().postSticky((MusicDurationEvent(it.duration / 1000)))
                EventBus.getDefault().postSticky(MusicPlayingEvent(it.isPlaying))
            }
        }
    }

    private fun playPauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) it.pause()
            else it.start()

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
            Log.d("vinh", "dung lai dc roi")
            mediaPlayer?.pause()
            EventBus.getDefault().postSticky(MusicPlayingEvent(false))
            sendNotification()
        }
    }

    private fun changeMusic(index: Int) {
        Log.d("vinh", "change music")
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }

        val song = songList[index]
        currentSong = song

        EventBus.getDefault().postSticky(SongInfoEvent(song))

        mediaPlayer = prepareMediaURL(song.link)
        sendNotification()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null

        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.cancel(NOTIFICATION_ID)

        jobTime?.cancel()

        Log.d("VINHMUSIC", "destroy music")
        EventBus.getDefault().unregister(this)
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        Log.d("VINHMUSIC", "prepared done!!!")
        mediaPlayer.start()
        EventBus.getDefault().postSticky(MusicPlayingEvent(true))

        EventBus.getDefault().postSticky(MusicDurationEvent(mediaPlayer.duration / 1000))
        timeSend(mediaPlayer)

        sendNotification()

        EventBus.getDefault().postSticky(AudioSessionIdEvent(mediaPlayer.audioSessionId))
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

    private fun sendNotification() {
        GlobalScope.launch {
            var bitmap: Bitmap
            val loader = ImageLoader(this@MusicService)
            val request = ImageRequest.Builder(this@MusicService)
                .data(songList[currentSongIndex].image)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            bitmap = try {
                val result = (loader.execute(request) as SuccessResult).drawable
                (result as BitmapDrawable).bitmap
            } catch (e: Exception) {
                BitmapFactory.decodeResource(resources, R.drawable.icon_music)
            }

            mediaPlayer?.let { mediaPlayer ->
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

                val mediaSessionCompat = setUpMedia()

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
                                .setMediaSession(mediaSessionCompat.sessionToken)
                        )
                        .setProgress(mediaPlayer.duration, mediaPlayer.currentPosition, false)
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
                    .putLong(MediaMetadata.METADATA_KEY_DURATION, mediaPlayer!!.duration.toLong())
                    .build()
            )
        }

    }
}