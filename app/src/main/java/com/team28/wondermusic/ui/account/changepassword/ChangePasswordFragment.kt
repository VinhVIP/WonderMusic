package com.team28.wondermusic.ui.account.changepassword

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.common.Helper.showHidePassword
import com.team28.wondermusic.data.models.ChangePasswordModal
import com.team28.wondermusic.databinding.FragmentChangePasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : DialogFragment() {

    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel by viewModels<ChangePasswordViewModel>()

    private var isCurPassValid = false
    private var isNewPassValid = false
    private var isConfirmPassValid = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        setupPasswordText()
        textWatchers()
        observers()

        binding.btnChangePassword.setOnClickListener {
            val modal = getModal()
            modal?.let { viewModel.changePassword(it) }
        }

        return binding.root
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            binding.btnChangePassword.isEnabled = !it
            isCancelable = !it
        }

        viewModel.status.observe(this) {
            it?.let {
                if (it) {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
                viewModel.status.postValue(null)
            }
        }
    }

    private fun getModal(): ChangePasswordModal? {
        if (isCurPassValid && isNewPassValid && isConfirmPassValid) {
            return ChangePasswordModal(
                binding.edCurrentPass.text.toString().trim(),
                binding.edNewPass.text.toString().trim()
            )
        }
        return null
    }

    private fun textWatchers() {
        validateCurPass()
        validateNewPass()
        validateConfirmPass()
    }

    private fun validateCurPass() {
        binding.edCurrentPass.addTextChangedListener {
            it?.let {
                binding.tvCurPassError.apply {
                    isCurPassValid = false
                    when {
                        it.isEmpty() -> {
                            text = "Mật khẩu hiện tại không được bỏ trống!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isCurPassValid = true
                            visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun validateNewPass() {
        binding.edNewPass.addTextChangedListener {
            it?.let {
                binding.tvNewPassError.apply {
                    isNewPassValid = false
                    when {
                        it.toString().trim().length < 6 -> {
                            text = "Mật khẩu mới phái có ít nhất 6 kí tự!"
                            visibility = View.VISIBLE
                        }
                        else -> {
                            isNewPassValid = true
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
                        it.toString() != binding.edNewPass.text.trim().toString() -> {
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

    private fun setupPasswordText() {
        binding.btnCurPassVisible.setOnClickListener {
            showHidePassword(binding.edCurrentPass, binding.btnCurPassVisible)
        }
        binding.btnNewPassVisible.setOnClickListener {
            showHidePassword(binding.edNewPass, binding.btnNewPassVisible)
        }
        binding.btnConfirmPassVisible.setOnClickListener {
            showHidePassword(binding.edConfirmPass, binding.btnConfirmPassVisible)
        }
    }

}