package com.team28.wondermusic.ui.player.songinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.database.entities.toStringTypes
import com.team28.wondermusic.databinding.FragmentSongInfoBinding
import com.team28.wondermusic.ui.player.PlayerViewModel

class SongInfoFragment : Fragment() {

    private lateinit var binding: FragmentSongInfoBinding
    private val viewModel by viewModels<PlayerViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongInfoBinding.inflate(inflater, container, false)
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
        }
    }

}