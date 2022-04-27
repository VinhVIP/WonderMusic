package com.team28.wondermusic.ui.home.individual.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.FragmentPlaylistMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistMenuFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentPlaylistMenuBinding
    private val viewModel by viewModels<PlaylistViewModel>({ requireActivity() })

    private var playlist: Playlist? = null
    private var position: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playlist = arguments?.getParcelable(Constants.Playlist)
        position = arguments?.getInt(Constants.Position)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistMenuBinding.inflate(inflater, container, false)

        binding.menuEditPlaylist.setOnClickListener {
            playlist?.let {
                FormPlaylistFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(Constants.Playlist, playlist)
                        putInt(Constants.Position, position!!)
                    }
                }.show(requireActivity().supportFragmentManager, null)

                this.dismiss()
            }
        }

        binding.menuDeletePlaylist.setOnClickListener {
            playlist?.let {
                viewModel.deletePlaylist(it, position!!)
                this.dismiss()
            }
        }

        return binding.root
    }

}