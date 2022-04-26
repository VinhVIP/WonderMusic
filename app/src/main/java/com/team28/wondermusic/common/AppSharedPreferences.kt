package com.team28.wondermusic.common

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppSharedPreference @Inject constructor(private val context: Context) {
    companion object {
        const val APP_SHARE_KEY = "com.team28.wondermusic"
    }

    private fun getSharedPreferences(): SharedPreferences? {
        return context.getSharedPreferences(APP_SHARE_KEY, Context.MODE_PRIVATE)
    }

}