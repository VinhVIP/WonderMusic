package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.ItemSingerBinding

class SingerAdapter(private val listener: AccountClickListener) :
    RecyclerView.Adapter<SingerAdapter.SingerViewHolder>() {

    inner class SingerViewHolder(val itemBinding: ItemSingerBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Account>() {
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem.idAccount == newItem.idAccount
        }

        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        return SingerViewHolder(
            ItemSingerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SingerViewHolder, position: Int) {
        val account = differ.currentList[position]

        holder.itemBinding.apply {
            if (account.avatar.isNotEmpty()) {
                Picasso.get().load(account.avatar).fit().into(imgAvatar)
            } else {
                Picasso.get().load(R.drawable.profile).fit().into(imgAvatar)
            }
            tvSingerName.text = account.accountName
        }

        holder.itemView.setOnClickListener {
            listener.onAccountClick(account)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}
