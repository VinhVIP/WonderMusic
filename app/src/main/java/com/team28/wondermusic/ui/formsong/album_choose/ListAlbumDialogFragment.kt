package com.team28.wondermusic.ui.formsong.album_choose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.AlbumAdapter
import com.team28.wondermusic.adapter.AlbumClickListener
import com.team28.wondermusic.base.BaseDialogFragment
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.FragmentAlbumDialogBinding
import com.team28.wondermusic.ui.formsong.FormSongViewModel
import com.team28.wondermusic.ui.formsong.album_form.FormAlbumDialogFragment

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

        albumAdapter = AlbumAdapter(this)
        albumAdapter.differ.submitList(TempData.albums)

        binding.recyclerAlbum.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(this@ListAlbumDialogFragment.context)
        }

        binding.btnGoToAddAlbum.setOnClickListener {
            val fragment = FormAlbumDialogFragment()
            fragment.show(requireActivity().supportFragmentManager, "album_form")
        }
    }

    override fun onAlbumClick(album: Album) {
        viewModel.album.postValue(album)
        dismiss()
    }

}