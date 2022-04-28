package com.team28.wondermusic.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.team28.wondermusic.R
import com.team28.wondermusic.common.Helper
import com.team28.wondermusic.data.models.Comment
import com.team28.wondermusic.databinding.ItemCommentReplyBinding

class CommentReplyAdapter(private val listener: CommentClickListener) :
    RecyclerView.Adapter<CommentReplyAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val itemBinding: ItemCommentReplyBinding) :
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
            ItemCommentReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = differ.currentList[position]

        holder.itemBinding.apply {
            imgAvatar.setImageResource(R.drawable.phathuy)
            tvAccountName.text = comment.account.accountName
            tvCommentTime.text = Helper.toDateTimeDistance(comment.dateTime)
            tvCommentContent.text = comment.content

            if (Helper.isMyAccount(comment.account)) {
                layoutAction.visibility = View.VISIBLE
            } else {
                layoutAction.visibility = View.INVISIBLE
            }

            btnEditComment.setOnClickListener {
                listener.onUpdateComment(comment)
            }

            btnDeleteComment.setOnClickListener {
                listener.onDeleteComment(comment)
            }
        }

    }

    override fun getItemCount(): Int = differ.currentList.size
}