package com.example.myapplication.data.model

data class Review(
    val id: String = "",
    val companyId: String = "",
    val userId: String = "",
    val userName: String = "",
    val rating: Int = 0,
    val ratingAmbiente: Int = 0,
    val ratingRetribuzione: Int = 0,
    val ratingCrescita: Int = 0,
    val ratingWLB: Int = 0,
    val comment: String = "",
    val role: String = "",
    val anonymous: Boolean = false,
    val mediaUrls: List<String> = emptyList(),
    val timestamp: Long = 0L
)
