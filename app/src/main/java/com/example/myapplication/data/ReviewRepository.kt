package com.example.myapplication.data

import com.example.myapplication.data.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReviewRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getReviewsForCompany(companyId: String): List<Review> {
        val snapshot = firestore.collection("reviews")
            .whereEqualTo("companyId", companyId)
            .get().await()
        return snapshot.documents.mapNotNull { it.toObject(Review::class.java)?.copy(id = it.id) }
    }

    suspend fun addReview(review: Review) {
        firestore.collection("reviews").add(review).await()
    }

    suspend fun updateReview(review: Review) {
        firestore.collection("reviews").document(review.id).set(review).await()
    }

    suspend fun deleteReview(reviewId: String) {
        firestore.collection("reviews").document(reviewId).delete().await()
    }

    suspend fun getReviewsByUser(userId: String): List<Review> {
        val snapshot = firestore.collection("reviews")
            .whereEqualTo("userId", userId)
            .get().await()
        return snapshot.documents.mapNotNull { it.toObject(Review::class.java)?.copy(id = it.id) }
    }
}
