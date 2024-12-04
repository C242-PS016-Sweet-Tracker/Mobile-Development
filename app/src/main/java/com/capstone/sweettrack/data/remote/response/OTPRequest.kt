package com.capstone.sweettrack.data.remote.response

data class OTPRequest(
    val username: String,
    val email: String,
    val password: String
)

data class OTPResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String,
    val password: String
)

data class VerifyOtpResponse(
    val statusCode: Int,
    val error: Boolean,
    val message: String,
    val describe: String
)

data class ResendingOTPRequest(
    val email: String,
)
