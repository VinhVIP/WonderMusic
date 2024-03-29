package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.database.entities.singersToString
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ItemSongSmallBinding


class SongSmallAdapter(
    private val listener: SongClickListener
) :
    RecyclerView.Adapter<SongSmallAdapter.SongViewHolder>() {

    inner class SongViewHolder(val itemBinding: ItemSongSmallBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.idSong == newItem.idSong
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = differ.currentList[position]

        holder.itemBinding.apply {
            if (song.image.isEmpty()) {
                Picasso.get().load(R.drawable.bitmap_music).fit()
                    .into(imgAvatar)
            } else {
                Picasso.get().load(song.image)
                    .placeholder(R.drawable.bitmap_music).fit()
                    .into(imgAvatar)
            }

            tvSongName.text = song.name
            tvSinger.text = song.singersToString()
        }

        holder.itemView.setOnClickListener {
            listener.onSongClick(song)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

