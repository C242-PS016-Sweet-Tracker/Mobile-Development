package com.capstone.sweettrack.data.remote.response

data class CaloriesResponse (
    val statusCode: Int? = null,
    val error: Boolean? = null,
    val message: String? = null,
    val data: DataCalories? = null
)

data class DataCalories(
    val username: String? = null,
    val kalori: Double? = null
)

data class EditCalorieRequest(
    val kalori: Double? = null
)

data class EditCalorieResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String
)