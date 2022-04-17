package com.team28.wondermusic.ui.home.individual.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.PlaylistAdapter
import com.team28.wondermusic.adapter.PlaylistClickListener
import com.team28.wondermusic.base.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.FragmentPlaylistBinding
import com.team28.wondermusic.ui.account.playlist_detail.PlaylistDetailFragment

class PlaylistFragment : BaseDialogFragment(), PlaylistClickListener {

    private lateinit var binding: FragmentPlaylistBinding

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter(this)
        playlistAdapter.differ.submitList(TempData.playlists)

        binding.recyclerPlaylist.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(this@PlaylistFragment.context)
        }
    }

    override fun onPlaylistClick(playlist: Playlist) {
        val fragment = PlaylistDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Playlist, playlist)
        }
        fragment.show(requireActivity().supportFragmentManager, null)
    }
}