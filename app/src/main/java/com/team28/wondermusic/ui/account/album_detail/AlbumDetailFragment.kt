package com.team28.wondermusic.ui.account.album_detail

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
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentAlbumDetailBinding
import com.team28.wondermusic.ui.formsong.album_form.FormAlbumDialogFragment
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity

class AlbumDetailFragment : BaseDialogFragment(), SongClickListener {

    override val isFullHeight = true

    private lateinit var binding: FragmentAlbumDetailBinding

    private lateinit var songAdapter: SongAdapter

    private var album: Album? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        album = arguments?.getParcelable(Constants.Album)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInfoPlaylist()

        songAdapter = SongAdapter(this)
        songAdapter.differ.submitList(TempData.songs)

        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@AlbumDetailFragment.context)
        }

        binding.btnEditAlbum.setOnClickListener {
            FormAlbumDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.Album, album)
                }
            }.show(requireActivity().supportFragmentManager, null)
        }
    }

    private fun setInfoPlaylist() {
        album?.let { album ->
            binding.tvAlbumName.text = album.name
            binding.tvAccountName.text = album.account.accountName
            album.songs?.let {
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