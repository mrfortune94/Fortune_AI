package com.fortunateworld.grokunfiltered

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// Retrofit interface that matches the xAI docs endpoints exactly.
interface GrokApi {
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

/**
 * Responses API models (chat-like)
 */
data class GrokResponseRequest(
    val model: String = "grok-4",
    val messages: List<Message>? = null,
    val input: List<InputItem>? = null,
    val temperature: Double? = 1.0,
    @SerializedName("max_tokens")
    val maxTokens: Int? = 2048
)

data class Message(val role: String, val content: String)

sealed class InputItem {
    data class InputText(val type: String = "input_text", val text: String): InputItem()
    data class InputImage(
        val type: String = "input_image",
        @SerializedName("image_url")
        val imageUrl: String,
        val details: String? = null
    ): InputItem()
}

data class GrokResponse(val output: ResponseOutput? = null, val id: String? = null)
data class ResponseOutput(
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("content")
    val content: List<ContentItem>? = null
)
data class ContentItem(@SerializedName("type") val type: String?, @SerializedName("text") val text: String?)

/**
 * Image generation models
 */
data class GrokImageRequest(
    val model: String,
    val prompt: String,
    val n: Int = 1,
    @SerializedName("aspect_ratio")
    val aspectRatio: String? = null,
    val size: String? = null
)

data class GrokImageEditRequest(
    val model: String,
    val image: String, // base64 or URL per docs
    val prompt: String
)

data class GrokImageResponse(
    val data: List<ImageData>
)

data class ImageData(
    val url: String?,
    @SerializedName("b64_json")
    val b64Json: String?
)

/**
 * Video generation models
 * Fields follow the docs' snake_case names and are mapped using @SerializedName where needed.
 */
data class GrokVideoRequest(
    val model: String,
    val prompt: String,
    val n: Int = 1,
    @SerializedName("duration_seconds")
    val durationSeconds: Int? = null,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("aspect_ratio")
    val aspectRatio: String? = null
)

data class GrokVideoResponse(
    val data: List<VideoData>
)

data class VideoData(
    val url: String?,
    @SerializedName("b64_json")
    val b64Json: String?,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null
)
