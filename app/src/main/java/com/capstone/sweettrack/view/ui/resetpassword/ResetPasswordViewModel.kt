package com.capstone.sweettrack.view.ui.resetpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.OTPResetPassRequest
import com.capstone.sweettrack.data.remote.response.OTPResponse
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ResetPasswordViewModel(private val repository: Repository) : ViewModel() {

    private val _requestResult = MutableLiveData<VerifyOtpResponse>()
    val requestResult: LiveData<VerifyOtpResponse> get() = _requestResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun requestOTP(email: String) {
        _isLoading.value = true
        val otpRequest = OTPResetPassRequest(email)

        viewModelScope.launch {
            try {
                val message = repository.otpResetPassRequest(otpRequest)
                if (!message.error) {
                    _requestResult.value = message
                } else {
                    _requestResult.value = message
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, OTPResponse::class.java)
                _requestResult.value =
                    VerifyOtpResponse(
                        500,
                        true,
                        "Gagal Mengirim OTP",
                        "Error"
                    )
            } catch (e: Exception) {
                _requestResult.value = VerifyOtpResponse(
                    500,
                    true,
                    "Gagal SignUp",
                    "Kesalahan Jaringan"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

}