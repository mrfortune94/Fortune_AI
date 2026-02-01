package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GrokApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    suspend fun chatCompletion(@Body request: GrokChatRequest): GrokChatResponse

    @Headers("Content-Type: application/json")
    @POST("responses")
    suspend fun createResponse(@Body request: GrokResponseRequest): GrokResponse

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

// Legacy chat completions models
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

// Responses API models
data class GrokResponseRequest(
    val messages: List<ResponseMessage>,
    val input: List<String> = emptyList(),
    val model: String = "grok-4"
)

data class ResponseMessage(
    val role: String,
    val content: String
)

data class GrokResponse(
    val responses: List<ResponseItem>
)

data class ResponseItem(
    val message: ResponseMessage
)

// Image generation models
data class GrokImageRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class GrokImageEditRequest(
    val model: String = "grok-2-image-1212",
    val prompt: String,
    val image: String,
    val n: Int = 1,
    val size: String = "1024x1024"
)

data class GrokImageResponse(val data: List<ImageData>)
data class ImageData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null
)

// Video generation models
data class GrokVideoRequest(
    val model: String = "grok-video-1",
    val prompt: String,
    @SerializedName("duration_seconds") val durationSeconds: Int = 10,
    val n: Int = 1
)

data class GrokVideoResponse(val data: List<VideoData>)
data class VideoData(
    val url: String? = null,
    @SerializedName("b64_json") val b64Json: String? = null,
    @SerializedName("thumbnail_url") val thumbnailUrl: String? = null
)
