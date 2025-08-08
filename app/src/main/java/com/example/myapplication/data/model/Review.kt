package com.example.myapplication.data.model

data class Review(
    val id: String = "",
    val companyId: String = "",
    val userId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = 0L
)
