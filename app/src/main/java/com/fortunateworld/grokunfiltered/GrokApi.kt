package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    // Responses API for chat
    @Headers("Content-Type: application/json")
    @POST("responses")
    suspend fun chatResponses(@Body request: GrokResponsesRequest): GrokResponsesResponse

    // Image generation
    @Headers("Content-Type: application/json")
    @POST("images/generations")
    suspend fun generateImage(@Body request: GrokImageRequest): GrokImageResponse

    // Image edit
    @Headers("Content-Type: application/json")
    @POST("images/edits")
    suspend fun editImage(@Body request: GrokImageEditRequest): GrokImageResponse

    // Video generation
    @Headers("Content-Type: application/json")
    @POST("videos/generations")
    suspend fun generateVideo(@Body request: GrokVideoRequest): GrokVideoResponse
}

// Responses API data classes
data class GrokResponsesRequest(
    val model: String = "grok-4",
    val messages: List<Message>? = null,
    val input: List<ResponseInput>? = null,
    val temperature: Double = 1.0,
    @SerializedName("max_tokens") val maxTokens: Int = 2048
)

data class Message(val role: String, val content: Any)
data class ResponseInput(val type: String, val text: String? = null, val image: String? = null)

data class GrokResponsesResponse(val choices: List<ResponseChoice>)
data class ResponseChoice(val message: MessageResponse)
data class MessageResponse(val content: String)

// Image generation data classes
data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024",
    @SerializedName("response_format") val responseFormat: String = "url"
)

data class GrokImageEditRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val image: String,
    val n: Int = 1,
    val size: String = "1024x1024",
    @SerializedName("response_format") val responseFormat: String = "url"
)

data class GrokImageResponse(val data: List<ImageData>)
data class ImageData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null
)

// Video generation data classes
data class GrokVideoRequest(
    val model: String = "grok-2-video-1212",
    val prompt: String,
    val duration: Int = 10,
    @SerializedName("response_format") val responseFormat: String = "url"
)

data class GrokVideoResponse(val data: List<VideoData>)
data class VideoData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null
)
