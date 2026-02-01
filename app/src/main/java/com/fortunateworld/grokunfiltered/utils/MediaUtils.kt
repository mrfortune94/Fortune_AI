package com.fortunateworld.grokunfiltered.utils

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object MediaUtils {
    /**
     * Decodes a base64 string to a file
     * @param context Android context
     * @param b64 Base64 encoded string
     * @param extension File extension (e.g., "mp4", "jpg")
     * @return File path to the decoded file
     */
    suspend fun decodeBase64ToFile(context: Context, b64: String, extension: String = "mp4"): String = withContext(Dispatchers.IO) {
        val decoded = Base64.decode(b64, Base64.DEFAULT)
        val file = File(context.cacheDir, "media_${System.currentTimeMillis()}.$extension")
        FileOutputStream(file).use { it.write(decoded) }
        file.absolutePath
    }

    /**
     * Extracts a thumbnail from a video file
     * @param videoPath Path to the video file (can be URL or local path)
     * @return Bitmap of the first frame or null if extraction fails
     */
    suspend fun extractVideoThumbnail(videoPath: String): Bitmap? = withContext(Dispatchers.IO) {
        val retriever = MediaMetadataRetriever()
        try {
            if (videoPath.startsWith("http://") || videoPath.startsWith("https://")) {
                retriever.setDataSource(videoPath, HashMap())
            } else {
                retriever.setDataSource(videoPath)
            }
            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
