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

    suspend fun getCompany(companyId: String): Company? {
        val doc = firestore.collection("companies").document(companyId).get().await()
        return doc.toObject(Company::class.java)?.copy(id = doc.id)
    }

    suspend fun addCompany(company: Company): String {
        val docRef = firestore.collection("companies").add(company).await()
        return docRef.id
    }

    suspend fun updateCompany(company: Company) {
        firestore.collection("companies").document(company.id).set(company).await()
    }

    suspend fun deleteCompany(companyId: String) {
        firestore.collection("companies").document(companyId).delete().await()
    }
}
