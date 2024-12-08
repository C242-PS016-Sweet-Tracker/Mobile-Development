package com.capstone.sweettrack.data.remote.api

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
import com.capstone.sweettrack.data.remote.response.RecommendationResponse
import com.capstone.sweettrack.data.remote.response.ResendingOTPRequest
import com.capstone.sweettrack.data.remote.response.UserProfileResponse
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResetPassword
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("auth/requestOTP")
    suspend fun requestOTP(
        @Body request: OTPRequest
    ): OTPResponse

    @POST("auth/verifyOTP")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): VerifyOtpResponse

    @POST("auth/resendingOTP")
    suspend fun resendingOTP(
        @Body request: ResendingOTPRequest
    ): VerifyOtpResponse

    @POST("auth/OTPResetpassword")
    suspend fun otpResetPassword(
        @Body request: OTPResetPassRequest
    ): VerifyOtpResponse

    @POST("auth/verifyResetPass")
    suspend fun verifyOtpResetPassword(
        @Body request: VerifyOtpResetPassword
    ): VerifyOtpResponse

    @GET("profil/profilUsers/{user_id}")
    suspend fun getProfileUsers(
        @Path("user_id") userId: Int
    ): UserProfileResponse

    @PUT("profil/editProfilUsers/{user_id}")
    suspend fun editProfileUsers(
        @Path("user_id") userId: Int,
        @Body request: EditProfileRequest
    ): ApiResponse

    @GET("detail/detailUsers/{user_id}")
    suspend fun getDetailUser(
        @Path("user_id") userId: Int
    ): DetailUserResponse

    @POST("detail/addDetailUsers")
    suspend fun addDetailUser(
        @Body request: AddDetailUserRequest
    ): ApiResponse

    @PUT("detail/editDetailUsers/{user_id}")
    suspend fun editDetailUser(
        @Path("user_id") userId: Int,
        @Body request: EditDetailUserRequest
    ): ApiResponse

    @GET("events?active=0")
    suspend fun getRecommendation(): RecommendationResponse

}