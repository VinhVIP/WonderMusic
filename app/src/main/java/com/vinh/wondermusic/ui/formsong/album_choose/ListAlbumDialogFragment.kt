package com.vinh.wondermusic.ui.formsong.album_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.adapter.AlbumAdapter
import com.vinh.wondermusic.adapter.AlbumClickListener
import com.vinh.wondermusic.base.fragments.BaseDialogFragment
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Album
import com.vinh.wondermusic.databinding.FragmentAlbumDialogBinding
import com.vinh.wondermusic.ui.formsong.FormSongViewModel
import com.vinh.wondermusic.ui.home.personal.album.FormAlbumDialogFragment

class ListAlbumDialogFragment : BaseDialogFragment(), AlbumClickListener {

    private lateinit var binding: FragmentAlbumDialogBinding
    private lateinit var albumAdapter: AlbumAdapter

    private val viewModel by viewModels<FormSongViewModel>({ requireActivity() })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumAdapter = AlbumAdapter(mutableListOf(), this)

        viewModel.myAlbum.observe(this) {
            albumAdapter.setData(it)
        }

        binding.recyclerAlbum.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(this@ListAlbumDialogFragment.context)
        }

        binding.btnGoToAddAlbum.setOnClickListener {
            val fragment = FormAlbumDialogFragment()
            fragment.show(requireActivity().supportFragmentManager, "album_form")
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllMyAlbum()
    }

    override fun onAlbumClick(album: Album) {
        viewModel.album.postValue(album)
        dismiss()
    }

    override fun onAlbumMoreMenuClick(album: Album, position: Int) {
        FormAlbumDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Album, album)
                putInt(Constants.Position, position)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}