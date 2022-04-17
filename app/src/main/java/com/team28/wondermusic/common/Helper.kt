package com.team28.wondermusic.common

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.InputType
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.team28.wondermusic.R

object Helper {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: AppCompatActivity, bgDrawable: Int) {
        val window: Window = activity.window
        val background = ContextCompat.getDrawable(activity, bgDrawable)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        window.statusBarColor =
            ContextCompat.getColor(activity, android.R.color.transparent)
        window.navigationBarColor =
            ContextCompat.getColor(activity, android.R.color.transparent)
        window.setBackgroundDrawable(background)
    }

    fun showHidePassword(editText: EditText, imgVisible: ImageView) {
        val typeface = editText.typeface

        if (editText.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
            // hiện mật khẩu
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imgVisible.setImageResource(R.drawable.ic_visibility)
        } else {
            // ẩn mật khẩu
            editText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            imgVisible.setImageResource(R.drawable.ic_visibility_off)
        }
        editText.typeface = typeface
        editText.setSelection(editText.text.length)
    }

    fun startMusicService(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }
}
