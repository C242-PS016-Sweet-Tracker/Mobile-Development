package com.capstone.sweettrack.view.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.sweettrack.data.Repository
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.remote.response.LoginRequest
import com.capstone.sweettrack.data.remote.response.LoginResponse
import com.capstone.sweettrack.data.remote.response.ResendingOTPRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthenticationViewModel(private val repository: Repository) : ViewModel() {

    private val _verifyResult = MutableLiveData<VerifyOtpResponse>()
    val verifyResult: LiveData<VerifyOtpResponse> get() = _verifyResult

    private val _resendingResult = MutableLiveData<VerifyOtpResponse>()
    val resendingResult: LiveData<VerifyOtpResponse> get() = _resendingResult

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> get() = _loginResult

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

    fun resendOTP(email: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val request = ResendingOTPRequest(email)
                val response = repository.resendingOtp(request)
                _resendingResult.value = response
            } catch (e: Exception) {
                _resendingResult.value = VerifyOtpResponse(
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

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(email, password)
                val response = repository.login(loginRequest)
                _loginResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val parsedError = Gson().fromJson(errorBody, LoginResponse::class.java)
                val errorResponse = LoginResponse(
                    error = true,
                    message = parsedError?.message ?: "Login gagal"
                )
                _loginResult.postValue(errorResponse)
            } catch (e: Exception) {
                _loginResult.postValue(LoginResponse(error = true, message = "Kesalahan jaringan"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

}