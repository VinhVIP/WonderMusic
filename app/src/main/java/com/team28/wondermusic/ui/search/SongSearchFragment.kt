package com.team28.wondermusic.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.adapter.SongLiteAdapter
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentSongSearchBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SongSearchFragment : Fragment(), SongClickListener {

    private lateinit var binding: FragmentSongSearchBinding
    private val viewModel by viewModels<SearchViewModel>({ requireActivity() })

    private lateinit var songAdapter: SongLiteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongSearchBinding.inflate(inflater, container, false)

        songAdapter = SongLiteAdapter(this)
        binding.recyclerView.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.songs.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.tvNoResult.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                songAdapter.differ.submitList(it)

                binding.tvNoResult.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    override fun onSongClick(song: Song) {
        viewModel.songs.value?.let {
            startActivity(Intent(context, PlayerActivity::class.java))
            Helper.sendMusicAction(
                requireContext(),
                MusicService.ACTION_PLAY,
                song,
                ArrayList(it)
            )
        }
    }

    override fun onOpenMenu(song: Song, position: Int) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
                putInt(Constants.Position, position)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}