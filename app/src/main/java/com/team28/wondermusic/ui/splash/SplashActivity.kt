package com.team28.wondermusic.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.team28.wondermusic.R
import com.team28.wondermusic.ui.home.HomeActivity
import com.team28.wondermusic.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        setContentView(R.layout.activity_splash)

//        Handler(Looper.getMainLooper()).postDelayed({
//            val mainIntent = Intent(this, LoginActivity::class.java)
//            startActivity(mainIntent)
//            finish()
//        }, 1000)
    }
}