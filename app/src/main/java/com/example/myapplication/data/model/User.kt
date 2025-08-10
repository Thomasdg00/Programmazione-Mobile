package com.example.myapplication.data.model

data class User(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val accountType: AccountType = AccountType.USER,
    val companyId: String = "",  // Solo per account aziendali
    val profileImageUrl: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class AccountType {
    USER, COMPANY
}