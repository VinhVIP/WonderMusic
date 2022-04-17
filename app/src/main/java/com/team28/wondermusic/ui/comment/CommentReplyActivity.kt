package com.team28.wondermusic.ui.comment

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.team28.wondermusic.R
import com.team28.wondermusic.adapter.CommentReplyAdapter
import com.team28.wondermusic.common.Constants
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.databinding.ActivityCommentReplyBinding

class CommentReplyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentReplyBinding

    private lateinit var replyAdapter: CommentReplyAdapter

    private var parentComment: Comment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        replyAdapter = CommentReplyAdapter()

        binding.recyclerReply.apply {
            adapter = replyAdapter
            layoutManager = LinearLayoutManager(this@CommentReplyActivity)
        }

        getData()
    }

    private fun getData() {
        parentComment = intent.getParcelableExtra(Constants.Comment)

        parentComment?.let { parentComment ->
            binding.parentComment.imgAvatar.setImageResource(R.drawable.phathuy)
            binding.parentComment.tvAccountName.text = parentComment.account.accountName
            binding.parentComment.tvCommentTime.text = parentComment.dateTime
            binding.parentComment.tvCommentContent.text = parentComment.content

            parentComment.children?.let {
                replyAdapter.differ.submitList(it)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}