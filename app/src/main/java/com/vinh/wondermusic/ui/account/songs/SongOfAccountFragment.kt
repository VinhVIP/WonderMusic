package com.vinh.wondermusic.ui.account.songs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.adapter.SongClickListener
import com.vinh.wondermusic.adapter.SongLiteAdapter
import com.vinh.wondermusic.base.fragments.BaseDialogFragment
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.databinding.FragmentSongOfAccountBinding
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.account.AccountViewModel
import com.vinh.wondermusic.ui.menubottom.MenuBottomFragment
import com.vinh.wondermusic.ui.player.PlayerActivity


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

        viewModel.songs.observe(this) {
            songAdapter.differ.submitList(it)
        }

        binding.btnPlayMusic.setOnClickListener {
            viewModel.songs.value?.let {
                if (it.isNotEmpty()) {
                    startActivity(Intent(context, PlayerActivity::class.java))
                    Helper.sendMusicAction(
                        requireContext(),
                        MusicService.ACTION_PLAY,
                        it[0],
                        it as ArrayList<Song>
                    )
                }
            }
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