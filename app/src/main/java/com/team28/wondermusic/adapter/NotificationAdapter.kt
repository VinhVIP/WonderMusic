package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Notification
import com.team28.wondermusic.databinding.ItemNotificationBinding


class NotificationAdapter(
    private val listener: NotificationClickListener
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val itemBinding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.idNotification == newItem.idNotification
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = differ.currentList[position]

        holder.itemBinding.apply {
            val transformation: Transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(30f)
                .oval(false)
                .build()

            Picasso.get()
                .load(R.drawable.phathuy)
                .fit()
                .transform(transformation)
                .into(imgAvatar)

            imgSong.setImageResource(R.drawable.phathuy)
            tvAccountName.text = notification.account?.accountName
            tvNotificationTime.text = notification.notificationTime
            tvNotificationContent.text = notification.content
        }

        holder.itemView.setOnClickListener {
            listener.onNotificationClick(notification)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

interface NotificationClickListener {
    fun onNotificationClick(notification: Notification)
}