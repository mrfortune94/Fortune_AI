package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    @Headers("Content-Type: application/json")
    @POST("responses")
    suspend fun chatCompletion(@Body request: GrokChatRequest): GrokChatResponse

    @Headers("Content-Type: application/json")
    @POST("images/generations")
    suspend fun generateImage(@Body request: GrokImageRequest): GrokImageResponse

    @Headers("Content-Type: application/json")
    @POST("images/edits")
    suspend fun editImage(@Body request: GrokImageEditRequest): GrokImageResponse

    @Headers("Content-Type: application/json")
    @POST("videos/generations")
    suspend fun generateVideo(@Body request: GrokVideoRequest): GrokVideoResponse
}

// Chat/Responses API - supports both messages and input arrays
data class GrokChatRequest(
    val model: String = "grok-4",
    val messages: List<Message>? = null,
    val input: List<InputItem>? = null,
    val temperature: Double = 1.0,
    @SerializedName("max_tokens") val maxTokens: Int = 2048
)

data class Message(val role: String, val content: String)

data class InputItem(
    val type: String,  // "text" or "image"
    val text: String? = null,
    @SerializedName("image_url") val imageUrl: ImageUrl? = null
)

data class ImageUrl(val url: String)

data class GrokChatResponse(val choices: List<Choice>)
data class Choice(val message: MessageResponse)
data class MessageResponse(val content: String)

// Image Generation
data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024",
    @SerializedName("response_format") val responseFormat: String = "url"  // "url" or "b64_json"
)

// Image Edit
data class GrokImageEditRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val image: String,  // Base64 or URL
    val n: Int = 1,
    val size: String = "1024x1024",
    @SerializedName("response_format") val responseFormat: String = "url"
)

data class GrokImageResponse(val data: List<ImageData>)
data class ImageData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null
)

// Video Generation
data class GrokVideoRequest(
    val model: String = "grok-video-beta",
    val prompt: String,
    val duration: Int = 10,  // Duration in seconds
    @SerializedName("response_format") val responseFormat: String = "url"  // "url" or "b64_json"
)

data class GrokVideoResponse(val data: List<VideoData>)
data class VideoData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null
)
