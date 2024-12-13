package com.capstone.sweettrack.view.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coding.sweettrack.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

class ChatViewModel : ViewModel() {

    private val _chatHistory = MutableLiveData<ArrayList<Message>>()
    val chatHistory: LiveData<ArrayList<Message>> = _chatHistory

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro-latest",
        apiKey = BuildConfig.API_KEY
    )

    fun sendMessage(input: String) {
        val userMessage = Message(input, isLocalUser = true, timestamp = System.currentTimeMillis())
        addMessage(userMessage)

        viewModelScope.launch {
            try {
                val inputContent = content {
                    text("$input Jawab dalam bahasa Indonesia dan beri jawaban singkat.")
                }

                val response = generativeModel.generateContent(inputContent)

                val botMessage = Message(
                    response.text,
                    isLocalUser = false,
                    timestamp = System.currentTimeMillis()
                )
                addMessage(botMessage)

            } catch (e: Exception) {
                _errorMessage.value = "Failed to connect to Gemini AI: ${e.message}"
            }
        }
    }


    private fun addMessage(message: Message) {
        val list = _chatHistory.value ?: ArrayList()
        list.add(message)
        _chatHistory.value = list
    }
}
