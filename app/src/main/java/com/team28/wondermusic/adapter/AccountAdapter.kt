package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Account
import com.team28.wondermusic.databinding.ItemAccountBinding

class AccountAdapter(val listener: ItemAccountClickListener) :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(val itemBinding: ItemAccountBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(
            ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = differ.currentList[position]

        holder.itemBinding.apply {
            Picasso.get().load(account.avatar).fit().into(imgAvatar)
            tvAccountName.text = account.accountName
            tvTotalFollowers.text = "${account.totalFollowers}"
        }

        holder.itemView.setOnClickListener {
            listener.onAccountClick(account)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

interface ItemAccountClickListener {
    fun onAccountClick(account: Account)
}