package com.example.myapplication.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Comment

class CommentAdapter(private var comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile: ImageView = itemView.findViewById(R.id.imageProfile)
        val textUsername: TextView = itemView.findViewById(R.id.textUsername)
        val imageBadge: ImageView = itemView.findViewById(R.id.imageBadge)
        val textComment: TextView = itemView.findViewById(R.id.textComment)
        val textLikes: TextView = itemView.findViewById(R.id.textLikes)
        val textReply: TextView = itemView.findViewById(R.id.textReply)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.imageProfile.setImageResource(comment.profileImageResId)
        holder.textUsername.text = comment.username
        holder.textComment.text = comment.commentText
        holder.textLikes.text = "👍 ${comment.likes}"
        holder.imageBadge.visibility = if (comment.isVerified) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = comments.size
    fun updateComments(newComments: List<Comment>) {
        comments = newComments
        notifyDataSetChanged()
    }
}
