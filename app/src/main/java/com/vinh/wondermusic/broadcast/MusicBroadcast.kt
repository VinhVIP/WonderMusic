package com.vinh.wondermusic.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.vinh.wondermusic.service.MusicService

class MusicBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("VINHMUSIC", "onReceive")

        val actionMusic = intent.getIntExtra(MusicService.INTENT_ACTION, -1)
        val intentService = Intent(context, MusicService::class.java)
        intentService.putExtra("action", actionMusic)
        context.startService(intentService)
    }
}