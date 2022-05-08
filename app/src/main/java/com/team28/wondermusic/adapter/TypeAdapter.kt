package com.team28.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.data.models.Type
import com.team28.wondermusic.databinding.ItemTypeBinding

class TypeAdapter(
    private val listener: TypeClickListener
) :
    RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

    inner class TypeViewHolder(val itemBinding: ItemTypeBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Type>() {
        override fun areItemsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem.idType == newItem.idType
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Type, newItem: Type): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        return TypeViewHolder(
            ItemTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = differ.currentList[position]

        holder.itemBinding.apply {
//            Picasso.get().load(song.image).fit().into(imgAvatar)
            tvTypeName.text = type.name
        }

        holder.itemView.setOnClickListener {
            listener.onTypeClick(type)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}