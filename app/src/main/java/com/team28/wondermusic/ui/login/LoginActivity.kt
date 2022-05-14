package com.team28.wondermusic.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.team28.wondermusic.R
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.LoginModal
import com.team28.wondermusic.databinding.ActivityLoginBinding
import com.team28.wondermusic.ui.forgetpassword.ForgetPasswordActivity
import com.team28.wondermusic.ui.home.HomeActivity
import com.team28.wondermusic.ui.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_moon)

        viewModel.isLoading.observe(this) {
            binding.btnLogin.isEnabled = !it
        }

        viewModel.isLoginSuccess.observe(this) {
            it?.let {
                if (it) {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    showErrorDialog(viewModel.message!!)
                }
                viewModel.isLoginSuccess.postValue(null)
            }
        }

        binding.btnLogin.setOnClickListener {
            val modal = getLoginModal()
            modal?.let { viewModel.login(it) }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnForgotPass.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java).apply {
                putExtra(Constants.Email, binding.edEmail.text.toString().trim())
            })
        }
    }

    override fun onStart() {
        super.onStart()
        val modal = viewModel.getSavedLoginModal()
        modal?.let {
            binding.edEmail.setText(it.email)
            binding.edPassword.setText(it.password)
        }
    }

    private fun getLoginModal(): LoginModal? {
        if (binding.edEmail.text.toString().isEmpty()) {
            showErrorDialog("Email không được bỏ trống")
            return null
        }
        if (binding.edPassword.text.toString().isEmpty()) {
            showErrorDialog("Mật khẩu không được bỏ trống")
            return null
        }
        return LoginModal(
            binding.edEmail.text.toString().trim(),
            binding.edPassword.text.toString()
        )
    }
}