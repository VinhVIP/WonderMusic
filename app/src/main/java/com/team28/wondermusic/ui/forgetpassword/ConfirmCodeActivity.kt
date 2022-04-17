package com.team28.wondermusic.ui.forgetpassword

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.team28.wondermusic.databinding.ActivityConfirmCodeBinding

class ConfirmCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConfirmCode.setOnClickListener {
            startActivity(Intent(this, NewPasswordActivity::class.java))
        }
    }
}