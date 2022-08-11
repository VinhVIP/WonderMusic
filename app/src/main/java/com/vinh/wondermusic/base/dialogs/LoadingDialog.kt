package com.vinh.wondermusic.base.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vinh.wondermusic.databinding.DialogLoadingBinding

class LoadingDialog(
    private val loadingTitle: String,
) : DialogFragment() {

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isCancelable = false
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogLoadingBinding.inflate(inflater, container, false)

        binding.tvLoadingTitle.text = loadingTitle

        return binding.root
    }

    fun closeDialog(){
        dismiss()
    }

}