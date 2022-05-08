package com.team28.wondermusic.ui.home.personal.playlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.services.playlist.PlaylistModal
import com.team28.wondermusic.databinding.FragmentFormPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormPlaylistFragment : DialogFragment() {

    private lateinit var binding: FragmentFormPlaylistBinding
    private val viewModel by viewModels<PlaylistViewModel>({ requireActivity() })

    private var playlist: Playlist? = null
    private var position: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playlist = arguments?.getParcelable(Constants.Playlist)
        position = arguments?.getInt(Constants.Position)

        viewModel.playlistUpdate = playlist
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFormPlaylistBinding.inflate(inflater, container, false)
        binding.tvError.visibility = View.GONE

        playlist?.let {
            binding.edPlaylistName.setText(it.name)
            binding.btnSubmit.text = "Chỉnh sửa"
        }

        submitEvent()

        viewModel.isLoading.observe(this) {
            if (it == false) binding.btnSubmit.isEnabled = true

            // loading xong mới cho phép tắt dialog
            this.isCancelable = true
        }

        viewModel.updateStatus.observe(this) { status ->
            status?.let {
                viewModel.message.value?.let { message ->
                    if (status) {
                        viewModel.playlistUpdate?.let {
                            val modal = getCurrentPlaylistModal()
                            it.name = modal!!.namePlaylist
                            it.playlistStatus = modal.playlistStatus
                        }
                        this.dismiss()
                    } else {
                        showNamePlaylistError(message)
                    }
                }
            }
        }

        viewModel.addStatus.observe(this) { status ->
            status?.let {
                Log.d("vinh", "status: $it")
                if (status) {
                    this.dismiss()
                } else {
                    showNamePlaylistError(viewModel.message.value!!)
                }
            }
        }

        return binding.root
    }

    private fun submitEvent() {
        binding.btnSubmit.setOnClickListener {
            val modal = getCurrentPlaylistModal()
            modal?.let {
                // Disabled button, show circular progress indicator
                binding.btnSubmit.isEnabled = false

                // Ẩn textview error
                binding.tvError.visibility = View.GONE

                // không cho phép tắt dialog
                this.isCancelable = false

                if (isFormAdd()) {
                    viewModel.addPlaylist(it)
                } else {
                    viewModel.updatePlaylist(playlist!!.idPlaylist, position!!, it)
                }
            }

        }
    }

    private fun showNamePlaylistError(errorText: String) {
        binding.tvError.text = errorText
        binding.tvError.visibility = View.VISIBLE
    }

    private fun getCurrentPlaylistModal(): PlaylistModal? {
        if (binding.edPlaylistName.text.toString().isEmpty()) {
            showNamePlaylistError("Tên playlist không được bỏ trống!")
            return null
        }
        return PlaylistModal(
            namePlaylist = binding.edPlaylistName.text.toString().trim(),
            playlistStatus = if (binding.radioPublic.isChecked) 0 else 1
        )
    }

    private fun isFormAdd(): Boolean = playlist == null

}