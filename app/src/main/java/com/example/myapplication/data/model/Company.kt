package com.example.myapplication.data.model

data class Company(
    val id: String = "",
    val name: String = "",
    val logoUrl: String = "",
    val location: String = "",
    val sector: String = "",
    val averageRating: Double = 0.0,
    val createdBy: String = "")
