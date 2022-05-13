package com.team28.wondermusic.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.*
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityAccountBinding
import com.team28.wondermusic.service.MusicService
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActivity : BaseActivity(), SongClickListener, PlaylistClickListener,
    AlbumClickListener {

    private lateinit var binding: ActivityAccountBinding
    private val viewModel by viewModels<AccountViewModel>()

    private lateinit var songAdapter: SongSmallAdapter
    private lateinit var playlistAdapter: PlaylistSmallAdapter
    private lateinit var albumAdapter: AlbumSmallAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Helper.setStatusBarGradiant(this, R.drawable.bg_account)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            Intent(this, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.also { startActivity(it) }
            finish()
        }

        binding.btnPlayMusic.setOnClickListener {
            viewModel.songs.value?.let {
                if (it.isNotEmpty()) {
                    startActivity(Intent(this, PlayerActivity::class.java))
                    Helper.sendMusicAction(
                        this,
                        MusicService.ACTION_PLAY,
                        it[0],
                        it as ArrayList<Song>
                    )
                }
            }
        }

        getData()

        setupSong()
        setupPlaylist()
        setupAlbum()

        setActions()
        observers()
    }

    override fun onStart() {
        super.onStart()
        fetchData()
        Log.d("vinhabc", "onStart")
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerSong.startShimmer()
        binding.shimmerAlbum.startShimmer()
        binding.shimmerPlaylist.startShimmer()
    }


    override fun onPause() {
        super.onPause()
        binding.shimmerSong.stopShimmer()
        binding.shimmerAlbum.stopShimmer()
        binding.shimmerPlaylist.stopShimmer()
    }

    private fun getData() {
        val account = intent.getParcelableExtra<Account>(Constants.Account)
        if (account == null) {
            finish()
        } else {
            viewModel.dataAccount = account
            showAccountInfo(account)
        }
    }

    private fun fetchData() {
        viewModel.getAccountInfo()
        viewModel.getSongsOfAccount()
        viewModel.getAlbumsOfAccount()
        viewModel.getPlaylistsOfAccount()
    }

    private fun setActions() {
        binding.swipeRefresh.setOnRefreshListener {
            fetchData()
        }

        binding.btnChangePassword.setOnClickListener {
            ChangePasswordFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.Account, viewModel.account.value)
                }
            }.show(supportFragmentManager, null)
        }

        binding.btnEditProfile.setOnClickListener {
            ChangeProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.Account, viewModel.account.value)
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

        binding.btnFollow.setOnClickListener {
            Log.d("vinhabc", "btn follow")
            if (viewModel.dataAccount.followStatus) {
                viewModel.unFollowAccount()
            } else {
                viewModel.followAccount()
            }
        }
    }

    private fun showAccountInfo(account: Account) {
        if (account.idAccount == DataLocal.myAccount.idAccount) {
            binding.layoutMyAccount.visibility = View.VISIBLE
            binding.btnEditProfile.visibility = View.VISIBLE

            binding.layoutAction.visibility = View.GONE
        } else {
            binding.layoutMyAccount.visibility = View.GONE
            binding.btnEditProfile.visibility = View.GONE

            binding.layoutAction.visibility = View.VISIBLE
        }

        binding.apply {
            if (account.avatar.isNotEmpty()) Picasso.get().load(account.avatar)
                .placeholder(R.drawable.ic_user_colorful).fit()
                .into(imgAvatar)
            tvAccountName.text = account.accountName
            tvEmail.text = account.email

            tvTotalSongs.text = "${account.totalSongs}"
            tvTotalLikes.text = "${account.totalLikes}"
            tvTotalFollowers.text = "${account.totalFollowers}"
            tvTotalFollowings.text = "${account.totalFollowings}"
            tvDateCreated.text = Helper.toDateString(account.dateCreated)

            btnFollow.text = if (account.followStatus) "Đã theo dõi" else "Theo dõi"
        }
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            binding.btnFollow.isEnabled = !it
            if (it) binding.btnFollow.text = "..."
        }

        viewModel.account.observe(this) {
            Log.d("vinhabc", "new account")
            showAccountInfo(it)
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.followResponseStatus.observe(this) {
            it?.let {
                if (it) {
                    viewModel.getAccountInfo()
                } else {
                    showErrorDialog(viewModel.message!!)
                }
                viewModel.followResponseStatus.postValue(null)
            }
        }

        viewModel.updateStatus.observe(this) {
            if (it) {
                viewModel.getAccountInfo()
                viewModel.updateStatus.value = false
            }
        }
    }

    private fun showFragment(fragment: DialogFragment) {
        fragment.show(supportFragmentManager, null)
    }

    private fun setupSong() {
        songAdapter = SongSmallAdapter(this)
        viewModel.songs.observe(this) {
            binding.shimmerSong.stopShimmer()
            binding.shimmerSong.visibility = View.GONE
            binding.recyclerSong.visibility = View.VISIBLE
            if (it.size > 10) {
                songAdapter.differ.submitList(it.subList(0, 9))
            } else {
                songAdapter.differ.submitList(it)
            }
        }

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
        viewModel.playlists.observe(this) {
            binding.shimmerPlaylist.stopShimmer()
            binding.shimmerPlaylist.visibility = View.GONE
            binding.recyclerPlaylist.visibility = View.VISIBLE
            playlistAdapter.differ.submitList(it)

            Log.d("vinhabc", "${it.size}")
        }

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
        viewModel.albums.observe(this) {
            binding.shimmerAlbum.stopShimmer()
            binding.shimmerAlbum.visibility = View.GONE
            binding.recyclerAlbum.visibility = View.VISIBLE
            albumAdapter.differ.submitList(it)
        }

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
        startActivity(Intent(this, PlayerActivity::class.java))
        Helper.sendMusicAction(
            this,
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
        }.show(supportFragmentManager, null)
    }

    override fun onPlaylistClick(playlist: Playlist) {
        val fragment = PlaylistDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Playlist, playlist)
        }
        fragment.show(supportFragmentManager, null)
    }

    override fun onPlaylistMoreMenuClick(playlist: Playlist, position: Int) {

    }

    override fun onAlbumClick(album: Album) {
        val fragment = AlbumDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Album, album)
        }
        fragment.show(supportFragmentManager, null)
    }

    override fun onAlbumMoreMenuClick(album: Album, position: Int) {

    }

}