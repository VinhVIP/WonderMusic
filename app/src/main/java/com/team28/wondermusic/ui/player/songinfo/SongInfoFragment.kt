package com.team28.wondermusic.ui.player.songinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.adapter.SongSmallAdapter
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.common.setHorizontalRecyclerScroll
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.database.entities.toStringTypes
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentSongInfoBinding
import com.team28.wondermusic.service.MusicService
import com.team28.wondermusic.ui.player.PlayerViewModel

class SongInfoFragment : Fragment() {

    private lateinit var binding: FragmentSongInfoBinding
    private val viewModel by viewModels<PlayerViewModel>({ requireActivity() })

    private lateinit var recommendSongAdapter: SongSmallAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongInfoBinding.inflate(inflater, container, false)

        setupRecommendSong()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.song.observe(requireActivity()) { song ->
            binding.apply {
                tvSongName.text = song.name
                tvSingerName.text = song.singersToString()
                song.album?.let {
                    tvAlbumName.text = it.name
                }
                song.types?.let {
                    tvType.text = song.types.toStringTypes()
                }

                tvLikes.text = "${song.like}"
                tvListens.text = "${song.listen}"
            }

            viewModel.getRecommendSong()
        }
    }

    private fun setupRecommendSong() {
        recommendSongAdapter = SongSmallAdapter(object : SongClickListener {
            override fun onSongClick(song: Song) {
                viewModel.recommendSongs.value?.let {
                    Helper.sendMusicAction(
                        requireContext(),
                        MusicService.ACTION_PLAY,
                        song,
                        ArrayList(it)
                    )
                }
            }

            override fun onOpenMenu(song: Song, position: Int) {
            }
        })

        viewModel.recommendSongs.observe(viewLifecycleOwner) {
            binding.shimmerSong.stopShimmer()
            binding.shimmerSong.visibility = View.GONE
            binding.recyclerSong.visibility = View.VISIBLE
            recommendSongAdapter.differ.submitList(it)
        }

        binding.recyclerSong.apply {
            adapter = recommendSongAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerSong.setHorizontalRecyclerScroll()
    }

}