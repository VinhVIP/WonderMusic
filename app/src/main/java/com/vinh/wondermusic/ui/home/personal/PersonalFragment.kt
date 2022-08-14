package com.vinh.wondermusic.ui.home.personal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.MenuIndividualAdapter
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.data.models.MenuClickListener
import com.vinh.wondermusic.data.models.MenuIndividual
import com.vinh.wondermusic.data.models.MenuIndividualType.*
import com.vinh.wondermusic.databinding.FragmentIndividualBinding
import com.vinh.wondermusic.ui.admin.ManagerTypeActivity
import com.vinh.wondermusic.ui.formsong.FormSongActivity
import com.vinh.wondermusic.ui.home.personal.album.AlbumFragment
import com.vinh.wondermusic.ui.home.personal.album.FormAlbumDialogFragment
import com.vinh.wondermusic.ui.home.personal.favoritesongs.FavoriteSongsFragment
import com.vinh.wondermusic.ui.home.personal.followers.FollowersFragment
import com.vinh.wondermusic.ui.home.personal.followings.FollowingsFragment
import com.vinh.wondermusic.ui.home.personal.mysongs.MySongsFragment
import com.vinh.wondermusic.ui.home.personal.playlist.FormPlaylistFragment
import com.vinh.wondermusic.ui.home.personal.playlist.PlaylistFragment

class PersonalFragment : Fragment(), MenuClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DataLocal.myAccount.role == 1) {
            menus.add(MenuIndividual("Quản lý thể loại", R.drawable.ic_admin, MANAGE_TYPE))
        }
    }

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
                this@PersonalFragment.context,
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
            MANAGE_TYPE -> {
                startActivity(Intent(requireContext(), ManagerTypeActivity::class.java))
            }
        }
    }

    private fun showFragment(fragment: DialogFragment) {
        fragment.show(requireActivity().supportFragmentManager, null)
    }

}