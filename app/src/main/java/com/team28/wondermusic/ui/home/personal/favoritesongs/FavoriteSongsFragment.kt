package com.team28.wondermusic.ui.home.personal.favoritesongs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.adapter.SongClickListener
import com.team28.wondermusic.adapter.SongLiteAdapter
import com.team28.wondermusic.base.fragments.BaseDialogFragment
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentFavoriteSongsBinding
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.menubottom.MenuBottomViewModel
import com.team28.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteSongsFragment : BaseDialogFragment(), SongClickListener {

    override val isFullHeight: Boolean = true

    private lateinit var binding: FragmentFavoriteSongsBinding
    private val viewModel by viewModels<FavoriteSongsViewModel>()
    private val menuViewModel by viewModels<MenuBottomViewModel>({ requireActivity() })

    private lateinit var songAdapter: SongLiteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteSongsBinding.inflate(inflater, container, false)
        observersMenuEvent()
        return binding.root
    }

    private fun observersMenuEvent() {
        menuViewModel.actionLoveStatus.observe(viewLifecycleOwner) {
            it?.let {
                if (it) {

                } else {

                }
                Toast.makeText(context, menuViewModel.message, Toast.LENGTH_SHORT).show()
                menuViewModel.actionLoveStatus.value = null
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        songAdapter = SongLiteAdapter(this)
        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager = LinearLayoutManager(this@FavoriteSongsFragment.context)
        }

        viewModel.favoriteSongs.observe(viewLifecycleOwner) {
            songAdapter.differ.submitList(it)
        }

        viewModel.isLoading.observe(this) {
            if (it)
                binding.pbLoading.visibility = View.VISIBLE
            else
                binding.pbLoading.visibility = View.GONE
        }

        binding.recyclerSong.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.isLoading.value?.let {
                        if (!it) viewModel.fetchData()
                    }
                }
            }
        })
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(context, PlayerActivity::class.java).apply {
            putExtra(Constants.Song, song)
        })
    }

    override fun onOpenMenu(song: Song, position: Int) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(requireActivity().supportFragmentManager, null)
    }

}