package com.team28.wondermusic.ui.account.songs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.adapter.SongLiteAdapter
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentSongOfAccountBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.account.AccountViewModel
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity
import java.util.ArrayList


class SongOfAccountFragment : BaseDialogFragment(), SongClickListener {

    override val isFullHeight: Boolean = true

    private lateinit var binding: FragmentSongOfAccountBinding
    private val viewModel by viewModels<AccountViewModel>({ requireActivity() })

    private lateinit var songAdapter: SongLiteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongOfAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = SongLiteAdapter(this)
        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@SongOfAccountFragment.context)
        }

        viewModel.songs.observe(this){
            songAdapter.differ.submitList(it)
        }

    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java))
        Helper.sendMusicAction(
            requireContext(),
            MusicService.ACTION_PLAY,
            song,
            viewModel.songs.value as ArrayList<Song>
        )
    }

    override fun onOpenMenu(song: Song, position: Int) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}