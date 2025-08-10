package com.example.myapplication.data


import com.example.myapplication.data.model.LoggedInUser
import com.example.myapplication.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.IOException

class RegisterDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun register(email: String, password: String, fullName: String = "", type: String = "standard"): Result<LoggedInUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                // Salva profilo utente su Firestore
                val userProfile = UserProfile(
                    id = user.uid,
                    fullName = fullName,
                    // puoi aggiungere altri campi se vuoi
                )
                firestore.collection("userProfiles").document(user.uid).set(userProfile).await()
                val loggedInUser = LoggedInUser(user.uid, user.email ?: "")
                Result.Success(loggedInUser)
            } else {
                Result.Error(IOException("User not created"))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error registering", e))
        }
    }
}
