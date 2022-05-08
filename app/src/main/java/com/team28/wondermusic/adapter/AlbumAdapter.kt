package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Album
import com.team28.wondermusic.databinding.ItemAlbumBinding

class AlbumAdapter(
    private var list: MutableList<Album> = mutableListOf(),
    val listener: AlbumClickListener
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(val itemBinding: ItemAlbumBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun changeAt(position: Int, album: Album) {
        list[position] = album
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Album>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = list[position]

        holder.itemBinding.apply {
            val firstSong = album.songs?.firstOrNull()
            firstSong?.let {
                if (it.image.isNotEmpty()) {
                    Picasso.get().load(it.image).placeholder(R.drawable.bitmap_music).fit()
                        .into(imgAvatar)
                }
            }
            tvAlbumName.text = album.name
            album.songs?.let {
                tvTotalSongs.text = "${it.size}"
            }
        }

        holder.itemView.setOnClickListener {
            listener.onAlbumClick(album)
        }

        holder.itemView.setOnLongClickListener {
            listener.onAlbumMoreMenuClick(album, position)
            false
        }

        holder.itemBinding.btnMore.setOnClickListener {
            listener.onAlbumMoreMenuClick(album, position)
        }
    }

    override fun getItemCount(): Int = list.size
}
