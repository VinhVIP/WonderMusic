package com.vinh.wondermusic.adapter


import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinh.wondermusic.R
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.databinding.ItemSongBinding

class SongAdapter(
    private val listener: SongClickListener,
    private val removeSongFromPlaylistListener: RemoveSongFromPlaylistListener? = null
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    var showRemoveSongFromPlaylist: Boolean = false

    private var songs: List<Song> = arrayListOf()

    class SongViewHolder(val itemBinding: ItemSongBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Song>) {
        songs = data
        notifyDataSetChanged()
    }

    fun getSongs(): List<Song> = songs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val song = songs[position]

        holder.itemBinding.apply {
            removeSongFromPlaylist.visibility =
                if (showRemoveSongFromPlaylist) View.VISIBLE
                else View.GONE

            tvIndex.text = "${position + 1}"
            if (song.imageFile != null) {
                Picasso.get().load(song.imageFile!!).placeholder(R.drawable.bitmap_music)
                    .fit().into(imgSongAvatar)
            } else {
                if (song.image.isNotEmpty()) {
                    Picasso.get().load(song.image).placeholder(R.drawable.bitmap_music)
                        .fit().into(imgSongAvatar)
                }
            }


            tvSongName.text = song.name
            tvAccountName.text = song.singersToString()

            if (position < 3) {
                tvIndex.setTextColor(Constants.colorsTopSong[position])
                tvIndex.typeface = Typeface.DEFAULT_BOLD
            }

            removeSongFromPlaylist.setOnClickListener {
                removeSongFromPlaylistListener?.onRemoveSongFromPlaylist(song, position)
            }
        }

        holder.itemView.setOnClickListener {
            listener.onSongClick(song)
        }

        holder.itemBinding.songMenuMore.setOnClickListener {
            listener.onOpenMenu(song, position)
        }
    }

    override fun getItemCount(): Int = songs.size
}
