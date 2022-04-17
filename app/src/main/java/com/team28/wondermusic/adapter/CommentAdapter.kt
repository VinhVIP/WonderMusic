package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.databinding.ItemCommentWithReplyBinding

class CommentAdapter(private val listener: CommentClickListener) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(), CommentReplyClickListener {

    inner class CommentViewHolder(val itemBinding: ItemCommentWithReplyBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.idComment == newItem.idComment
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            ItemCommentWithReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.itemBinding.apply {
            imgAvatar.setImageResource(R.drawable.phathuy)
            tvAccountName.text = comment.account.accountName
            tvCommentTime.text = comment.dateTime
            tvCommentContent.text = comment.content

            comment.children?.let {
                val replyAdapter = CommentReplyLiteAdapter(comment, this@CommentAdapter)
                replyAdapter.differ.submitList(comment.children)

                recyclerReply.apply {
                    adapter = replyAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCommentReplyClick(comment: Comment) {
        listener.onViewChildrenComment(comment)
    }
}

interface CommentClickListener {
    fun onViewChildrenComment(parentComment: Comment)
}