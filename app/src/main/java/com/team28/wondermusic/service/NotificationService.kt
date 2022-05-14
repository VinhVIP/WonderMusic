package com.team28.wondermusic.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team28.wondermusic.CustomApplication.Companion.CHANNEL_ID
import com.team28.wondermusic.R
import com.team28.wondermusic.activities.TricksActivity
import com.team28.wondermusic.common.AppSharedPreferences
import com.team28.wondermusic.common.Constants

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("vinh", "onMessageReceived")

        var pref: SharedPreferences =
            applicationContext.getSharedPreferences(
                AppSharedPreferences.APP_SHARE_KEY,
                Context.MODE_PRIVATE
            )
        val isLoggedIn = pref.getBoolean(AppSharedPreferences.IS_LOGGED_IN, false)

        if(isLoggedIn){
            val title = message.data["title"]
            val content = message.data["content"]
            val action = message.data["action"]

            sendNotification(title, content, action)
            Log.d("vinh", "Action: $action")
        }
    }

    private fun sendNotification(title: String?, content: String?, action: String?) {
        val intent = Intent(this, TricksActivity::class.java).apply {
            putExtra(Constants.Action, action)
        }
//        val pendingIntent =
//            PendingIntent.getActivity(
//                this,
//                0,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//            )

        val pendingIntent: PendingIntent? =
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }


        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_small_music)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_music)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
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