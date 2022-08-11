package com.vinh.wondermusic.ui.songplaylist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vinh.wondermusic.adapter.OnStartDragListener
import com.vinh.wondermusic.adapter.ReorderHelperCallback
import com.vinh.wondermusic.adapter.SongPlaylistAdapter
import com.vinh.wondermusic.adapter.SongPlaylistListener
import com.vinh.wondermusic.base.fragments.BaseDialogFragment
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.databinding.FragmentSongPlaylistBinding
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.player.PlayerViewModel

class SongPlaylistFragment : BaseDialogFragment(), OnStartDragListener, SongPlaylistListener {

    private lateinit var binding: FragmentSongPlaylistBinding
    private val viewModel by viewModels<PlayerViewModel>({ requireActivity() })

    private lateinit var songPlaylistAdapter: SongPlaylistAdapter
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSongPlaylistBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.songList.observe(requireActivity()) {
            songPlaylistAdapter.setData(it)

            viewModel.songList.removeObservers(requireActivity())
        }

        viewModel.song.observe(requireActivity()) {
            songPlaylistAdapter.setCurrentSong(it)
        }
    }

    private fun setupRecyclerView() {
        songPlaylistAdapter = SongPlaylistAdapter(this, this)

        binding.recyclerPlaylist.apply {
            adapter = songPlaylistAdapter
            layoutManager = LinearLayoutManager(this@SongPlaylistFragment.context)
        }

        val callback: ItemTouchHelper.Callback = ReorderHelperCallback(songPlaylistAdapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(binding.recyclerPlaylist)
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder?) {
        viewHolder?.let {
            mItemTouchHelper?.startDrag(it)
        }
    }

    override fun onSongPlaylistClick(song: Song, position: Int) {
        songPlaylistAdapter.setCurrentSong(song)
        val intent = Intent(context, MusicService::class.java)

        intent.putExtra(Constants.Action, MusicService.ACTION_PLAY).apply {
            val bundle = Bundle().apply {
                putParcelable(Constants.Song, song)
            }
            putExtra(Constants.Data, bundle)
        }

        Helper.startMusicService(requireContext(), intent)
    }

    override fun onSongPlaylistReorder(songs: ArrayList<Song>) {
        viewModel.songList.postValue(songs)
        val intent = Intent(context, MusicService::class.java)

        intent.putExtra(Constants.Action, MusicService.ACTION_DO_NOTHING).apply {
            val bundle = Bundle().apply {
                putParcelableArrayList(Constants.SongList, songs)
            }
            putExtra(Constants.Data, bundle)
        }

        Helper.startMusicService(requireContext(), intent)
    }

}