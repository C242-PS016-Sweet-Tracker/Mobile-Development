package com.capstone.sweettrack.view.ui.userinformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserInformationViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isSuccess = MutableLiveData(false)
    val isSuccess: LiveData<Boolean> get() = _isSuccess

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun submitUserData(
        name: String,
        age: Int,
        gender: String,
        height: Double,
        weight: Double,
        activityLevel: String,
        diabetesType: String,
        lastBloodSugar: Double
    ) {
        _isLoading.value = true

        // Simulasi pengiriman data
        viewModelScope.launch {
            delay(2000) // Simulasi loading
            _isLoading.value = false
            _isSuccess.value = true
        }
    }
}
