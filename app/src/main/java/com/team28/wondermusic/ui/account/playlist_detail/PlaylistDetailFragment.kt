package com.team28.wondermusic.ui.account.playlist_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.SongAdapter
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentPlaylistDetailBinding
import com.team28.wondermusic.ui.home.individual.playlist.FormPlaylistFragment
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity

class PlaylistDetailFragment : BaseDialogFragment(), SongClickListener {

    override val isFullHeight = true

    private lateinit var binding: FragmentPlaylistDetailBinding

    private lateinit var songAdapter: SongAdapter

    private var playlist: Playlist? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getParcelable(Constants.Playlist)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInfoPlaylist()

        songAdapter = SongAdapter(this)
        songAdapter.differ.submitList(TempData.songs)

        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@PlaylistDetailFragment.context)
        }
    }

    private fun setInfoPlaylist() {
        playlist?.let { playlist ->
            binding.tvPlaylistName.text = playlist.name
            binding.tvAccountName.text = playlist.account.accountName
            playlist.songs?.let {
                binding.tvTotalSongs.text = "${it.size}"
            }

        }
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java).apply {
            putExtra(Constants.Song, song)
        })
    }

    override fun onOpenMenu(song: Song) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}