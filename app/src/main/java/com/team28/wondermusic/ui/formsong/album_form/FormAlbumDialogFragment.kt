package com.team28.wondermusic.ui.formsong.album_form

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.FragmentFormAlbumBinding


class FormAlbumDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentFormAlbumBinding

    private var album: Album? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        album = arguments?.getParcelable(Constants.Album)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormAlbumBinding.inflate(inflater, container, false)

        album?.let {
            binding.edAlbumName.setText(it.name)
            binding.btnSubmit.text = "Chỉnh sửa"
        }

        binding.btnSubmit.setOnClickListener {
            dismiss()
        }

        return binding.root
    }


}