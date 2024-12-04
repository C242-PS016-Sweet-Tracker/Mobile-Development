package com.capstone.sweettrack.data

import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.pref.UserPreference
import com.capstone.sweettrack.data.remote.api.ApiService
import com.capstone.sweettrack.data.remote.response.LoginRequest
import com.capstone.sweettrack.data.remote.response.LoginResponse
import com.capstone.sweettrack.data.remote.response.OTPRequest
import com.capstone.sweettrack.data.remote.response.OTPResponse
import com.capstone.sweettrack.data.remote.response.ResendingOTPRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }


    suspend fun login(loginRequest: LoginRequest): LoginResponse {
        val response = apiService.login(loginRequest)
        val loginResult = response.loginResult ?: throw IllegalStateException("Login result is null")
        val userModel = UserModel(
            userId = loginResult.userId ?: "",
            name = loginResult.name ?: "",
            token = loginResult.token ?: "",
            isLogin = true
        )
        saveSession(userModel)

        return response
    }


    suspend fun requestOtp(otpRequest: OTPRequest): OTPResponse {
        val response = apiService.requestOTP(otpRequest)
        return response
    }

    suspend fun verifyOtp(verifyOtpRequest: VerifyOtpRequest): VerifyOtpResponse {
        val response = apiService.verifyOtp(verifyOtpRequest)
        return response
    }

    suspend fun resendingOtp(resendingOTPRequest: ResendingOTPRequest): VerifyOtpResponse {
        val response = apiService.resendingOTP(resendingOTPRequest)
        return response
    }


    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, userPreference)
            }.also { instance = it }
    }
}