package com.example.myapplication.ui.review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.ReviewRepository
import com.example.myapplication.data.model.Review
import kotlinx.coroutines.launch

class ReviewViewModel(
    fun updateReview(review: Review) {
        viewModelScope.launch {
            repository.updateReview(review)
            getReviewsForCompany(review.companyId)
        }
    }

    fun deleteReview(review: Review) {
        viewModelScope.launch {
            repository.deleteReview(review.id)
            getReviewsForCompany(review.companyId)
        }
    }
    private val repository: ReviewRepository = ReviewRepository()
) : ViewModel() {
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    fun getReviewsForCompany(companyId: String) {
        viewModelScope.launch {
            _reviews.value = repository.getReviewsForCompany(companyId)
        }
    }

    fun addReview(review: Review) {
        viewModelScope.launch {
            repository.addReview(review)
            getReviewsForCompany(review.companyId)
        }
    }
}
