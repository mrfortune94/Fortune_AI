package com.fortunateworld.grokunfiltered

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class GrokApiModelsTest {
    private val gson = Gson()

    @Test
    fun testVideoRequestSerialization() {
        val request = GrokVideoRequest(
            prompt = "test video",
            durationSeconds = 15
        )
        val json = gson.toJson(request)
        
        // Verify snake_case field name
        assertTrue("JSON should contain duration_seconds", json.contains("duration_seconds"))
        assertTrue("JSON should contain the value 15", json.contains("15"))
        assertFalse("JSON should NOT contain durationSeconds", json.contains("durationSeconds"))
    }

    @Test
    fun testVideoResponseDeserialization() {
        val json = """
            {
                "data": [
                    {
                        "url": "https://example.com/video.mp4",
                        "b64_json": "base64data",
                        "thumbnail_url": "https://example.com/thumb.jpg"
                    }
                ]
            }
        """.trimIndent()
        
        val response = gson.fromJson(json, GrokVideoResponse::class.java)
        
        assertNotNull("Response should not be null", response)
        assertEquals("Should have one video data item", 1, response.data.size)
        
        val videoData = response.data.first()
        assertEquals("URL should match", "https://example.com/video.mp4", videoData.url)
        assertEquals("b64_json should match", "base64data", videoData.b64Json)
        assertEquals("thumbnail_url should match", "https://example.com/thumb.jpg", videoData.thumbnailUrl)
    }

    @Test
    fun testImageResponseWithBase64() {
        val json = """
            {
                "data": [
                    {
                        "b64_json": "base64imagedata"
                    }
                ]
            }
        """.trimIndent()
        
        val response = gson.fromJson(json, GrokImageResponse::class.java)
        
        assertNotNull("Response should not be null", response)
        assertEquals("Should have one image data item", 1, response.data.size)
        
        val imageData = response.data.first()
        assertNull("URL should be null", imageData.url)
        assertEquals("b64_json should match", "base64imagedata", imageData.b64Json)
    }

    @Test
    fun testImageResponseWithUrl() {
        val json = """
            {
                "data": [
                    {
                        "url": "https://example.com/image.jpg"
                    }
                ]
            }
        """.trimIndent()
        
        val response = gson.fromJson(json, GrokImageResponse::class.java)
        
        assertNotNull("Response should not be null", response)
        assertEquals("Should have one image data item", 1, response.data.size)
        
        val imageData = response.data.first()
        assertEquals("URL should match", "https://example.com/image.jpg", imageData.url)
        assertNull("b64_json should be null", imageData.b64Json)
    }

    @Test
    fun testResponsesApiSerialization() {
        val request = GrokResponseRequest(
            messages = listOf(
                ResponseMessage("user", "Hello"),
                ResponseMessage("assistant", "Hi there")
            ),
            input = listOf("input1", "input2")
        )
        val json = gson.toJson(request)
        
        assertTrue("JSON should contain messages array", json.contains("messages"))
        assertTrue("JSON should contain input array", json.contains("input"))
        assertTrue("JSON should contain user role", json.contains("user"))
        assertTrue("JSON should contain assistant role", json.contains("assistant"))
    }

    @Test
    fun testResponsesApiDeserialization() {
        val json = """
            {
                "responses": [
                    {
                        "message": {
                            "role": "assistant",
                            "content": "Test response"
                        }
                    }
                ]
            }
        """.trimIndent()
        
        val response = gson.fromJson(json, GrokResponse::class.java)
        
        assertNotNull("Response should not be null", response)
        assertEquals("Should have one response item", 1, response.responses.size)
        assertEquals("Content should match", "Test response", response.responses.first().message.content)
        assertEquals("Role should match", "assistant", response.responses.first().message.role)
    }
}
