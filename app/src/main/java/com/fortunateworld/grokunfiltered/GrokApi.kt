package com.fortunateworld.grokunfiltered

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chatCompletion(@Body request: GrokChatRequest): GrokChatResponse

    @Headers("Content-Type: application/json")
    @POST("images/generations")
    suspend fun generateImage(@Body request: GrokImageRequest): GrokImageResponse
}

data class GrokChatRequest(
    val model: String = "grok-4",
    val messages: List<Message>,
    val temperature: Double = 1.0,
    val max_tokens: Int = 2048
)

data class Message(val role: String, val content: String)

data class GrokChatResponse(val choices: List<Choice>)
data class Choice(val message: MessageResponse)
data class MessageResponse(val content: String)

data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class GrokImageResponse(val data: List<ImageData>)
data class ImageData(val url: String)
