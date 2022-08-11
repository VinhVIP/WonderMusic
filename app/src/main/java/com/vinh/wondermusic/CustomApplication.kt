package com.vinh.wondermusic

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.vinh.wondermusic.common.DataLocal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CustomApplication : Application() {
    companion object {
        const val CHANNEL_ID = "MUSIC_CHANNEL"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        getExternalFilesDir("")?.let {
            DataLocal.externalFileFir = it.path
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Channel music",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setSound(null, null)
            channel.enableVibration(false)

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}