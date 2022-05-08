package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.ItemPlaylistSmallBinding

class PlaylistSmallAdapter(private val listener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistSmallAdapter.PlaylistViewHolder>() {

    inner class PlaylistViewHolder(val itemBinding: ItemPlaylistSmallBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.idPlaylist == newItem.idPlaylist
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemPlaylistSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = differ.currentList[position]

        val img = listOf(
            holder.itemBinding.imgAvatar1,
            holder.itemBinding.imgAvatar2,
            holder.itemBinding.imgAvatar3,
            holder.itemBinding.imgAvatar4
        )

        holder.itemBinding.apply {
            playlist.songs?.let {
                if (it.isNotEmpty()) {
                    for (i in 0..3) {
                        if (it[i % it.size].image.isNotEmpty()) {
                            Picasso.get().load(it[i % it.size].image).fit().into(img[i])
                        }
                    }
                }
            }
            tvPlaylistName.text = playlist.name
        }

        holder.itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
