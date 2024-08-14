package com.dyland.exercise3.chat

sealed class ChatUIEvents {
    class SendMessage(val message: String) : ChatUIEvents()
}