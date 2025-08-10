package com.example.myapplication.ui.register

/**
 * Registration result : success (user details) or error message.
 */
data class RegisterResult(
    val success: com.example.myapplication.ui.login.LoggedInUserView? = null,
    val error: Int? = null
)
