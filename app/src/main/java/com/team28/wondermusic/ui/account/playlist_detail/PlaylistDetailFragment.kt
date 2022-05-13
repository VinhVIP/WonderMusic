package com.team28.wondermusic.ui.account.playlist_detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.RemoveSongFromPlaylistListener
import com.team28.wondermusic.adapter.SongAdapter
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.adapter.SongLiteAdapter
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentPlaylistDetailBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistDetailFragment : BaseDialogFragment(), SongClickListener,
    RemoveSongFromPlaylistListener {

//    override val isFullHeight = true

    private lateinit var binding: FragmentPlaylistDetailBinding
    private val viewModel by viewModels<PlaylistDetailViewModel>()

    private lateinit var songAdapter: SongLiteAdapter

    private lateinit var playlist: Playlist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val playlist: Playlist? = arguments?.getParcelable(Constants.Playlist)

        if (playlist == null) dismiss()
        else this.playlist = playlist
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

        songAdapter = SongLiteAdapter(this, this)
        if (Helper.isMyAccount(playlist.account)) {
            songAdapter.showRemoveSongFromPlaylist = true
        }

        songAdapter.differ.submitList(playlist.songs)

        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.removeSongFromPlaylistStatus.observe(this) {
            it?.let {
                Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT).show()
                if (it && viewModel.removePosition != -1) {
                    playlist.songs?.let { songs ->
                        val list = mutableListOf<Song>()
                        list.addAll(songs)
                        list.removeAt(viewModel.removePosition)
                        songAdapter.differ.submitList(list)

                        playlist.songs = list
                        binding.tvTotalSongs.text = "${list.size}"
                    }
                }
                viewModel.removePosition = -1
                viewModel.removeSongFromPlaylistStatus.value = null
            }
        }
    }

    private fun setInfoPlaylist() {
        binding.tvPlaylistName.text = playlist.name
        binding.tvAccountName.text = playlist.account.accountName
        playlist.songs?.let {
            binding.tvTotalSongs.text = "${it.size}"
        }
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java))
        Helper.sendMusicAction(
            requireContext(),
            MusicService.ACTION_PLAY,
            song,
            playlist.songs as ArrayList<Song>
        )
    }

    override fun onOpenMenu(song: Song, position: Int) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

    override fun onRemoveSongFromPlaylist(song: Song, position: Int) {
        viewModel.removePosition = position
        viewModel.removeSongFromPlaylist(playlist.idPlaylist, song.idSong)
    }

}