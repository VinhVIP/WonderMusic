package com.vinh.wondermusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.vinh.wondermusic.R
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Notification
import com.vinh.wondermusic.databinding.ItemNotificationBinding


class NotificationAdapter(
    private var list: MutableList<Notification> = mutableListOf(),
    private val context: Context,
    private val listener: NotificationClickListener
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val itemBinding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Notification>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun readAll() {
        list.forEach { it.notificationStatus = 1 }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteAll() {
        list.clear()
        notifyDataSetChanged()
    }

    fun readAt(position: Int) {
        list[position].notificationStatus = 1
        notifyItemChanged(position)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = list[position]

        holder.itemBinding.apply {
            tvNotificationTime.text = Helper.toDateTimeDistance(notification.notificationTime)
            tvNotificationContent.text = notification.content

            if (notification.notificationStatus == 0) {
                tvNotificationContent.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.text_primary
                    )
                )
            } else {
                tvNotificationContent.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.text_secondary
                    )
                )
            }
        }

        holder.itemView.setOnClickListener {
            listener.onNotificationClick(notification, position)
        }
        holder.itemView.setOnLongClickListener {
            listener.onNotificationLongClick(notification, position)
            false
        }
        holder.itemBinding.btnMore.setOnClickListener {
            listener.onNotificationLongClick(notification, position)
        }
    }

    override fun getItemCount(): Int = list.size
}

interface NotificationClickListener {
    fun onNotificationClick(notification: Notification, position: Int)
    fun onNotificationLongClick(notification: Notification, position: Int)
}