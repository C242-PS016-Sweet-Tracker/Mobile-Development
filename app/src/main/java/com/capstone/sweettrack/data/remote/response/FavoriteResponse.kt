package com.capstone.sweettrack.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteResponses (
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String
): Parcelable

@Parcelize
data class FavoriteResponse (
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val data: List<Favorite>
): Parcelable

@Parcelize
data class Favorite(
    val favorite_id: Int,
    val user_id: Int,
    val nama_makanan: String,
    val kalori: Double,
    val karbohidrat: Double,
    val lemak: Double,
    val protein: Double,
    val serat: Double,
    val img: String,
): Parcelable

data class FavoriteAdd(
    val user_id: Int,
    val namaMakanan: String,
    val kalori: Double,
    val karbohidrat: Double,
    val lemak: Double,
    val protein: Double,
    val serat: Double,
    val img: String,
)