package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Playlist
import com.team28.wondermusic.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private var list: MutableList<Playlist> = mutableListOf(),
    private val listener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    var hideBtnMore = false

    inner class PlaylistViewHolder(val itemBinding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun changeAt(position: Int, playlist: Playlist) {
        list[position] = playlist
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Playlist>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = list[position]

        holder.itemBinding.apply {
            tvPlaylistName.text = playlist.name
            tvSingerName.text = playlist.account.accountName
            if (playlist.playlistStatus == 0) {
                imgPrivate.visibility = View.GONE
            }
            playlist.songs?.let {
                if (it.isNotEmpty() && it.first().image.isNotEmpty()) {
                    Picasso.get().load(it.first().image).into(imgAvatar)
                } else {
                    // TODO: Load ảnh mặc định
                    Picasso.get().load(R.drawable.bitmap_music).into(imgAvatar)
                    imgSurface.visibility = View.GONE
                }
                tvTotalSongs.text = "${it.size}"
            }

            btnMore.visibility = if (hideBtnMore) View.GONE else View.VISIBLE
        }

        holder.itemView.setOnClickListener {
            listener.onPlaylistClick(playlist)
        }

        holder.itemBinding.btnMore.setOnClickListener {
            listener.onPlaylistMoreMenuClick(playlist, position)
        }
    }

    override fun getItemCount(): Int = list.size
}