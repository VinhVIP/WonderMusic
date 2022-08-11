package com.vinh.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinh.wondermusic.R
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.databinding.ItemSongTopBinding

class SongTopAdapter(
    private val listener: SongClickListener
) :
    RecyclerView.Adapter<SongTopAdapter.SongViewHolder>() {

    inner class SongViewHolder(val itemBinding: ItemSongTopBinding) :
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
            ItemSongTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

            tvTopNumber.text = "#${position + 1}"
            tvSongName.text = song.name
            tvSinger.text = song.singersToString()
            tvDate.text = Helper.toDateString(song.dateCreated)
        }

        holder.itemView.setOnClickListener {
            listener.onSongClick(song)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
