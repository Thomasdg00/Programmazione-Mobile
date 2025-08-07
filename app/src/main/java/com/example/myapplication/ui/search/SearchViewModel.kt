package com.example.myapplication.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {

    // Lista completa (dati finti d’esempio)
    private val allItems = listOf(
        "Kotlin", "Java", "Android", "Jetpack", "RecyclerView", "LiveData", "ViewModel"
    )

    private val _filteredItems = MutableLiveData<List<String>>().apply {
        value = allItems
    }
    val filteredItems: LiveData<List<String>> = _filteredItems

    fun filterItems(query: String) {
        _filteredItems.value = if (query.isBlank()) {
            allItems
        } else {
            allItems.filter { it.contains(query, ignoreCase = true) }
        }
    }
}