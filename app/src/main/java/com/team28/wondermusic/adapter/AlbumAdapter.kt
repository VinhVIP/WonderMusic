package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.ItemAlbumBinding

class AlbumAdapter(val listener: AlbumClickListener) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val itemBinding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.idAlbum == newItem.idAlbum
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = differ.currentList[position]

        holder.itemBinding.apply {
            tvAlbumName.text = album.name
            album.songs?.let {
                tvTotalSongs.text = "${it.size}"
            }
        }

        holder.itemView.setOnClickListener {
            listener.onAlbumClick(album)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
