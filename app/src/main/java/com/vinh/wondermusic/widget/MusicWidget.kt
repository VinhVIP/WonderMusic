package com.vinh.wondermusic.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import com.vinh.wondermusic.R
import com.vinh.wondermusic.broadcast.MusicBroadcast
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.player.PlayerActivity
import com.vinh.wondermusic.ui.splash.SplashActivity


class MusicWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    companion object {
        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            song: SongWidget? = null
        ) {
            var views: RemoteViews

            if (song == null) {
                views = RemoteViews(context.packageName, R.layout.music_widget_empty)

                val intent = Intent(context, SplashActivity::class.java)
                val playerPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.layoutWidget, playerPendingIntent)

            } else {
                views = RemoteViews(context.packageName, R.layout.music_widget)

                song.let {
                    views.setTextViewText(R.id.tvSongName, it.song.name)
                    views.setTextViewText(R.id.tvSongSingers, it.song.singersToString())
                    views.setImageViewBitmap(R.id.imgSongAvatar, it.bitmap)
                    views.setImageViewResource(
                        R.id.btnPlay,
                        if (it.isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    )
                }

                val intent = Intent(context, PlayerActivity::class.java)
                val playerPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.layoutWidget, playerPendingIntent)

                // Button Action
                views.setOnClickPendingIntent(
                    R.id.btnPrev,
                    getPendingIntent(context, MusicService.ACTION_PREV)
                )
                views.setOnClickPendingIntent(
                    R.id.btnPlay,
                    getPendingIntent(context, MusicService.ACTION_PLAY)
                )
                views.setOnClickPendingIntent(
                    R.id.btnNext,
                    getPendingIntent(context, MusicService.ACTION_NEXT)
                )
            }


            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, action: Int): PendingIntent {
            val intent = Intent(context, MusicBroadcast::class.java)
            intent.putExtra(MusicService.INTENT_ACTION, action)
            return PendingIntent.getBroadcast(
                context.applicationContext,
                action,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}

data class SongWidget(val song: Song, val bitmap: Bitmap, val isPlaying: Boolean)
