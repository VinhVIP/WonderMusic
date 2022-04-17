package com.team28.wondermusic.ui.account.changepassword

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.team28.wondermusic.common.Helper.showHidePassword
import com.team28.wondermusic.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : DialogFragment() {

    private lateinit var binding: FragmentChangePasswordBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPasswordText()

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