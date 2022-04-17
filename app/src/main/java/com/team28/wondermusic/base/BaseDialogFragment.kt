package com.team28.wondermusic.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class BaseDialogFragment : BottomSheetDialogFragment() {

    protected open val isFullHeight: Boolean = false

    @SuppressLint("RestrictedApi", "VisibleForTests")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        if (dialog is BottomSheetDialog) {
            dialog.behavior.disableShapeAnimations()

            if (isFullHeight) {
                dialog.setOnShowListener {
                    val layout =
                        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                    layout?.let {
                        val behavior = BottomSheetBehavior.from(it)
                        setupFullHeight(it)
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}