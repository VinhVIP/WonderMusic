package com.team28.wondermusic.base.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.team28.wondermusic.R

class ErrorDialog(
    context: Context,
    private val errorContent: String,
    private val textButton: String? = null
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val rootView = layoutInflater.inflate(R.layout.dialog_error, null, false)

        val tvContentError = rootView.findViewById<TextView>(R.id.tvContentError)
        tvContentError.text = errorContent

        val btnOK = rootView.findViewById<Button>(R.id.btnOK)
        textButton?.let {
            btnOK.text = textButton
        }
        btnOK.setOnClickListener {
            dismiss()
        }
        setContentView(rootView)

    }
}