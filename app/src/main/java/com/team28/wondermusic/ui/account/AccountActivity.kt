package com.team28.wondermusic.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.*
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityAccountBinding
import com.team28.wondermusic.ui.account.album_detail.AlbumDetailFragment
import com.team28.wondermusic.ui.account.albums.AlbumOfAccountFragment
import com.team28.wondermusic.ui.account.changepassword.ChangePasswordFragment
import com.team28.wondermusic.ui.account.changeprofile.ChangeProfileFragment
import com.team28.wondermusic.ui.account.playlist_detail.PlaylistDetailFragment
import com.team28.wondermusic.ui.account.playlists.PlaylistOfAccountFragment
import com.team28.wondermusic.ui.account.songs.SongOfAccountFragment
import com.team28.wondermusic.ui.login.LoginActivity
import com.team28.wondermusic.ui.menubottom.MenuBottomFragment
import com.team28.wondermusic.ui.player.PlayerActivity

class AccountActivity : AppCompatActivity(), SongClickListener, PlaylistClickListener,
    AlbumClickListener {

    private lateinit var binding: ActivityAccountBinding

    private lateinit var songAdapter: SongSmallAdapter
    private lateinit var playlistAdapter: PlaylistSmallAdapter
    private lateinit var albumAdapter: AlbumSmallAdapter

    private var account: Account? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_account)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.also { startActivity(it) }
            finish()
        }

        getData()

        setupSong()
        setupPlaylist()
        setupAlbum()

        setActions()
    }

    private fun getData() {
        account = intent.getParcelableExtra(Constants.Account)

        account?.let {
            if (it.idAccount == TempData.myAccount.idAccount) {
                binding.layoutMyAccount.visibility = View.VISIBLE
                binding.btnEditProfile.visibility = View.VISIBLE

                binding.layoutAction.visibility = View.GONE
            } else {
                binding.layoutMyAccount.visibility = View.GONE
                binding.btnEditProfile.visibility = View.GONE

                binding.layoutAction.visibility = View.VISIBLE
            }

            binding.apply {
                Picasso.get().load(it.avatar).fit().into(imgAvatar)
                tvAccountName.text = it.accountName
                tvEmail.text = it.email

                tvTotalSongs.text = "${it.totalSongs}"
                tvTotalLikes.text = "${it.totalLikes}"
                tvTotalFollowers.text = "${it.totalFollowers}"
                tvTotalFollowings.text = "${it.totalFollowings}"
                tvDateCreated.text = it.dateCreated
            }

        }
    }

    private fun setActions() {
        binding.btnChangePassword.setOnClickListener {
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.Account, account)
                }
            }.show(supportFragmentManager, null)
        }

        binding.btnEditProfile.setOnClickListener {
            ChangeProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.Account, account)
                }
            }.show(supportFragmentManager, null)
        }

        binding.menuShowSongs.setOnClickListener {
            showFragment(SongOfAccountFragment())
        }

        binding.menuShowAlbums.setOnClickListener {
            showFragment(AlbumOfAccountFragment())
        }

        binding.menuShowPlaylists.setOnClickListener {
            showFragment(PlaylistOfAccountFragment())
        }
    }

    private fun showFragment(fragment: DialogFragment) {
        fragment.show(supportFragmentManager, null)
    }

    private fun setupSong() {
        songAdapter = SongSmallAdapter(this)
        songAdapter.differ.submitList(TempData.songs)

        binding.recyclerSong.apply {
            adapter = songAdapter
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerSong)
    }

    private fun setupPlaylist() {
        playlistAdapter = PlaylistSmallAdapter(this)
        playlistAdapter.differ.submitList(TempData.playlists)

        binding.recyclerPlaylist.apply {
            adapter = playlistAdapter
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerPlaylist)
    }

    private fun setupAlbum() {
        albumAdapter = AlbumSmallAdapter(this)
        albumAdapter.differ.submitList(TempData.albums)

        binding.recyclerAlbum.apply {
            adapter = albumAdapter
            layoutManager =
                LinearLayoutManager(
                    context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerAlbum)
    }

    private fun setHorizontalRecyclerScroll(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun onSongClick(song: Song) {
        startActivity(Intent(this, PlayerActivity::class.java).apply {
            putExtra(Constants.Song, song)
        })
    }

    override fun onOpenMenu(song: Song) {
        MenuBottomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
        }.show(supportFragmentManager, null)
    }

    override fun onPlaylistClick(playlist: Playlist) {
        val fragment = PlaylistDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Playlist, playlist)
        }
        fragment.show(supportFragmentManager, null)
    }

    override fun onAlbumClick(album: Album) {
        val fragment = AlbumDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Album, album)
        }
        fragment.show(supportFragmentManager, null)
    }

}