package com.team28.wondermusic.common

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.InputType
import android.util.TypedValue
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Account
import java.text.SimpleDateFormat
import java.util.*


object Helper {

    fun isMyAccount(account: Account): Boolean {
        return account.idAccount == DataLocal.myAccount.idAccount
    }

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

    private fun stringToDate(datetime: String): Date? {
        // 2022-03-06T22:14:40.034Z
        val str = datetime.substring(0, "2022-03-06T22:14:40".length).replace('T', ' ')
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return try {
            sdf.parse(str)
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun toDateTimeDistance(datetime: String): String {
        val sdf = SimpleDateFormat("HH:mm - dd/MM/yyyy")
        return try {
            val date = sdf.parse(datetime)
            if (date == null) {
                datetime
            } else {
                val diff = Date().time - date.time
                getDifferenceString(diff)
            }
        } catch (e: Exception) {
            val date = stringToDate(datetime)
            if (date == null) {
                datetime
            } else {
                getDifferenceString(Date().time - date.time)
            }
        }
    }

    private fun getDifferenceString(time: Long): String {
        return when {
            time < 1000L * 60 -> "Vừa xong"
            time < 1000L * 60 * 60 -> {
                "${time / (1000L * 60)} phút trước"
            }
            time < 1000L * 60 * 60 * 24 -> {
                "${time / (1000L * 60 * 60)} giờ trước"
            }
            time < 1000L * 60 * 60 * 24 * 30 -> {
                "${time / (1000L * 60 * 60 * 24)} ngày trước"
            }
            time < 1000L * 60 * 60 * 24 * 30 * 12 -> {
                "${time / (1000L * 60 * 60 * 24 * 30)} tháng trước"
            }
            else -> {
                "${time / (1000L * 60 * 60 * 24 * 30 * 12)} năm trước"
            }
        }
    }

    fun dpToPixel(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}

fun Drawable.resize(context: Context, width: Int, height: Int): Drawable {
    val b = (this as BitmapDrawable).bitmap
    val bitmapResized = Bitmap.createScaledBitmap(b, width, height, false)
    return BitmapDrawable(context.resources, bitmapResized)
}
