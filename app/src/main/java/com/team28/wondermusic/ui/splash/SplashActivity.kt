package com.team28.wondermusic.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.team28.wondermusic.R
import com.team28.wondermusic.common.AppSharedPreferences
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.ui.home.HomeActivity
import com.team28.wondermusic.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(R.layout.activity_splash)

        viewModel.getSavedData()

        if (DataLocal.IS_LOGGED_IN) {
            startActivity(Intent(this, HomeActivity::class.java))
            Log.d("vinh", "logged")
            finish()
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            Log.d("vinh", "no logged")
            finish()
        }

    }
}