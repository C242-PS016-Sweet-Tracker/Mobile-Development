package com.capstone.sweettrack.data

import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.pref.UserPreference
import com.capstone.sweettrack.data.remote.api.ApiService
import com.capstone.sweettrack.data.remote.response.AddDetailUserRequest
import com.capstone.sweettrack.data.remote.response.ApiResponse
import com.capstone.sweettrack.data.remote.response.DetailUserResponse
import com.capstone.sweettrack.data.remote.response.EditDetailUserRequest
import com.capstone.sweettrack.data.remote.response.EditProfileRequest
import com.capstone.sweettrack.data.remote.response.LoginRequest
import com.capstone.sweettrack.data.remote.response.LoginResponse
import com.capstone.sweettrack.data.remote.response.OTPRequest
import com.capstone.sweettrack.data.remote.response.OTPResetPassRequest
import com.capstone.sweettrack.data.remote.response.OTPResponse
import com.capstone.sweettrack.data.remote.response.ResendingOTPRequest
import com.capstone.sweettrack.data.remote.response.UserProfileResponse
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResetPassword
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

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
        val loginResult =
            response.loginResult ?: throw IllegalStateException("Login result is null")
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

    suspend fun otpResetPassRequest(requestOtpResetPass: OTPResetPassRequest): VerifyOtpResponse {
        val response = apiService.otpResetPassword(requestOtpResetPass)
        return response
    }

    suspend fun verifyOtpResetPassRequest(request: VerifyOtpResetPassword): VerifyOtpResponse {
        val response = apiService.verifyOtpResetPassword(request)
        return response
    }

    suspend fun getProfileUser(): UserProfileResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        return apiService.getProfileUsers(userId)
    }


    suspend fun editProfileUser(request: EditProfileRequest): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        return apiService.editProfileUsers(userId, request)
    }

    suspend fun getDetailUserInformation(): DetailUserResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val response = apiService.getDetailUser(userId)

        return response
    }

    suspend fun editDetailUserInformation(
        name: String,
        gender: String,
        age: Int?,
        height: Double?,
        weight: Double?,
        activityLevel: String,
        diabetesType: String,
        lastBloodSugar: Double?
    ): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val detailUser = EditDetailUserRequest(name, gender, age, height, weight, activityLevel, diabetesType, lastBloodSugar, 0.0)
        val response = apiService.editDetailUser(userId, detailUser)
        return response
    }

    suspend fun addDetailUserInformation(
        name: String,
        gender: String,
        age: Int?,
        height: Double?,
        weight: Double?,
        activityLevel: String,
        diabetesType: String,
        lastBloodSugar: Double?,
    ): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val detailUser = AddDetailUserRequest(name, gender, age, height, weight, activityLevel, diabetesType, lastBloodSugar, userId)
        println("Kirim data ke API: name=$name, gender=$gender, age=$age, height=$height, weight=$weight, activityLevel=$activityLevel, diabetesType=$diabetesType, lastBloodSugar=$lastBloodSugar")
        val response = apiService.addDetailUser(detailUser)

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