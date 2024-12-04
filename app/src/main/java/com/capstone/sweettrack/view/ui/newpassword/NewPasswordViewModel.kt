package com.capstone.sweettrack.view.ui.newpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.VerifyOtpResetPassword
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import kotlinx.coroutines.launch

class NewPasswordViewModel(private val repository: Repository) : ViewModel() {

    private val _verifyResult = MutableLiveData<VerifyOtpResponse>()
    val verifyResult: LiveData<VerifyOtpResponse> get() = _verifyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    fun verifyOTP(otp: String, password: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val request = VerifyOtpResetPassword(otp, password)
                val response = repository.verifyOtpResetPassRequest (request)
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