package com.team28.wondermusic.ui.home.individual.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.PlaylistAdapter
import com.team28.wondermusic.adapter.PlaylistClickListener
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.FragmentPlaylistBinding
import com.team28.wondermusic.ui.account.playlist_detail.PlaylistDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistFragment : BaseDialogFragment(), PlaylistClickListener {

    private lateinit var binding: FragmentPlaylistBinding
    private val viewModel by viewModels<PlaylistViewModel>({ requireActivity() })

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getMyPlaylists()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        playlistAdapter = PlaylistAdapter(mutableListOf(), this)
        binding.recyclerPlaylist.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(this@PlaylistFragment.context)
        }

        binding.btnAddPlaylist.setOnClickListener {
            FormPlaylistFragment().show(requireActivity().supportFragmentManager, null)
        }

        viewModel.playlists.value?.let {
            playlistAdapter.setData(it)
            if (it.isEmpty()) {
                binding.pbLoading.visibility = View.VISIBLE
            }
        }
        viewModel.playlists.observe(this) {
            playlistAdapter.setData(it)
        }

        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.addStatus.observe(this) { status ->
            status?.let {
                if (it) {
                    playlistAdapter.setData(emptyList())
                    viewModel.getMyPlaylists()
                }
                viewModel.addStatus.postValue(null)
            }
        }

        viewModel.deleteStatus.observe(this) { status ->
            status?.let {
                if (it)
                    viewModel.changePosition?.let { position ->
                        playlistAdapter.removeAt(position)
                    }
                viewModel.deleteStatus.postValue(null)
            }
        }

        viewModel.updateStatus.observe(this) { status ->
            status?.let {
                if (it) {
                    playlistAdapter.changeAt(viewModel.changePosition!!, viewModel.playlistUpdate!!)
                }
                viewModel.updateStatus.postValue(null)
            }
        }

        return binding.root
    }

    override fun onPlaylistClick(playlist: Playlist) {
        val fragment = PlaylistDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Playlist, playlist)
        }
        fragment.show(requireActivity().supportFragmentManager, null)
    }

    override fun onPlaylistMoreMenuClick(playlist: Playlist, position: Int) {
        PlaylistMenuFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Playlist, playlist)
                putInt(Constants.Position, position)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }
}