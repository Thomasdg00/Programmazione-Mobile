package com.example.myapplication.ui.azienda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewRepository
import com.example.myapplication.data.model.Review
import kotlinx.coroutines.launch

class AziendaRecensioniViewModel(
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {
    private val _recensioni = MutableLiveData<List<Review>>()
    val recensioni: LiveData<List<Review>> = _recensioni

    fun caricaRecensioni(companyId: String) {
        viewModelScope.launch {
            val reviews = reviewRepository.getReviewsForCompany(companyId)
            _recensioni.value = reviews
        }
    }
}
