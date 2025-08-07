package com.example.myapplication.data.model

data class Comment(
    val profileImageResId: Int,
    val username: String,
    val isVerified: Boolean,
    val commentText: String,
    val likes: Int
)