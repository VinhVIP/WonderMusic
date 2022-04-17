package com.team28.wondermusic.ui.signup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.team28.wondermusic.R
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_shapes)


        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }
}