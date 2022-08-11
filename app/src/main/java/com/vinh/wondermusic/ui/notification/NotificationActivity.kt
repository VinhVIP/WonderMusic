package com.vinh.wondermusic.ui.notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.NotificationAdapter
import com.vinh.wondermusic.adapter.NotificationClickListener
import com.vinh.wondermusic.base.activities.BaseActivity
import com.vinh.wondermusic.base.dialogs.ConfirmDialog
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.common.Helper.getAction
import com.vinh.wondermusic.common.Helper.getIds
import com.vinh.wondermusic.data.models.Notification
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.databinding.ActivityNotificationBinding
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.account.AccountActivity
import com.vinh.wondermusic.ui.comment.CommentActivity
import com.vinh.wondermusic.ui.comment.reply.CommentReplyActivity
import com.vinh.wondermusic.ui.notification.NotificationAction.*
import com.vinh.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                viewModel.refreshNotifications()
            }
        }

        viewModel.deleteAllStatus.observe(this) {
            if (it) {
                notificationAdapter.deleteAll()
                viewModel.refreshNotifications()
            }
        }

        viewModel.readStatus.observe(this) {
            if (it) {
                viewModel.changePosition?.let { position ->
                    notificationAdapter.readAt(position)
                }
                viewModel.refreshNotifications()
            }
        }

        viewModel.deleteStatus.observe(this) {
            if (it) {
                viewModel.changePosition?.let { position ->
                    notificationAdapter.removeAt(position)
                }
                viewModel.refreshNotifications()
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

        val action = getAction(notification.action)
        val ids = getIds(notification.action)
        Log.d("vinhid", "${notification.action} - ${ids.size}")

        for (i in ids) {
            Log.d("vinhid", "$i")
        }

        action?.let {
            if (ids.isNotEmpty()) {
                when (it) {
                    NEW_SONG -> {
                        playSong(ids[0])
                    }
                    COMMENT -> {
                        showComment(ids[0])
                    }
                    REPLY -> {
                        if (ids.size > 1) showChildrenComment(ids[0], ids[1])
                    }
                    LOVE -> {
                        playSong(ids[0])
                    }
                    FOLLOW -> {
                        showAccount(ids[0])
                    }
                    SINGER -> {
                        playSong(ids[0])
                    }
                }
            }
        }
    }

    override fun onNotificationLongClick(notification: Notification, position: Int) {
        NotificationMenuFragment().apply {
            arguments = Bundle().apply {
                putParcelable(Constants.Notification, notification)
                putInt(Constants.Position, position)
            }
        }.show(supportFragmentManager, null)
    }

    private fun playSong(idSong: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                Toast.makeText(
                    this@NotificationActivity,
                    "Không tìm thấy bài hát này!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Helper.sendMusicAction(
                    this@NotificationActivity,
                    MusicService.ACTION_PLAY,
                    song,
                    ArrayList<Song>().apply { add(song) }
                )
                startActivity(
                    Intent(
                        this@NotificationActivity,
                        PlayerActivity::class.java
                    )
                )
            }
        }
    }

    private fun showComment(idSong: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                Toast.makeText(
                    this@NotificationActivity,
                    "Không tìm thấy bài hát này chứa bình luận này!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(this@NotificationActivity, CommentActivity::class.java).apply {
                    putExtra(Constants.Song, song)
                })
            }
        }
    }

    private fun showChildrenComment(idSong: Int, idCommentParent: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                Toast.makeText(
                    this@NotificationActivity,
                    "Không tìm thấy bài hát này chứa bình luận này!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val parentComment = viewModel.getParentComment(idCommentParent)
                parentComment?.let {
                    val intent =
                        Intent(this@NotificationActivity, CommentReplyActivity::class.java).apply {
                            putExtra(Constants.Comment, parentComment)
                            putExtra(Constants.Song, song)
                        }
                    startActivity(intent)
                }
            }
        }
    }

    private fun showAccount(idAccount: Int) {
        lifecycleScope.launch {
            val account = viewModel.getAccount(idAccount)
            if (account == null) {
                Toast.makeText(
                    this@NotificationActivity,
                    "Không tìm thấy tài khoản này!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(Intent(this@NotificationActivity, AccountActivity::class.java).apply {
                    putExtra(Constants.Account, account)
                })
            }
        }
    }


}