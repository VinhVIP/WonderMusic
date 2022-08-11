package com.vinh.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinh.wondermusic.data.models.MenuBottom
import com.vinh.wondermusic.data.models.MenuBottomClickListener
import com.vinh.wondermusic.databinding.ItemMenuBinding

class MenuBottomAdapter(private val listener: MenuBottomClickListener) :
    RecyclerView.Adapter<MenuBottomAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(val itemBinding: ItemMenuBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MenuBottom>() {
        override fun areItemsTheSame(oldItem: MenuBottom, newItem: MenuBottom): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: MenuBottom, newItem: MenuBottom): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = differ.currentList[position]

        holder.itemBinding.apply {
            tvMenuTitle.text = menu.title
            imgMenu.setImageResource(menu.drawableRes)
        }

        holder.itemView.setOnClickListener {
            listener.onMenuClick(menu)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size


}