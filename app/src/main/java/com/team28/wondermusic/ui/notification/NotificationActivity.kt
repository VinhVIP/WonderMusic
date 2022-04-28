package com.team28.wondermusic.ui.notification

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.NotificationAdapter
import com.team28.wondermusic.adapter.NotificationClickListener
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.base.dialogs.ConfirmDialog
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Notification
import com.team28.wondermusic.databinding.ActivityNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : BaseActivity(), NotificationClickListener {

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel by viewModels<NotificationViewModel>()

    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initData()
        setupViews()
    }

    private fun initData() {
        // load các thông báo cũ từ local
        viewModel.getAllNotifications()

        // load các thông báo mới từ server
        viewModel.refreshNotifications()
    }

    private fun setupViews() {
        notificationAdapter = NotificationAdapter(context = this, listener = this)

        viewModel.notifications.observe(this) {
            notificationAdapter.setData(it)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.recyclerNotification.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(this@NotificationActivity)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNotifications()
        }

        viewModel.message.observe(this) {
            it?.let {
                showErrorDialog(it)
            }
        }

        viewModel.readAllStatus.observe(this) {
            if (it) {
                notificationAdapter.readAll()
            }
        }

        viewModel.deleteAllStatus.observe(this) {
            if (it) {
                notificationAdapter.deleteAll()
            }
        }

        viewModel.readStatus.observe(this) {
            if (it) {
                viewModel.changePosition?.let { position ->
                    notificationAdapter.readAt(position)
                }
            }
        }

        viewModel.deleteStatus.observe(this) {
            if (it) {
                viewModel.changePosition?.let { position ->
                    notificationAdapter.removeAt(position)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.notification_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
            R.id.menuReadAllNotification -> viewModel.readAllNotification()
            R.id.menuDeleteAllNotification -> {
                showConfirmDialog(
                    "Xóa tất cả",
                    "Bạn chắc chắn muốn xóa toàn bộ thông báo?",
                    "Xóa",
                    "Hủy",
                    "",
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {
                        }

                        override fun positiveAction() {
                            viewModel.deleteAllNotification()
                        }
                    }
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNotificationClick(notification: Notification, position: Int) {
        viewModel.changePosition = position
        viewModel.readNotification(notification)
    }

    override fun onNotificationLongClick(notification: Notification, position: Int) {
        NotificationMenuFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Notification, notification)
                putInt(Constants.Position, position)
            }
        }.show(supportFragmentManager, null)
    }
}