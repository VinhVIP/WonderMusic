package com.team28.wondermusic.ui.home.individual

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.MenuIndividualAdapter
import com.team28.wondermusic.data.models.MenuClickListener
import com.team28.wondermusic.data.models.MenuIndividual
import com.team28.wondermusic.data.models.MenuIndividualType.*
import com.team28.wondermusic.databinding.FragmentIndividualBinding
import com.team28.wondermusic.ui.home.individual.playlist.FormPlaylistFragment
import com.team28.wondermusic.ui.formsong.FormSongActivity
import com.team28.wondermusic.ui.formsong.album_form.FormAlbumDialogFragment
import com.team28.wondermusic.ui.home.individual.album.AlbumFragment
import com.team28.wondermusic.ui.home.individual.favoritesongs.FavoriteSongsFragment
import com.team28.wondermusic.ui.home.individual.followers.FollowersFragment
import com.team28.wondermusic.ui.home.individual.followings.FollowingsFragment
import com.team28.wondermusic.ui.home.individual.mysongs.MySongsFragment
import com.team28.wondermusic.ui.home.individual.playlist.PlaylistFragment

class IndividualFragment : Fragment(), MenuClickListener {

    private lateinit var binding: FragmentIndividualBinding

    private lateinit var menuAdapter: MenuIndividualAdapter


    private val menus = arrayListOf(
        MenuIndividual("Của tôi", R.drawable.song, SONG),
        MenuIndividual("Yêu thích", R.drawable.heart, FAVORITE),
        MenuIndividual("Theo dõi tôi", R.drawable.follower, FOLLOWER),
        MenuIndividual("Đang theo dõi", R.drawable.following, FOLLOWING),
        MenuIndividual("Playlist", R.drawable.playlist, PLAYLIST),
        MenuIndividual("Album", R.drawable.album, ALBUM),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndividualBinding.inflate(layoutInflater, container, false)

        menuAdapter = MenuIndividualAdapter(this)
        menuAdapter.differ.submitList(menus)

        binding.recyclerMenu.apply {
            adapter = menuAdapter
            layoutManager = GridLayoutManager(
                this@IndividualFragment.context,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
        }

        setupFAB()

        return binding.root
    }

    private fun setupFAB() {
        binding.fabAddSong.setOnClickListener {
            startActivity(Intent(context, FormSongActivity::class.java))
        }
        binding.fabAddAlbum.setOnClickListener {
            val fragment = FormAlbumDialogFragment()
            fragment.show(requireActivity().supportFragmentManager, null)
        }
        binding.fabAddPlaylist.setOnClickListener {
            FormPlaylistFragment().show(requireActivity().supportFragmentManager, null)
        }

    }

    override fun onMenuClick(menu: MenuIndividual) {
        when (menu.type) {
            SONG -> showFragment(MySongsFragment())
            FAVORITE -> showFragment(FavoriteSongsFragment())
            FOLLOWER -> showFragment(FollowersFragment())
            FOLLOWING -> showFragment(FollowingsFragment())
            PLAYLIST -> showFragment(PlaylistFragment())
            ALBUM -> showFragment(AlbumFragment())
        }
    }

    private fun showFragment(fragment: DialogFragment) {
        fragment.show(requireActivity().supportFragmentManager, null)
    }

}