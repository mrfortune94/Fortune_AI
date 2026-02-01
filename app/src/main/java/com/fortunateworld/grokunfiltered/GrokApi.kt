package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    // Responses API endpoint (new standardized endpoint)
    @Headers("Content-Type: application/json")
    @POST("responses")
    suspend fun createResponse(@Body request: GrokResponseRequest): GrokResponse

    // Legacy chat completions endpoint (kept for compatibility)
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chatCompletion(@Body request: GrokChatRequest): GrokChatResponse

    // Image generation endpoint
    @Headers("Content-Type: application/json")
    @POST("images/generations")
    suspend fun generateImage(@Body request: GrokImageRequest): GrokImageResponse

    // Image edit endpoint
    @Headers("Content-Type: application/json")
    @POST("images/edits")
    suspend fun editImage(@Body request: GrokImageEditRequest): GrokImageResponse

    // Video generation endpoint
    @Headers("Content-Type: application/json")
    @POST("videos/generations")
    suspend fun generateVideo(@Body request: GrokVideoRequest): GrokVideoResponse
}

// Responses API data classes
data class GrokResponseRequest(
    val model: String = "grok-4",
    val messages: List<Message>,
    val temperature: Double = 1.0,
    @SerializedName("max_tokens")
    val maxTokens: Int = 2048
)

data class GrokResponse(
    val id: String? = null,
    val choices: List<ResponseChoice>
)

data class ResponseChoice(
    val message: MessageResponse,
    @SerializedName("finish_reason")
    val finishReason: String? = null
)

// Legacy chat completions data classes
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

// Image generation data classes
data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class GrokImageEditRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val image: String,  // URL or base64
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class GrokImageResponse(val data: List<ImageData>)
data class ImageData(
    val url: String? = null,
    @SerializedName("b64_json")
    val b64Json: String? = null
)

// Video generation data classes
data class GrokVideoRequest(
    val model: String = "grok-video-1",
    val prompt: String,
    val n: Int = 1,
    @SerializedName("aspect_ratio")
    val aspectRatio: String = "16:9",
    val duration: Int = 5,  // duration in seconds
    val format: String = "mp4"
)

data class GrokVideoResponse(val data: List<VideoData>)
data class VideoData(
    val url: String? = null,
    @SerializedName("b64_json")
    val b64Json: String? = null
)
