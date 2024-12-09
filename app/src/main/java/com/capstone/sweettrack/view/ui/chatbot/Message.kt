package com.capstone.sweettrack.view.ui.chatbot

data class Message(
    val text: String?,
    val isLocalUser: Boolean,
    val timestamp: Long
)
