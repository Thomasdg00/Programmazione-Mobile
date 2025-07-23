package com.example.progetto.data.model

class Review(
    val id: String = "",
    val userId: String = "",
    val companyId: String = "",
    val userRole: String = "",
    val isAnonymous: Boolean = true,
    val ratings: Map<String, Float> = emptyMap(), // ambiente, retribuzione, ecc.
    val comment: String = "",
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
) {
}