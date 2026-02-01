package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    // Responses API for chat (replaces chat/completions)
    @Headers("Content-Type: application/json")
    @POST("v1/responses")
    suspend fun chatResponse(@Body request: GrokResponseRequest): GrokResponseResponse

    // Image generation endpoint
    @Headers("Content-Type: application/json")
    @POST("v1/images/generations")
    suspend fun generateImage(@Body request: GrokImageRequest): GrokImageResponse

    // Image edit endpoint
    @Headers("Content-Type: application/json")
    @POST("v1/images/edits")
    suspend fun editImage(@Body request: GrokImageEditRequest): GrokImageResponse

    // Video generation endpoint
    @Headers("Content-Type: application/json")
    @POST("v1/videos/generations")
    suspend fun generateVideo(@Body request: GrokVideoRequest): GrokVideoResponse
}

// Responses API models (for chat)
data class GrokResponseRequest(
    val model: String = "grok-beta",
    val messages: List<Message>? = null,  // For role/content style
    val input: List<InputItem>? = null,   // For combined text+image inputs
    val temperature: Double? = 1.0,
    @SerializedName("max_tokens")
    val maxTokens: Int? = 2048
)

data class Message(
    val role: String, 
    val content: String
)

data class InputItem(
    val type: String,  // "text" or "image_url"
    val text: String? = null,
    @SerializedName("image_url")
    val imageUrl: ImageUrl? = null
)

data class ImageUrl(
    val url: String
)

data class GrokResponseResponse(
    val id: String? = null,
    val choices: List<ResponseChoice>? = null,
    val output: String? = null  // Direct output for simple responses
)

data class ResponseChoice(
    val message: MessageResponse? = null,
    val text: String? = null
)

data class MessageResponse(
    val content: String
)

// Image generation models
data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024",
    @SerializedName("response_format")
    val responseFormat: String? = "url"  // "url" or "b64_json"
)

data class GrokImageEditRequest(
    val model: String = "grok-2-image-1212",
    val image: String,  // Base64 encoded or URL
    val prompt: String,
    val n: Int = 1,
    val size: String? = "1024x1024",
    @SerializedName("response_format")
    val responseFormat: String? = "url"
)

data class GrokImageResponse(
    val data: List<ImageData>
)

data class ImageData(
    val url: String? = null,
    @SerializedName("b64_json")
    val b64Json: String? = null
)

// Video generation models
data class GrokVideoRequest(
    val model: String = "grok-video-1",
    val prompt: String,
    val duration: Int = 5,  // Duration in seconds
    @SerializedName("response_format")
    val responseFormat: String? = "url"  // "url" or "b64_json"
)

data class GrokVideoResponse(
    val data: List<VideoData>
)

data class VideoData(
    val url: String? = null,
    @SerializedName("b64_json")
    val b64Json: String? = null,
    val thumbnail: String? = null  // Thumbnail URL or base64
)
