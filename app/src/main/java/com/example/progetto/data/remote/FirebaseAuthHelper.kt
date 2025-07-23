package com.example.progetto.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object FirebaseAuthHelper {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // 🔐 Registrazione nuovo utente
    fun register(
        email: String,
        password: String,
        onResult: (Boolean, FirebaseUser?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, auth.currentUser)
                } else {
                    onResult(false, null)
                }
            }
    }

    // 🔓 Login utente
    fun login(
        email: String,
        password: String,
        onResult: (Boolean, FirebaseUser?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, auth.currentUser)
                } else {
                    onResult(false, null)
                }
            }
    }

    // 🚪 Logout
    fun logout() {
        auth.signOut()
    }

    // 👤 Ottieni l'utente attualmente autenticato
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // 🔁 Verifica se l'utente è autenticato
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // 🆔 Ottieni l'ID dell'utente autenticato
    fun getUserId(): String? {
        return auth.currentUser?.uid
    }

}
