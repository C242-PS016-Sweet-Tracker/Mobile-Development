package com.capstone.sweettrack.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OcrResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val data: Sugar? = null
) : Parcelable

@Parcelize
data class Sugar(
    val gula: Double? = null
) : Parcelable