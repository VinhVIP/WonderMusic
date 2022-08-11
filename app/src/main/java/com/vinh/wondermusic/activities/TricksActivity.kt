package com.vinh.wondermusic.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.vinh.wondermusic.R
import com.vinh.wondermusic.base.activities.BaseActivity
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.service.MusicService
import com.vinh.wondermusic.ui.account.AccountActivity
import com.vinh.wondermusic.ui.comment.CommentActivity
import com.vinh.wondermusic.ui.comment.reply.CommentReplyActivity
import com.vinh.wondermusic.ui.home.HomeActivity
import com.vinh.wondermusic.ui.notification.NotificationAction
import com.vinh.wondermusic.ui.player.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TricksActivity : BaseActivity() {

    private val viewModel by viewModels<TricksViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tricks)

        Log.d("vinh", "trick")

        val action = intent?.getStringExtra(Constants.Action)
        if (action == null) {
            gotoHome()
        } else {
            val notificationAction = Helper.getAction(action)

            val ids = Helper.getIds(action)
            if (notificationAction == null) gotoHome()
            else {
                if (ids.isNotEmpty()) {
                    when (notificationAction) {
                        NotificationAction.NEW_SONG -> {
                            playSong(ids[0])
                        }
                        NotificationAction.COMMENT -> {
                            showComment(ids[0])
                        }
                        NotificationAction.REPLY -> {
                            if (ids.size > 1) showChildrenComment(ids[0], ids[1])
                        }
                        NotificationAction.LOVE -> {
                            playSong(ids[0])
                        }
                        NotificationAction.FOLLOW -> {
                            showAccount(ids[0])
                        }
                        NotificationAction.SINGER -> {
                            playSong(ids[0])
                        }
                    }
                } else {
                    gotoHome()
                }
            }
        }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun playSong(idSong: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                gotoHome()
            } else {
                Helper.sendMusicAction(
                    this@TricksActivity,
                    MusicService.ACTION_PLAY,
                    song,
                    ArrayList<Song>().apply { add(song) }
                )
                startActivity(
                    Intent(
                        this@TricksActivity,
                        PlayerActivity::class.java
                    )
                )
                finish()
            }
        }
    }

    private fun showComment(idSong: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                gotoHome()
            } else {
                startActivity(Intent(this@TricksActivity, CommentActivity::class.java).apply {
                    putExtra(Constants.Song, song)
                })
                finish()
            }
        }
    }

    private fun showChildrenComment(idSong: Int, idCommentParent: Int) {
        lifecycleScope.launch {
            val song = viewModel.getSong(idSong)
            if (song == null) {
                gotoHome()
            } else {
                val parentComment = viewModel.getParentComment(idCommentParent)
                parentComment?.let {
                    val intent =
                        Intent(this@TricksActivity, CommentReplyActivity::class.java).apply {
                            putExtra(Constants.Comment, parentComment)
                            putExtra(Constants.Song, song)
                        }
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showAccount(idAccount: Int) {
        lifecycleScope.launch {
            val account = viewModel.getAccount(idAccount)
            if (account == null) {
                gotoHome()
            } else {
                startActivity(Intent(this@TricksActivity, AccountActivity::class.java).apply {
                    putExtra(Constants.Account, account)
                })
                finish()
            }
        }
    }
}