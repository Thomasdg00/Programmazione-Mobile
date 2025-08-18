package com.example.myapplication.data

import com.example.myapplication.data.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import android.net.Uri
import com.example.myapplication.data.model.AccountType
import com.example.myapplication.data.model.AccountType.USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.SetOptions

class UserProfileRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
) {
    suspend fun getUserProfile(userId: String): UserProfile? {
        val doc = firestore.collection("userProfiles").document(userId).get().await()
        return doc.toObject(UserProfile::class.java)
    }

    suspend fun uploadProfileImage(userId: String, imageUri: Uri): String? {
        // Percorso su Firebase Storage
        val ref = storage.reference.child("profile_images/$userId.jpg")
        // Carica immagine
        ref.putFile(imageUri).await()
        // Ottieni url download
        val url = ref.downloadUrl.await().toString()
        // Aggiorna url nel profilo utente
        firestore.collection("userProfiles").document(userId)
            .update("profileImageUrl", url).await()
        return url
    }


    suspend fun updateUserProfile(userProfile: UserProfile) {
        try {
            firestore.collection("userProfiles").document(userProfile.id)
                .set(userProfile, SetOptions.merge()).await()
        } catch (e: Exception) {
            // Logga o rilancia eccezione se vuoi
            throw e
        }
    }
    suspend fun saveUserProfile(userProfile: UserProfile) {
        firestore.collection("userProfiles").document(userProfile.id)
            .set(userProfile).await()
    }


}
