package com.dyland.exercise3.chat

import com.dyland.exercise3.Message

data class ChatUIState (
    val messages: List<Message> = emptyList(),
    val isLoading: Boolean =  false
)