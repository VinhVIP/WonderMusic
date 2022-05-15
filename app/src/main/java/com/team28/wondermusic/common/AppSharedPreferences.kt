package com.team28.wondermusic.common

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.beust.klaxon.Klaxon
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.LoginModal
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreferences @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        const val APP_SHARE_KEY = "com.team28.wondermusic"
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val EMAIL = "EMAIL"
        const val PASSWORD = "PASSWORD"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val USER = "USER"
        const val SHUFFLE = "SHUFFLE"
        const val REPEAT = "REPEAT"
    }

    private var pref: SharedPreferences =
        context.getSharedPreferences(APP_SHARE_KEY, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = pref.edit()

    fun saveLogin(email: String, password: String) {
        editor.putString(EMAIL, email)
        editor.putString(PASSWORD, password)

        editor.putBoolean(IS_LOGGED_IN, true)
        editor.commit()
    }

    fun getLoginModal(): LoginModal? {
        val email = pref.getString(EMAIL, null)
        val password = pref.getString(PASSWORD, null)

        if (email == null || password == null) {
            return null
        }
        Log.d("vinh","$email $password")
        return LoginModal(email, password)
    }

    fun logOut() {
        editor.putBoolean(IS_LOGGED_IN, false)
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGGED_IN, false)
    }

    fun saveUser(user: Account) {
        editor.putString(USER, Klaxon().toJsonString(user)).apply()
    }

    fun getUser(): Account? {
        val json = pref.getString(USER, null)
        json?.let {
            return Klaxon().parse<Account>(it)
        }
        return null
    }

    fun saveAccessToken(token: String) {
        editor.putString(ACCESS_TOKEN, token)
        editor.commit()
    }

    fun getAccessToken(): String {
        return pref.getString(ACCESS_TOKEN, null) ?: ""
    }

    fun setShuffle(isShuffle: Boolean) {
        editor.putBoolean(SHUFFLE, isShuffle)
        editor.commit()
    }

    fun isShuffle(): Boolean {
        return pref.getBoolean(SHUFFLE, false)
    }

    fun setRepeat(isRepeat: Boolean) {
        editor.putBoolean(REPEAT, isRepeat)
        editor.commit()
    }

    fun isRepeat(): Boolean {
        return pref.getBoolean(REPEAT, false)
    }

}