package com.dyland.exercise3.chat

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dyland.exercise3.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val _chatUIState = MutableStateFlow(ChatUIState())
    val chatUIState = _chatUIState.asStateFlow()
    private val generativeModel : GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = "Replace you api key"
    )

    fun onEvent(event: ChatUIEvents) {
        when (event) {
            is ChatUIEvents.SendMessage -> {

                val message = Message(role = "user", message = event.message)
                _chatUIState.value = _chatUIState.value.copy(
                    messages = _chatUIState.value.messages + message
                )

                sendMessage(event.message)
            }
        }
    }

     private fun sendMessage(message: String) {
        viewModelScope.launch {
            _chatUIState.update {
                it.copy(isLoading = true)
            }
            try{
                val chat = generativeModel.startChat(
                    history = _chatUIState.value.messages.map {
                        content (it.role) { text(it.message) }
                    }.toList()
                )
                val response = chat.sendMessage(message + "Phân tích cho tôi đoạn văn trên")
                _chatUIState.update {
                    it.copy(
                        isLoading = false,
                        messages = it.messages + Message(role = "model", message = response.text.toString())
                    )
                }
            } catch (e : Exception){
                e.printStackTrace()
            } finally {
                _chatUIState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

//    private fun streamData(message: String): Flow<String> {
//        return flow {
//            val chat = generativeModel.startChat(
//                history = _chatUIState.value.messages.map {
//                    content (it.role) { text(it.message) }
//                }.toList()
//            )
//
//            val response = chat.sendMessageStream(message)
//
//            response.body()?.let { responseBody ->
//                val reader = responseBody.charStream().bufferedReader()
//                var line: String?
//
//                while (reader.readLine().also { line = it } != null) {
//                    emit(line!!) // Phát ra từng dòng
//                }
//            }
//
//            response.let {
//
//            }
//
//        }.flowOn(Dispatchers.IO)
//    }
}