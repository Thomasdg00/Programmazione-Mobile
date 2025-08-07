package com.example.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.data.model.Comment

class HomeViewModel : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    init {
        // Esempio di dati statici
        _comments.value = listOf(
            Comment(R.drawable.sharp_account_circle_24, "Mario", true, "Bel video!", 12),
            Comment(R.drawable.sharp_account_circle_24, "Giulia", false, "Non sono d'accordo", 3)
        )
    }
}