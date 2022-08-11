package com.vinh.wondermusic.ui.account.albums

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
import com.vinh.wondermusic.databinding.FragmentAlbumOfAccountBinding
import com.vinh.wondermusic.ui.account.AccountViewModel
import com.vinh.wondermusic.ui.account.album_detail.AlbumDetailFragment


class AlbumOfAccountFragment : BaseDialogFragment(), AlbumClickListener {

    private lateinit var binding: FragmentAlbumOfAccountBinding
    private val viewModel by viewModels<AccountViewModel>({ requireActivity() })

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumOfAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumAdapter = AlbumAdapter(mutableListOf(), this)

        binding.recyclerAlbum.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(this@AlbumOfAccountFragment.context)
        }

        viewModel.albums.observe(this){
            albumAdapter.setData(it)
        }
    }

    override fun onAlbumClick(album: Album) {
//        Log.d("vinh", album.name)
        val fragment = AlbumDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Album, album)
        }
        fragment.show(requireActivity().supportFragmentManager, null)
    }

    override fun onAlbumMoreMenuClick(album: Album, position: Int) {

    }

}