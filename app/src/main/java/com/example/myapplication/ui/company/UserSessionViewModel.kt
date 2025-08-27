package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserSessionViewModel : ViewModel() {

    // Tipo di account dell'utente (es: "standard" o "aziendale")
    private val _accountType = MutableLiveData<String>()
    val accountType: LiveData<String> get() = _accountType

    // ID utente loggato (può servirti in altri fragment)
    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String> get() = _userId

    // Nome o display name
    private val _displayName = MutableLiveData<String>()
    val displayName: LiveData<String> get() = _displayName

    // Setter per aggiornare i dati della sessione
    fun setAccountType(type: String) {
        _accountType.value = type
    }

    fun setUserId(id: String) {
        _userId.value = id
    }

    fun setDisplayName(name: String) {
        _displayName.value = name
    }

    // Funzione per resettare i dati in caso di logout
    fun clearSession() {
        _accountType.value = ""
        _userId.value = ""
        _displayName.value = ""
    }
}
