package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.ItemPlaylistBinding

class PlaylistAdapter(private val listener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val itemBinding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.idPlaylist == newItem.idPlaylist
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = differ.currentList[position]

        // TODO: Load ảnh từ song
        holder.itemBinding.apply {
            Picasso.get().load(R.drawable.phathuy).into(imgAvatar)
            tvPlaylistName.text = playlist.name
            tvSingerName.text = playlist.account.accountName
            playlist.songs?.let {
                tvTotalSongs.text = "${it.size}"
            }
        }

        holder.itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}