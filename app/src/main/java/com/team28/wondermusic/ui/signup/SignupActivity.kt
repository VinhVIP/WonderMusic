package com.team28.wondermusic.ui.signup

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.team28.wondermusic.R
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.SignupModal
import com.team28.wondermusic.databinding.ActivitySignupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel>()

    private var isEmailValid = false
    private var isAccountNameValid = false
    private var isPassValid = false
    private var isConfirmPassValid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_shapes)


        binding.btnBackToLogin.setOnClickListener {
            finish()
        }

        binding.btnSignup.setOnClickListener {
            val modal = getSignupModal()
            modal?.let { viewModel.signup(it) }
        }

        viewModel.isLoading.observe(this) {
            binding.btnSignup.isEnabled = !it
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    showErrorDialog(viewModel.message ?: "Có lỗi xảy ra")
                }
                viewModel.status.postValue(null)
            }
        }

        textWatchers()
    }

    private fun getSignupModal(): SignupModal? {
        if (isEmailValid && isAccountNameValid && isPassValid && isConfirmPassValid) {
            return SignupModal(
                binding.edEmail.text.toString().trim(),
                binding.edUser.text.toString().trim(),
                binding.edPass.text.toString().trim(),
                binding.edConfirmPass.text.toString().trim()
            )
        }
        return null

    }

    private fun textWatchers() {
        validateEmail()
        validateAccountName()
        validatePass()
        validateConfirmPass()
    }

    private fun validateEmail() {
        binding.edEmail.addTextChangedListener {
            it?.let {
                binding.tvEmailError.apply {
                    isEmailValid = false
                    when {
                        it.toString().isEmpty() -> {
                            text = "Email không được bỏ trống!"
                            visibility = View.VISIBLE
                        }
                        !Helper.validateEmail(it.toString()) -> {
                            text = "Định dạng email không hợp lệ!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isEmailValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun validateAccountName() {
        binding.edUser.addTextChangedListener {
            it?.let {
                binding.tvAccountNameError.apply {
                    isAccountNameValid = false
                    when {
                        it.toString().isEmpty() -> {
                            text = "Tên tài khoản không được bỏ trống!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isAccountNameValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun validatePass() {
        binding.edPass.addTextChangedListener {
            it?.let {
                binding.tvPassError.apply {
                    isPassValid = false
                    when {
                        it.toString().length < 6 -> {
                            text = "Mật khẩu phái có ít nhất 6 kí tự!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isPassValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun validateConfirmPass() {
        binding.edConfirmPass.addTextChangedListener {
            it?.let {
                binding.tvConfirmPassError.apply {
                    isConfirmPassValid = false
                    when {
                        it.toString().isEmpty() -> {
                            text = "Mật khẩu xác nhận không được bỏ trống!"
                            visibility = View.VISIBLE
                        }
                        it.toString() != binding.edPass.text.toString() -> {
                            text = "Mật khẩu xác nhận không khớp!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isConfirmPassValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


}