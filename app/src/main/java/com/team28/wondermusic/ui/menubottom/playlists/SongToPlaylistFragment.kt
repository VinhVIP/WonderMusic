package com.team28.wondermusic.ui.menubottom.playlists

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
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentSongToPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongToPlaylistFragment : BaseDialogFragment(), PlaylistClickListener {

    private lateinit var binding: FragmentSongToPlaylistBinding
    private val viewModel by viewModels<SongTopPlaylistViewModel>()

    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val song = arguments?.getParcelable<Song>(Constants.Song)
        if (song == null) dismiss()
        else this.song = song

        viewModel.getMyPlaylists()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongToPlaylistBinding.inflate(inflater, container, false)

        playlistAdapter = PlaylistAdapter(mutableListOf(), this)
        binding.recyclerPlaylist.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.isLoading.observe(this) {
            binding.pbLoading.visibility = if (it) View.VISIBLE else View.GONE
        }

        viewModel.playlists.observe(this) {
            playlistAdapter.setData(it)
        }

        viewModel.addSongToPlaylistStatus.observe(this) {
            it?.let {
                Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                if (it) dismiss()
                viewModel.addSongToPlaylistStatus.value = null
            }
        }

        return binding.root
    }

    override fun onPlaylistClick(playlist: Playlist) {
        viewModel.addSongToPlaylist(playlist.idPlaylist, song.idSong)
    }

    override fun onPlaylistMoreMenuClick(playlist: Playlist, position: Int) {
    }

}