package com.team28.wondermusic.ui.notification

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.adapter.NotificationAdapter
import com.team28.wondermusic.adapter.NotificationClickListener
import com.team28.wondermusic.data.models.Notification
import com.team28.wondermusic.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity(), NotificationClickListener {

    private lateinit var binding: ActivityNotificationBinding

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        notificationAdapter = NotificationAdapter(this)
        notificationAdapter.differ.submitList(populateNotificationData())

        binding.recyclerNotification.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }
    }

    private fun populateNotificationData(): ArrayList<Notification> {
        return arrayListOf(
            Notification(1, "Quang Vinh đã nhắc đến bạn trong một bình luận", "", 0, "10/04/2022"),
            Notification(2, "Huy vừa đăng một bài hát mới", "", 0, "8/04/2022"),
            Notification(3, "Long đã nhắc đến bạn trong một bình luận", "", 0, "10/04/2022"),
            Notification(4, "Đăng đã nhắc đến bạn trong một bình luận", "", 0, "10/04/2022"),
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNotificationClick(notification: Notification) {

    }
}