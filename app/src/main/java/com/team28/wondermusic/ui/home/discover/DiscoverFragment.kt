package com.team28.wondermusic.ui.home.discover

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.team28.wondermusic.adapter.*
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.TempData
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.FragmentDiscoverBinding
import com.team28.wondermusic.ui.account.AccountActivity
import com.team28.wondermusic.ui.account.playlist_detail.PlaylistDetailFragment
import com.team28.wondermusic.ui.player.PlayerActivity


class DiscoverFragment : Fragment(), SongClickListener, PlaylistClickListener,
    AccountClickListener {

    private lateinit var binding: FragmentDiscoverBinding

    private lateinit var newSongAdapter: SongSmallAdapter
    private lateinit var followSongAdapter: SongSmallAdapter
    private lateinit var topSongAdapter: SongTopAdapter
    private lateinit var playlistAdapter: PlaylistSmallAdapter
    private lateinit var singerAdapter: SingerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNewestSong()
        setupFollowSong()
        setupTopSong()
        setupPlaylist()
        setupSingers()
    }

    private fun setupNewestSong() {
        newSongAdapter = SongSmallAdapter(this)
        newSongAdapter.differ.submitList(TempData.songs)

        binding.recyclerNewSong.apply {
            adapter = newSongAdapter
            layoutManager =
                LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerNewSong)
    }

    private fun setupFollowSong() {
        followSongAdapter = SongSmallAdapter(this)
        followSongAdapter.differ.submitList(TempData.songs)

        binding.recyclerFollowSong.apply {
            adapter = followSongAdapter
            layoutManager =
                LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerFollowSong)
    }

    private fun setupTopSong() {
        topSongAdapter = SongTopAdapter(this)
        topSongAdapter.differ.submitList(TempData.songs)

        binding.recyclerTopSong.apply {
            adapter = topSongAdapter
            layoutManager =
                LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerTopSong)
    }

    private fun setupPlaylist() {
        playlistAdapter = PlaylistSmallAdapter(this)
        playlistAdapter.differ.submitList(TempData.playlists)

        binding.recyclerPlaylist.apply {
            adapter = playlistAdapter
            layoutManager =
                LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerPlaylist)
    }

    private fun setupSingers() {
        singerAdapter = SingerAdapter(this)
        singerAdapter.differ.submitList(TempData.accounts)

        binding.recyclerTopSinger.apply {
            adapter = singerAdapter
            layoutManager =
                LinearLayoutManager(
                    this@DiscoverFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
        }

        setHorizontalRecyclerScroll(binding.recyclerTopSinger)
    }


    private fun setHorizontalRecyclerScroll(recyclerView: RecyclerView) {
        recyclerView.addOnItemTouchListener(object : OnItemTouchListener {
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
        startActivity(Intent(context, PlayerActivity::class.java).apply {
            putExtra(Constants.Song, song)
        })
    }

    override fun onOpenMenu(song: Song) {

    }

    override fun onPlaylistClick(playlist: Playlist) {
        val fragment = PlaylistDetailFragment()
        fragment.arguments = Bundle().apply {
            putParcelable(Constants.Playlist, playlist)
        }
        fragment.show(requireActivity().supportFragmentManager, null)
    }

    override fun onAccountClick(account: Account) {
        startActivity(Intent(context, AccountActivity::class.java).apply {
            putExtra(Constants.Account, account)
        })
    }

}