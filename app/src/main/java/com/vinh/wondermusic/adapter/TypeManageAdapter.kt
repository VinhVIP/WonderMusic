package com.vinh.wondermusic.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vinh.wondermusic.data.models.Type
import com.vinh.wondermusic.databinding.ItemTypeManageBinding

class TypeManageAdapter(
    private val listener: TypeManageListener
) :
    RecyclerView.Adapter<TypeManageAdapter.TypeViewHolder>() {

    inner class TypeViewHolder(val itemBinding: ItemTypeManageBinding) :
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
            ItemTypeManageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val type = differ.currentList[position]

        holder.itemBinding.apply {
            tvTypeName.text = type.name
            btnDeleteType.setOnClickListener {
                listener.onTypeDelete(type, position)
            }
        }

        holder.itemView.setOnClickListener {
            listener.onTypeEdit(type, position)
        }

    }

    override fun getItemCount(): Int = differ.currentList.size
}

interface TypeManageListener {
    fun onTypeEdit(type: Type, position: Int)
    fun onTypeDelete(type: Type, position: Int)
}
