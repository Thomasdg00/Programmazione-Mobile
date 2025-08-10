package com.example.myapplication.data

import com.example.myapplication.data.model.LoggedInUser

class RegisterRepository(private val dataSource: RegisterDataSource) {
    suspend fun register(email: String, password: String): Result<LoggedInUser> {
        return dataSource.register(email, password)
    }
}
