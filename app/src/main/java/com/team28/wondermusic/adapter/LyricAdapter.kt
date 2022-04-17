package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.LineLyric
import com.team28.wondermusic.databinding.ItemLyricBinding

class LyricAdapter(
    private var lyrics: ArrayList<LineLyric>,
    val context: Context,
    val listener: LyricsClickListener
) :
    RecyclerView.Adapter<LyricAdapter.LyricViewHolder>() {

    private var current: Int = -1;

    @SuppressLint("NotifyDataSetChanged")
    fun setData(lyrics: ArrayList<LineLyric>) {
        this.lyrics = lyrics
        notifyDataSetChanged()
    }

    fun currentLine(position: Int) {
        current = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricViewHolder {
        return LyricViewHolder(
            ItemLyricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: LyricViewHolder, position: Int) {
        val line = lyrics[position]

        holder.itemBinding.apply {
            tvLineLyric.text = line.text

            if (current == position) {
                tvLineLyric.setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                tvLineLyric.typeface = Typeface.DEFAULT_BOLD
            } else {
                tvLineLyric.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                tvLineLyric.typeface = Typeface.DEFAULT
            }
        }

        holder.itemView.setOnClickListener {
            listener.onLineLyricClick(line)
        }
    }

    override fun getItemCount(): Int = lyrics.size

    inner class LyricViewHolder(val itemBinding: ItemLyricBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}