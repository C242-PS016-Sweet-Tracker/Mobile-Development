package com.capstone.sweettrack.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseModel (
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val data: Result? = null
): Parcelable

@Parcelize
data class Result (
    val makanan: String,
    val kalori: Double,
    var gula: Double,
    val lemak: Double,
    val protein: Double
): Parcelable