package com.capstone.sweettrack.data.remote.response

data class LoginRequest(
    val usernameOrEmail: String,
    val password: String
)
