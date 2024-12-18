package com.capstone.sweettrack.data

import android.content.Context
import android.net.Uri
import com.capstone.sweettrack.data.local.entity.HistoryScan
import com.capstone.sweettrack.data.local.room.SweetTrackDao
import com.capstone.sweettrack.data.local.room.SweetTrackDatabase
import com.capstone.sweettrack.data.pref.UserModel
import com.capstone.sweettrack.data.pref.UserPreference
import com.capstone.sweettrack.data.remote.api.ApiService
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
import com.capstone.sweettrack.util.reduceFileImage
import com.capstone.sweettrack.util.uriToFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class Repository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val sweetTrackDatabase: SweetTrackDatabase,
    private val sweetTrackDao: SweetTrackDao

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

    suspend fun editProfileUsers(
        namaLengkap: String,
        username: String,
        jenisKelamin: String,
        umur: Int,
        fotoUri: Uri?,
        context: Context
    ): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val namaLengkapPart = createPartFromString(namaLengkap)
        val usernamePart = createPartFromString(username)
        val jenisKelaminPart = createPartFromString(jenisKelamin)
        val umurPart = createPartFromString(umur.toString())
        val fotoPart = fotoUri?.let { prepareFilePart(it, context) }

        return apiService.editProfileUsers(
            userId,
            image = fotoPart,
            namaLengkap = namaLengkapPart,
            username = usernamePart,
            jenisKelamin = jenisKelaminPart,
            umur = umurPart
        )
    }

    private fun createPartFromString(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun prepareFilePart(uri: Uri, context: Context): MultipartBody.Part {
        val file = uriToFile(uri, context).reduceFileImage()

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData("image", file.name, requestFile)
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
        val detailUser = EditDetailUserRequest(
            name,
            gender,
            age,
            height,
            weight,
            activityLevel,
            diabetesType,
            lastBloodSugar,
            0.0
        )
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
        val detailUser = AddDetailUserRequest(
            name,
            gender,
            age,
            height,
            weight,
            activityLevel,
            diabetesType,
            lastBloodSugar,
            userId
        )
        val response = apiService.addDetailUser(detailUser)

        return response
    }

    suspend fun getUserCalorie(): CaloriesResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val response = apiService.getCalorie(userId)

        return response
    }

    suspend fun setUserCalorie(calorie: Double?): EditCalorieResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val request = EditCalorieRequest(calorie)
        val response = apiService.setCalorie(userId, request)

        return response
    }

    suspend fun updateUserCalorieDay(calorie: Double?): EditCalorieResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        val request = UpdateCalorieDayRequest(calorie)
        val response = apiService.updateCalorieDay(userId, request)

        return response
    }

    suspend fun scanNutritionOcr(
        fotoUri: Uri?,
        context: Context
    ): OcrResponse {

        val fotoPart = fotoUri?.let { prepareFilePart(it, context) }
        val response = apiService.ocrScan(fotoPart)

        return response
    }

    suspend fun scanFoodNutrition(
        fotoUri: Uri?,
        context: Context
    ): ResponseModel {
        val fotoPart = fotoUri?.let { prepareFilePart(it, context) }
        val response = apiService.scanFoodNutrition(fotoPart)

        return response
    }


    suspend fun addFoodToHistory(
        fotoUri: Uri,
        foodName: String,
        calorie: Double,
        sugar: Double,
        fat: Double,
        protein: Double,
        context: Context
    ): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()

        val fotoPart = prepareFilePart(fotoUri, context)

        val userIdBody = createPartFromString(userId.toString())
        val foodNameBody = createPartFromString(foodName)
        val calorieBody = createPartFromString(calorie.toString())
        val sugarBody = createPartFromString(sugar.toString())
        val fatBody = createPartFromString(fat.toString())
        val proteinBody = createPartFromString(protein.toString())

        val response = apiService.addFoodScan(
            fotoPart,
            userIdBody,
            foodNameBody,
            calorieBody,
            sugarBody,
            fatBody,
            proteinBody
        )

        return response
    }


    suspend fun addResultOcrToHistoryScan(
        fotoUri: Uri,
        sugar: Double,
        context: Context
    ): ApiResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()

        val fotoPart = fotoUri.let { prepareFilePart(it, context) }

        val userIdBody = createPartFromString(userId.toString())
        val sugarBody = createPartFromString(sugar.toString())

        val response = apiService.addOcrData(fotoPart, userIdBody, sugarBody)

        return response
    }


    suspend fun getRecommendationFood(type: String): RecommendationResponse {
        val request = RecommendationRequest(type)
        val response = apiService.getRecommendationFood(request)

        return response
    }

    suspend fun getFavoriteUser(): FavoriteResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()

        val response = apiService.getFavoriteUser(userId)

        return response
    }

    suspend fun addFavoriteUser(favoriteAdd: FavoriteAdd): FavoriteResponses {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()

        val request = FavoriteAdd(
            user_id = userId,
            namaMakanan = favoriteAdd.namaMakanan,
            kalori = favoriteAdd.kalori,
            karbohidrat = favoriteAdd.karbohidrat,
            lemak = favoriteAdd.lemak,
            protein = favoriteAdd.protein,
            serat = favoriteAdd.serat,
            img = favoriteAdd.img
        )
        val response = apiService.addFavoriteUser(request)

        return response
    }

    suspend fun getHistoryUser(): HistoryResponse {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()

        val response = apiService.getHistoryScan(userId)

        return response
    }

    suspend fun removeFavorite(foodName: String) {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        sweetTrackDao.deleteFavoriteByName(foodName, userId)
    }

    suspend fun isFoodFavorite(foodName: String): Int {
        val session = userPreference.getSession().first()
        val userId = session.userId.toInt()
        return sweetTrackDao.getFavoriteCountByName(foodName, userId)
    }


    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            sweetTrackDatabase: SweetTrackDatabase
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(
                    apiService,
                    userPreference,
                    sweetTrackDatabase,
                    sweetTrackDatabase.eventDao()
                )
            }.also { instance = it }
    }

}