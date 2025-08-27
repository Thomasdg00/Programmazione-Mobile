package com.example.myapplication.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.LoginDataSource
import com.example.myapplication.data.LoginRepository
import com.example.myapplication.data.UserProfileRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory (
    private val repository: LoginRepository,
    private val userProfileRepository: UserProfileRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginRepository = repository,
                    userProfileRepository = userProfileRepository
                ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}