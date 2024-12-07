package com.capstone.sweettrack.view.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.remote.response.OTPRequest
import com.capstone.sweettrack.data.remote.response.OTPResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: Repository) : ViewModel() {

    private val _registerResult = MutableLiveData<OTPResponse>()
    val registerResult: LiveData<OTPResponse> get() = _registerResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun requestOTP(username: String, email: String, password: String) {
        _isLoading.value = true
        val otpRequest = OTPRequest(username, email, password)

        viewModelScope.launch {
            try {
                val message = repository.requestOtp(otpRequest)
                if (message.error != true) {
                    _registerResult.value = message
                } else {
                    _registerResult.value = message
                }
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, OTPResponse::class.java)
                _registerResult.value = errorResponse
//                    OTPResponse(
//                    500,
//                    true,
//                    "Gagal Mengirim OTP",
//                    "Error"
//                )
            } catch (e: Exception) {
                _registerResult.value = OTPResponse(
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
