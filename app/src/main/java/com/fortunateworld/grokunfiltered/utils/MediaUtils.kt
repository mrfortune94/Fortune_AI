package com.fortunateworld.grokunfiltered.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object MediaUtils {
    suspend fun decodeBase64ToFile(context: android.content.Context, b64: String, namePrefix: String = "grok_video_"): File =
        withContext(Dispatchers.IO) {
            val bytes = Base64.decode(b64, Base64.DEFAULT)
            val file = File.createTempFile(namePrefix, ".mp4", context.cacheDir)
            FileOutputStream(file).use { it.write(bytes) }
            file
        }

    suspend fun extractVideoThumbnail(urlOrPath: String): Bitmap? = withContext(Dispatchers.IO) {
        try {
            val retriever = MediaMetadataRetriever()
            // urlOrPath can be http(s) URL or local path
            retriever.setDataSource(urlOrPath, HashMap())
            // frameAtTime retrieves the first frame by default when no time is specified
            val bitmap = retriever.frameAtTime
            retriever.release()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
