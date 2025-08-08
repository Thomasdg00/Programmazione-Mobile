package com.example.myapplication.data

import com.example.myapplication.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getUserProfile(userId: String): UserProfile? {
        val doc = firestore.collection("userProfiles").document(userId).get().await()
        return doc.toObject(UserProfile::class.java)
    }
}
