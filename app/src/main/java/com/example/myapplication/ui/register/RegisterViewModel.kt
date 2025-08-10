package com.example.myapplication.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.RegisterRepository
import com.example.myapplication.data.Result
import com.example.myapplication.ui.login.LoggedInUserView
import kotlinx.coroutines.launch
import com.example.myapplication.R

class RegisterViewModel(private val registerRepository: RegisterRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult

    fun register(email: String, password: String) {
        viewModelScope.launch {
            val result = registerRepository.register(email, password)
            if (result is Result.Success) {
                _registerResult.value = RegisterResult(success = LoggedInUserView(result.data.displayName))
            } else {
                _registerResult.value = RegisterResult(error = R.string.registration_failed)
            }
        }
    }
}
