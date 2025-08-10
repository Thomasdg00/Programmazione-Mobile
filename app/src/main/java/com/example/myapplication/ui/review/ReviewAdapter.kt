package com.example.myapplication.ui.review

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.model.Review
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    private var reviews: List<Review> = emptyList()

    fun submitList(list: List<Review>) {
        reviews = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
    }

    override fun getItemCount() = reviews.size

    var currentUserId: String? = null
    var onEdit: ((Review) -> Unit)? = null
    var onDelete: ((Review) -> Unit)? = null

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(review: Review, currentUserId: String?, onEdit: ((Review) -> Unit)?, onDelete: ((Review) -> Unit)?) {
            val buttonEdit = itemView.findViewById<android.widget.Button>(R.id.buttonEditReview)
            val buttonDelete = itemView.findViewById<android.widget.Button>(R.id.buttonDeleteReview)
            if (review.userId == currentUserId) {
                buttonEdit.visibility = View.VISIBLE
                buttonDelete.visibility = View.VISIBLE
                buttonEdit.setOnClickListener { onEdit?.invoke(review) }
                buttonDelete.setOnClickListener { onDelete?.invoke(review) }
            } else {
                buttonEdit.visibility = View.GONE
                buttonDelete.visibility = View.GONE
            }
            val userName = if (review.anonymous) "Anonimo" else review.userName.ifBlank { review.userId }
            itemView.findViewById<TextView>(R.id.textViewReviewUser).text = userName
            itemView.findViewById<TextView>(R.id.textViewReviewRole).text = review.role
            itemView.findViewById<TextView>(R.id.textViewReviewRatings).text =
                "Ambiente: ${review.ratingAmbiente}  Retribuzione: ${review.ratingRetribuzione}  Crescita: ${review.ratingCrescita}  WLB: ${review.ratingWLB}"
            itemView.findViewById<TextView>(R.id.textViewReviewComment).text = review.comment
            val date = Date(review.timestamp)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            itemView.findViewById<TextView>(R.id.textViewReviewDate).text = sdf.format(date)
            // Media
            val mediaContainer = itemView.findViewById<android.widget.LinearLayout>(R.id.mediaContainer)
            val mediaScroll = itemView.findViewById<android.widget.HorizontalScrollView>(R.id.mediaScrollView)
            mediaContainer.removeAllViews()
            if (review.mediaUrls.isNotEmpty()) {
                mediaScroll.visibility = View.VISIBLE
                for (url in review.mediaUrls) {
                    val image = android.widget.ImageView(itemView.context)
                    val params = android.widget.LinearLayout.LayoutParams(180, 180)
                    params.setMargins(0,0,8,0)
                    image.layoutParams = params
                    image.scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                    com.bumptech.glide.Glide.with(itemView).load(url).into(image)
                    mediaContainer.addView(image)
                }
            } else {
                mediaScroll.visibility = View.GONE
            }
            // Pulsanti modifica/cancella solo per autore
            // TODO: aggiungi pulsanti e logica se necessario
        }
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position], currentUserId, onEdit, onDelete)
    }
}
