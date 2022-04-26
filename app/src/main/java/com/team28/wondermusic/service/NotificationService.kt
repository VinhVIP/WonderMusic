package com.team28.wondermusic.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team28.wondermusic.CustomApplication.Companion.CHANNEL_ID
import com.team28.wondermusic.R
import com.team28.wondermusic.ui.home.HomeActivity

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("vinh", "onMessageReceived")

        sendNotification(message.notification?.title, message.notification?.body)

        message.notification?.let {
            sendNotification(it.title, it.body)
        }

        val title = message.data["title"]
        val content = message.data["content"]
        sendNotification(title, content)
    }

    private fun sendNotification(title: String?, content: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_music)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager: NotificationManager? =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.notify(2, notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("vinh", token)
    }
}