package com.capstone.sweettrack.data.remote.response

data class CaloriesResponse(
    val statusCode: Int? = null,
    val error: Boolean? = null,
    val message: String? = null,
    val data: DataCalories? = null
)

data class DataCalories(
    val username: String,
    val kalori: Double,
    val kalori_harian: Double,
    val tipe_diabetes: String
)

data class EditCalorieRequest(
    val kalori: Double? = null
)

data class UpdateCalorieDayRequest(
    val kaloriAdd: Double? = null
)

data class EditCalorieResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String
)