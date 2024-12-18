package com.capstone.sweettrack.data.remote.api

import com.capstone.sweettrack.data.remote.response.AddDetailUserRequest
import com.capstone.sweettrack.data.remote.response.ApiResponse
import com.capstone.sweettrack.data.remote.response.CaloriesResponse
import com.capstone.sweettrack.data.remote.response.DetailUserResponse
import com.capstone.sweettrack.data.remote.response.EditCalorieRequest
import com.capstone.sweettrack.data.remote.response.EditCalorieResponse
import com.capstone.sweettrack.data.remote.response.EditDetailUserRequest
import com.capstone.sweettrack.data.remote.response.FavoriteAdd
import com.capstone.sweettrack.data.remote.response.FavoriteResponse
import com.capstone.sweettrack.data.remote.response.FavoriteResponses
import com.capstone.sweettrack.data.remote.response.HistoryResponse
import com.capstone.sweettrack.data.remote.response.LoginRequest
import com.capstone.sweettrack.data.remote.response.LoginResponse
import com.capstone.sweettrack.data.remote.response.OTPRequest
import com.capstone.sweettrack.data.remote.response.OTPResetPassRequest
import com.capstone.sweettrack.data.remote.response.OTPResponse
import com.capstone.sweettrack.data.remote.response.OcrResponse
import com.capstone.sweettrack.data.remote.response.RecommendationRequest
import com.capstone.sweettrack.data.remote.response.RecommendationResponse
import com.capstone.sweettrack.data.remote.response.ResendingOTPRequest
import com.capstone.sweettrack.data.remote.response.ResponseModel
import com.capstone.sweettrack.data.remote.response.UpdateCalorieDayRequest
import com.capstone.sweettrack.data.remote.response.UserProfileResponse
import com.capstone.sweettrack.data.remote.response.VerifyOtpRequest
import com.capstone.sweettrack.data.remote.response.VerifyOtpResetPassword
import com.capstone.sweettrack.data.remote.response.VerifyOtpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @Multipart
    @PUT("profil/editProfilUsers/{user_id}")
    suspend fun editProfileUsers(
        @Path("user_id") userId: Int,
        @Part image: MultipartBody.Part?,
        @Part("namaLengkap") namaLengkap: RequestBody,
        @Part("username") username: RequestBody,
        @Part("jenisKelamin") jenisKelamin: RequestBody,
        @Part("umur") umur: RequestBody
    ): ApiResponse

    @Multipart
    @POST("ocr/predict")
    suspend fun ocrScan(
        @Part image: MultipartBody.Part?
    ): OcrResponse

    @Multipart
    @POST("scan/predict")
    suspend fun scanFoodNutrition(
        @Part image: MultipartBody.Part?
    ): ResponseModel

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

    @GET("kalori/getKalori/{user_id}")
    suspend fun getCalorie(
        @Path("user_id") userId: Int
    ): CaloriesResponse

    @PUT("kalori/updateKalori/{user_id}")
    suspend fun setCalorie(
        @Path("user_id") userId: Int,
        @Body request: EditCalorieRequest
    ): EditCalorieResponse

    @PUT("kalori/updateKaloriHarian/{user_id}")
    suspend fun updateCalorieDay(
        @Path("user_id") userId: Int,
        @Body request: UpdateCalorieDayRequest
    ): EditCalorieResponse

    @POST("rekomendasi")
    suspend fun getRecommendationFood(
        @Body request: RecommendationRequest
    ): RecommendationResponse


    @Multipart
    @POST("scan/add")
    suspend fun addFoodScan(
        @Part image: MultipartBody.Part,
        @Part("user_id") userId: RequestBody,
        @Part("namaMakanan") foodName: RequestBody,
        @Part("kalori") calorie: RequestBody,
        @Part("gula") sugar: RequestBody,
        @Part("lemak") fat: RequestBody,
        @Part("protein") protein: RequestBody
    ): ApiResponse

    @Multipart
    @POST("ocr/add")
    suspend fun addOcrData(
        @Part image: MultipartBody.Part,
        @Part("user_id") userId: RequestBody,
        @Part("gula") gula: RequestBody
    ): ApiResponse


    @GET("scan/hasilAnalisa/{user_id}")
    suspend fun getHistoryScan(
        @Path("user_id") userId: Int,
    ): HistoryResponse

    @GET("rekomendasi/getFavorit/{user_id}")
    suspend fun getFavoriteUser(
        @Path("user_id") userId: Int
    ): FavoriteResponse

    @POST("rekomendasi/addfavorit")
    suspend fun addFavoriteUser(
        @Body request: FavoriteAdd
    ): FavoriteResponses
}