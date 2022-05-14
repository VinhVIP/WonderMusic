package com.team28.wondermusic.ui.comment.reply

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.CommentClickListener
import com.team28.wondermusic.adapter.CommentReplyAdapter
import com.team28.wondermusic.base.activities.BaseActivity
import com.team28.wondermusic.base.dialogs.ConfirmDialog
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.common.DataLocal
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.data.models.Song
import com.team28.wondermusic.databinding.ActivityCommentReplyBinding
import com.team28.wondermusic.ui.comment.CommentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentReplyActivity : BaseActivity(), CommentClickListener {

    private lateinit var binding: ActivityCommentReplyBinding
    private val viewModel by viewModels<CommentViewModel>()

    private lateinit var replyAdapter: CommentReplyAdapter

    private lateinit var parentComment: Comment
    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
        observers()
        getData()
    }

    private fun getData() {
        song = intent.getParcelableExtra(Constants.Song)!!
        parentComment = intent.getParcelableExtra(Constants.Comment)!!

        updateOnlyParentComment(parentComment)

        parentComment.children.let {
            replyAdapter.differ.submitList(it)
        }

        if (Helper.isMyAccount(parentComment.account)) {
            binding.parentComment.apply {
                btnEditComment.visibility = View.VISIBLE
                btnDeleteComment.visibility = View.VISIBLE
                dot1.visibility = View.VISIBLE
                dot2.visibility = View.VISIBLE
            }
        }

        binding.edComment.hint = "Trả lời ${parentComment.account.accountName}"

    }

    private fun updateOnlyParentComment(parentCmt: Comment) {
        binding.parentComment.apply {
            if (parentComment.account.avatar.isNotEmpty()) {
                Picasso.get().load(parentComment.account.avatar)
                    .placeholder(R.drawable.ic_user_colorful).fit().into(imgAvatar)
            }
            tvAccountName.text = parentCmt.account.accountName
            tvCommentTime.text = Helper.toDateTimeDistance(parentCmt.dateTime)
            tvCommentContent.text = parentCmt.content
        }
    }

    private fun observers() {
        viewModel.isLoading.observe(this) {
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.parentComment.observe(this) {
            updateOnlyParentComment(it)
            replyAdapter.differ.submitList(it.children)
        }

        viewModel.sendStatus.observe(this) {
            it?.let {
                if (it) {
                    refresh()
                    binding.layoutDetail.visibility = View.GONE
                    viewModel.commentUpdate = null
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

        viewModel.deleteParentStatus.observe(this) {
            it?.let {
                if (it) {
                    Toast.makeText(this, viewModel.message, Toast.LENGTH_SHORT).show()
                    viewModel.deleteStatus.postValue(null)
                    finish()
                }
            }
        }
    }

    private fun setup() {
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        replyAdapter = CommentReplyAdapter(this)

        binding.recyclerReply.apply {
            adapter = replyAdapter
            layoutManager = LinearLayoutManager(this@CommentReplyActivity)
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getCommentAndChildren(parentComment)
        }

        if (DataLocal.myAccount.avatar.isNotEmpty()) {
            Log.d("vinhimg", DataLocal.myAccount.avatar)
            Picasso.get().load(DataLocal.myAccount.avatar).placeholder(R.drawable.ic_user_colorful)
                .fit().into(binding.imgUser)
        }

        binding.btnSendComment.setOnClickListener {
            val content = binding.edComment.text.toString().trim()
            if (content.isNotEmpty()) {
                if (viewModel.isUpdateComment()) {
                    viewModel.updateComment(song, viewModel.commentUpdate!!, content)
                } else {
                    viewModel.replyComment(song, parentComment, content)
                }
                binding.edComment.setText("")
            }
        }

        binding.btnCancel.setOnClickListener {
            viewModel.commentUpdate = null
            binding.layoutDetail.visibility = View.GONE
        }

        binding.parentComment.btnEditComment.setOnClickListener {
            onUpdateComment(parentComment)
        }

        binding.parentComment.btnDeleteComment.setOnClickListener {
            viewModel.deleteParentComment(song, parentComment)
        }
    }

    private fun refresh() {
        binding.swipeRefresh.isRefreshing = true
        viewModel.getCommentAndChildren(parentComment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
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
            object : ConfirmDialog.ConfirmCallback {
                override fun negativeAction() {
                }

                override fun positiveAction() {
                    viewModel.deleteComment(song, comment)
                }
            }
        )
    }

    override fun onViewChildrenComment(parentComment: Comment) {
    }

    override fun onReplyComment(comment: Comment) {
    }

}