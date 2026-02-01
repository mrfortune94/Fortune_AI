package com.fortunateworld.grokunfiltered

sealed class ChatMessage {
    data class Text(val sender: String, val content: String) : ChatMessage()
    data class Video(val videoUrl: String, val thumbnailUrl: String?) : ChatMessage()
}
