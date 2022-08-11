package com.vinh.wondermusic.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.adapter.PlaylistAdapter
import com.vinh.wondermusic.adapter.PlaylistClickListener
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Playlist
import com.vinh.wondermusic.databinding.FragmentPlaylistSearchBinding
import com.vinh.wondermusic.ui.account.playlist_detail.PlaylistDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistSearchFragment : Fragment(), PlaylistClickListener {

    private lateinit var binding: FragmentPlaylistSearchBinding
    private val viewModel by viewModels<SearchViewModel>({ requireActivity() })

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistSearchBinding.inflate(inflater, container, false)

        playlistAdapter = PlaylistAdapter(mutableListOf(), this).apply {
            hideBtnMore = true
        }

        binding.recyclerView.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.playlists.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoResult.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                playlistAdapter.setData(it)

                binding.tvNoResult.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
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
    }

}