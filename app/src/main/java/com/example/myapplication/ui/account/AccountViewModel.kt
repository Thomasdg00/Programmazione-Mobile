package com.example.myapplication.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserProfileRepository
import com.example.myapplication.data.ReviewRepository
import com.example.myapplication.data.model.UserProfile
import com.example.myapplication.data.model.Review
import kotlinx.coroutines.launch

class AccountViewModel(
    private val userProfileRepository: UserProfileRepository = UserProfileRepository(),
    private val reviewRepository: ReviewRepository = ReviewRepository()
) : ViewModel() {
    private val _userProfile = MutableLiveData<UserProfile?>()
    val userProfile: LiveData<UserProfile?> = _userProfile

    private val _userReviews = MutableLiveData<List<Review>>()
    val userReviews: LiveData<List<Review>> = _userReviews

    // Placeholder per upload immagine profilo
    private val _profileImageUploadState = MutableLiveData<Boolean>()
    val profileImageUploadState: LiveData<Boolean> = _profileImageUploadState

    fun uploadProfileImage(userId: String, imageUri: String) {
        // TODO: implementa upload su Firebase Storage e aggiorna il profilo utente
        // Per ora solo placeholder
        _profileImageUploadState.value = false
    }

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _userProfile.value = userProfileRepository.getUserProfile(userId)
            _userReviews.value = reviewRepository.getReviewsByUser(userId)
        }
    }
}
