package com.team28.wondermusic.ui.home.personal.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.AlbumAdapter
import com.team28.wondermusic.adapter.AlbumClickListener
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.FragmentAlbumBinding
import com.team28.wondermusic.ui.account.album_detail.AlbumDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlbumFragment : BaseDialogFragment(), AlbumClickListener {

    private lateinit var binding: FragmentAlbumBinding
    private val viewModel by viewModels<AlbumViewModel>({ requireActivity() })

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getAllMyAlbum()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        albumAdapter = AlbumAdapter(mutableListOf(), this)

        binding.recyclerAlbum.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(this@AlbumFragment.context)
        }

        binding.btnAddAlbum.setOnClickListener {
            FormAlbumDialogFragment().show(requireActivity().supportFragmentManager, null)
        }
        observers()
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.myAlbum.observe(this) {
            albumAdapter.setData(it)
        }

        viewModel.addStatus.observe(this) { status ->
            status?.let {
                if (it) {
                    albumAdapter.setData(emptyList())
                    viewModel.getAllMyAlbum()
                }
                viewModel.addStatus.postValue(null)
            }
        }

        viewModel.deleteStatus.observe(this) { status ->
            status?.let {
                if (it) {
                    viewModel.changePosition?.let { position ->
                        albumAdapter.removeAt(position)
                    }
                }
                viewModel.deleteStatus.postValue(null)
            }
        }

        viewModel.updateStatus.observe(this) { status ->
            status?.let {
                if (it) {
                    albumAdapter.changeAt(viewModel.changePosition!!, viewModel.albumUpdate!!)
                }
                viewModel.updateStatus.postValue(null)
            }
        }

    }

    override fun onAlbumClick(album: Album) {
        val fragment = AlbumDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Album, album)
        }
        fragment.show(requireActivity().supportFragmentManager, null)
    }

    override fun onAlbumMoreMenuClick(album: Album, position: Int) {
        AlbumMenuFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Album, album)
                putInt(Constants.Position, position)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}