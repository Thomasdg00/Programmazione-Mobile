package com.example.myapplication.data


import com.example.myapplication.data.model.LoggedInUser
import com.example.myapplication.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<LoggedInUser> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Recupera profilo utente da Firestore
                val doc = firestore.collection("userProfiles").document(user.uid).get().await()
                val userProfile = doc.toObject(UserProfile::class.java)
                val displayName = userProfile?.fullName ?: (user.email ?: "")
                val loggedInUser = LoggedInUser(user.uid, displayName)
                Result.Success(loggedInUser)
            } else {
                Result.Error(IOException("User not found"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        auth.signOut()
    }
}