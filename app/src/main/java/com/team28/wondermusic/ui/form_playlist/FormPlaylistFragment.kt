package com.team28.wondermusic.ui.form_playlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.FragmentFormPlaylistBinding

class FormPlaylistFragment : DialogFragment() {

    private lateinit var binding: FragmentFormPlaylistBinding

    private var playlist: Playlist? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist = arguments?.getParcelable(Constants.Playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormPlaylistBinding.inflate(inflater, container, false)

        playlist?.let {
            binding.edPlaylistName.setText(it.name)

            binding.btnSubmit.text = "Chỉnh sửa"
        }

        binding.btnSubmit.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

}