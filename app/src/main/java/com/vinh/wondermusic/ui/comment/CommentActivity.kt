package com.vinh.wondermusic.ui.comment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.vinh.wondermusic.R
import com.vinh.wondermusic.adapter.CommentAdapter
import com.vinh.wondermusic.adapter.CommentClickListener
import com.vinh.wondermusic.base.activities.BaseActivity
import com.vinh.wondermusic.base.dialogs.ConfirmDialog.ConfirmCallback
import com.vinh.wondermusic.common.Constants
import com.vinh.wondermusic.common.DataLocal
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Comment
import com.vinh.wondermusic.data.models.Song
import com.vinh.wondermusic.databinding.ActivityCommentBinding
import com.vinh.wondermusic.ui.comment.reply.CommentReplyActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentActivity : BaseActivity(), CommentClickListener {

    private lateinit var binding: ActivityCommentBinding
    private val viewModel by viewModels<CommentViewModel>()

    private lateinit var song: Song

    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Các bình luận của bài hát này
        val song: Song? = intent.getParcelableExtra(Constants.Song)
        if (song == null) finish()
        else this.song = song


        refresh()

        setup()
        observers()

    }

    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.getALlComments(song)
    }

    private fun observers() {
        viewModel.rootComments.observe(this) {
            binding.swipeRefresh.isRefreshing = false
            commentAdapter.differ.submitList(it)
        }

        viewModel.sendStatus.observe(this) {
            it?.let {
                if (it) {
                    refresh()
                    binding.layoutDetail.visibility = View.GONE
                } else {
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
            viewModel.sendStatus.postValue(null)
        }

        viewModel.deleteStatus.observe(this) {
            it?.let {
                if (it) {
                    refresh()
                }
                Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                viewModel.deleteStatus.postValue(null)
            }
        }
    }

    private fun setup() {
        Helper.setStatusBarGradiant(this, R.color.main_bg_color)

        binding.toolbar.title = "Bình luận"
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        commentAdapter = CommentAdapter(this)

        binding.recyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@CommentActivity)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getALlComments(song)
        }

        if (DataLocal.myAccount.avatar.isNotEmpty()) {
            Picasso.get().load(DataLocal.myAccount.avatar).placeholder(R.drawable.ic_user_colorful)
                .fit().into(binding.imgAvatar)
        }

        binding.btnSendComment.setOnClickListener {
            val content = binding.edComment.text.toString().trim()
            if (content.isNotEmpty()) {

                if (viewModel.isUpdateComment()) {
                    viewModel.updateComment(song, viewModel.commentUpdate!!, content)
                } else {
                    viewModel.addComment(song, content)
                }
                binding.edComment.setText("")
            }
        }

        binding.btnCancel.setOnClickListener {
            viewModel.commentUpdate = null
            binding.layoutDetail.visibility = View.GONE
        }
    }

    override fun onViewChildrenComment(parentComment: Comment) {
        val intent = Intent(this, CommentReplyActivity::class.java).apply {
            putExtra(Constants.Comment, parentComment)
            putExtra(Constants.Song, song)
        }
        startActivity(intent)
    }

    override fun onReplyComment(comment: Comment) {
        this.onViewChildrenComment(comment)
    }

    override fun onUpdateComment(comment: Comment) {
        binding.edComment.setText(comment.content)

        binding.layoutDetail.visibility = View.VISIBLE
        viewModel.commentUpdate = comment
    }

    override fun onDeleteComment(comment: Comment) {
        this.showConfirmDialog(
            "Xác nhận xóa",
            "Các bình luận trả lời cũng sẽ bị xóa. Bạn chắc chắn muốn xóa?",
            "Xóa",
            "Hủy",
            "",
            object : ConfirmCallback {
                override fun negativeAction() {
                }

                override fun positiveAction() {
                    viewModel.deleteComment(song, comment)
                }
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}