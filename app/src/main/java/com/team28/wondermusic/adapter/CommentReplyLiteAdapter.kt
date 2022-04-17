package com.team28.wondermusic.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.R
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.databinding.ItemCommentReplyLiteBinding

class CommentReplyLiteAdapter(
    private val parentComment: Comment,
    private val listener: CommentReplyClickListener
) :
    RecyclerView.Adapter<CommentReplyLiteAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val itemBinding: ItemCommentReplyLiteBinding) :
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
        differ.submitList(parentComment.children)

        return CommentViewHolder(
            ItemCommentReplyLiteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.itemBinding.apply {
            imgAvatar.setImageResource(R.drawable.phathuy)
            tvAccountName.text = comment.account.accountName
            tvCommentContent.text = comment.content
        }

        holder.itemView.setOnClickListener {
            listener.onCommentReplyClick(parentComment)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}

interface CommentReplyClickListener {
    fun onCommentReplyClick(comment: Comment)
}