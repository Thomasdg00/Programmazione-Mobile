package com.example.myapplication.data.model

data class UserProfile(
    val id: String = "",
    val fullName: String = "",
    val age: Int = 0,
    val currentJob: String = "",
    val pastJobs: List<String> = emptyList(),
    val profileImageUrl: String = ""
)
