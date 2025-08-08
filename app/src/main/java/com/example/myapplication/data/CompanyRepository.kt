package com.example.myapplication.data

import com.example.myapplication.data.model.Company
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CompanyRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getAllCompanies(): List<Company> {
        val snapshot = firestore.collection("companies").get().await()
        return snapshot.documents.mapNotNull { it.toObject(Company::class.java)?.copy(id = it.id) }
    }
}
