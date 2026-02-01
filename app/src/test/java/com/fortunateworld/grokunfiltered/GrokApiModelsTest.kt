package com.fortunateworld.grokunfiltered

import com.google.gson.Gson
import org.junit.Test

class GrokApiModelsTest {
    private val gson = Gson()

    @Test
    fun grokVideoRequest_serializesSnakeCase() {
        val req = GrokVideoRequest(model = "grok-video-1", prompt = "test", durationSeconds = 10)
        val json = gson.toJson(req)
        // Ensure JSON contains duration_seconds and prompt keys
        assert(json.contains("duration_seconds"))
        assert(json.contains("prompt"))
    }
}
