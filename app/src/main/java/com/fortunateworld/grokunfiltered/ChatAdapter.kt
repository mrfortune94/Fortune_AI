package com.fortunateworld.grokunfiltered

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatAdapter(
    private val messages: List<ChatMessage>,
    private val scope: CoroutineScope
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_TEXT = 0
        private const val VIEW_TYPE_VIDEO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position]) {
            is ChatMessage.Text -> VIEW_TYPE_TEXT
            is ChatMessage.Video -> VIEW_TYPE_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_TEXT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_text, parent, false)
                TextViewHolder(view)
            }
            VIEW_TYPE_VIDEO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_video, parent, false)
                VideoViewHolder(view, scope)
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val message = messages[position]) {
            is ChatMessage.Text -> (holder as TextViewHolder).bind(message)
            is ChatMessage.Video -> (holder as VideoViewHolder).bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class TextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.chatMessageText)

        fun bind(message: ChatMessage.Text) {
            textView.text = "${message.sender}: ${message.content}"
        }
    }

    class VideoViewHolder(itemView: View, private val scope: CoroutineScope) : RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        private val playOverlay: ImageView = itemView.findViewById(R.id.playOverlay)
        private val videoView: VideoView = itemView.findViewById(R.id.videoView)
        private val container: View = itemView.findViewById(R.id.videoContainer)

        fun bind(message: ChatMessage.Video) {
            // Reset state
            videoView.visibility = View.GONE
            thumbnail.visibility = View.VISIBLE
            playOverlay.visibility = View.VISIBLE

            // Load thumbnail
            if (message.thumbnailUrl != null) {
                thumbnail.load(message.thumbnailUrl)
            } else {
                // Generate thumbnail from video
                generateThumbnail(message.videoUrl)
            }

            // Click to play
            container.setOnClickListener {
                playVideo(message.videoUrl)
            }
        }

        private fun generateThumbnail(videoUrl: String) {
            scope.launch {
                try {
                    val bitmap = withContext(Dispatchers.IO) {
                        val retriever = MediaMetadataRetriever()
                        try {
                            retriever.setDataSource(videoUrl, HashMap())
                            retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                        } catch (e: Exception) {
                            Log.e("VideoViewHolder", "Failed to generate thumbnail", e)
                            null
                        } finally {
                            retriever.release()
                        }
                    }
                    bitmap?.let {
                        thumbnail.setImageBitmap(it)
                    }
                } catch (e: Exception) {
                    Log.e("VideoViewHolder", "Error generating thumbnail", e)
                }
            }
        }

        private fun playVideo(videoUrl: String) {
            thumbnail.visibility = View.GONE
            playOverlay.visibility = View.GONE
            videoView.visibility = View.VISIBLE

            videoView.setVideoPath(videoUrl)
            val mediaController = MediaController(itemView.context)
            mediaController.setAnchorView(videoView)
            videoView.setMediaController(mediaController)

            videoView.setOnPreparedListener { mp ->
                mp.start()
            }

            videoView.setOnCompletionListener {
                // Return to thumbnail view
                videoView.visibility = View.GONE
                thumbnail.visibility = View.VISIBLE
                playOverlay.visibility = View.VISIBLE
            }

            videoView.setOnErrorListener { _, what, extra ->
                Log.e("VideoViewHolder", "Error playing video: what=$what, extra=$extra")
                videoView.visibility = View.GONE
                thumbnail.visibility = View.VISIBLE
                playOverlay.visibility = View.VISIBLE
                true
            }

            videoView.start()
        }
    }
}
