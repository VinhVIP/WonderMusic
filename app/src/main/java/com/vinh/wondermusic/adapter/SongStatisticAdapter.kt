package com.vinh.wondermusic.adapter

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinh.wondermusic.R
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.data.models.singersToString
import com.vinh.wondermusic.databinding.ItemSongBinding
import com.vinh.wondermusic.databinding.ItemSongStatisticBinding

class SongStatisticAdapter(
    private val listener: SongClickListener,
) : RecyclerView.Adapter<SongStatisticAdapter.SongViewHolder>() {

    private var songs: List<Song> = arrayListOf()

    class SongViewHolder(val itemBinding: ItemSongStatisticBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(song: Song) {
            itemBinding.apply {

                tvIndex.text = "${bindingAdapterPosition + 1}"
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
                tvNumListen.text = song.listen.toString()
                tvNumHeart.text = song.like.toString()


                if (bindingAdapterPosition < 3) {
                    tvIndex.setTextColor(Constants.colorsTopSong[bindingAdapterPosition])
                    tvIndex.typeface = Typeface.DEFAULT_BOLD
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Song>) {
        songs = data
        notifyDataSetChanged()
    }

    fun getSongs(): List<Song> = songs

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            ItemSongStatisticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: SongViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val song = songs[position]
        holder.bindData(song)
    }

    override fun getItemCount(): Int = songs.size
}
