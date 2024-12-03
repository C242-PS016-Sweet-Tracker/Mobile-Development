package com.capstone.sweettrack.view.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val repository: Repository) : ViewModel() {

    private val _verifyResult = MutableLiveData<VerifyOtpResponse>()
    val verifyResult: LiveData<VerifyOtpResponse> get() = _verifyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun verifyOTP(email: String, otp: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val request = VerifyOtpRequest(email, otp, password)
                val response = repository.verifyOtp(request)
                _verifyResult.value = response
            } catch (e: Exception) {
                _verifyResult.value = VerifyOtpResponse(
                    statusCode = 500,
                    error = true,
                    message = "error",
                    describe = e.message ?: "Terjadi kesalahan"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

}