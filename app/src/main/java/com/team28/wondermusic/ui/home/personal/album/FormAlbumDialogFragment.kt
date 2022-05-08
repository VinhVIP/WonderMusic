package com.team28.wondermusic.ui.home.personal.album

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.services.album.AlbumModal
import com.team28.wondermusic.databinding.FragmentFormAlbumBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FormAlbumDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentFormAlbumBinding
    private val viewModel by viewModels<AlbumViewModel>({ requireActivity() })

    private var album: Album? = null
    private var position: Int? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        album = arguments?.getParcelable(Constants.Album)
        position = arguments?.getInt(Constants.Position)

        viewModel.albumUpdate = album
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

        viewModel.isLoading.observe(this) {
            if (it == false) binding.btnSubmit.isEnabled = true

            // loading xong mới cho phép tắt dialog
            this.isCancelable = true
        }

        viewModel.addStatus.observe(this) { status ->
            status?.let {
                if (status) {
                    this.dismiss()
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.updateStatus.observe(this) { status ->
            status?.let {
                if (status) {
                    viewModel.albumUpdate?.let {
                        val modal = getCurrentAlbumModal()
                        it.name = modal!!.albumName
                    }
                    this.dismiss()
                } else {
                    Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        onSubmitEvent()

        return binding.root
    }

    private fun onSubmitEvent() {
        binding.btnSubmit.setOnClickListener {
            val modal = getCurrentAlbumModal()
            modal?.let {
                binding.btnSubmit.isEnabled = false

                // không cho phép tắt dialog
                this.isCancelable = false

                if (isFormAdd()) {
                    viewModel.addAlbum(it)
                } else {
                    viewModel.updateAlbum(album!!.idAlbum, position!!, it)
                }
            }
        }
    }

    private fun getCurrentAlbumModal(): AlbumModal? {
        if (binding.edAlbumName.text.toString().isEmpty()) {
            Toast.makeText(context, "Tên album không được bỏ trống", Toast.LENGTH_SHORT).show()
            return null
        }
        return AlbumModal(binding.edAlbumName.text.toString().trim())
    }

    private fun isFormAdd(): Boolean = album == null
}