package com.capstone.sweettrack.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendationResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val rekomendasi: List<Recommendation>
) : Parcelable

@Parcelize
data class Recommendation(
    val nama_makanan: String,
    val kalori: Double,
    val karbohidrat: Double,
    val lemak: Double,
    val protein: Double,
    val serat: Double,
    val img: String,
) : Parcelable

data class RecommendationRequest(
    val tipe: String
)