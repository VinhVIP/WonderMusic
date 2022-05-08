package com.team28.wondermusic.ui.home.personal.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.team28.wondermusic.base.dialogs.ConfirmDialog
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.FragmentAlbumMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumMenuFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentAlbumMenuBinding
    private val viewModel by viewModels<AlbumViewModel>({ requireActivity() })

    private var album: Album? = null
    private var position: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        album = arguments?.getParcelable(Constants.Album)
        position = arguments?.getInt(Constants.Position)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumMenuBinding.inflate(inflater, container, false)

        binding.menuEditAlbum.setOnClickListener {
            album?.let {
                FormAlbumDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Constants.Album, album)
                        putInt(Constants.Position, position!!)
                    }
                }.show(requireActivity().supportFragmentManager, null)

                this.dismiss()
            }
        }

        binding.menuDeleteAlbum.setOnClickListener {
            album?.let {
                val confirmDialog = ConfirmDialog(
                    context = requireContext(),
                    title = "Xác nhận xóa",
                    message = "Bạn có muốn xóa album: ${it.name}",
                    positiveButtonTitle = "Xóa",
                    negativeButtonTitle = "Hủy",
                    callback = object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {
                        }

                        override fun positiveAction() {
                            viewModel.deleteAlbum(it.idAlbum, position!!)
                            this@AlbumMenuFragment.dismiss()
                        }
                    }
                )
                confirmDialog.show()
            }
        }

        return binding.root
    }

}