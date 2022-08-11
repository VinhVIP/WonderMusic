package com.vinh.wondermusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.vinh.wondermusic.R
import com.vinh.wondermusic.common.Helper
import com.vinh.wondermusic.data.models.Comment
import com.vinh.wondermusic.databinding.ItemCommentReplyBinding

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
            if (comment.account.avatar.isNotEmpty()) {
                Picasso.get().load(comment.account.avatar).placeholder(R.drawable.ic_user_colorful)
                    .fit().into(imgAvatar)
            }
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