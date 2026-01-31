package com.example.mp3player

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.media3.ui.PlayerView
import com.google.common.util.concurrent.MoreExecutors

class MainActivity : AppCompatActivity() {
    private var controller: MediaController? = null
    private lateinit var playerView: PlayerView
    private lateinit var videoSection: LinearLayout
    private lateinit var musicSection: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        videoSection = findViewById(R.id.video_section)
        musicSection = findViewById(R.id.music_section)

        val videoButton = findViewById<Button>(R.id.video_button)
        val musicButton = findViewById<Button>(R.id.music_button)
        val playVideoButton = findViewById<Button>(R.id.play_video)
        val playMusicButton = findViewById<Button>(R.id.play_music)

        videoButton.setOnClickListener { showVideoSection() }
        musicButton.setOnClickListener { showMusicSection() }

        playVideoButton.setOnClickListener {
            controller?.let { player ->
                val mediaItem = MediaItem.Builder()
                    .setUri("https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Big Buck Bunny")
                            .setArtist("Demo")
                            .build()
                    )
                    .build()
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }
        }

        playMusicButton.setOnClickListener {
            controller?.let { player ->
                val mediaItem = MediaItem.Builder()
                    .setUri("https://storage.googleapis.com/exoplayer-test-media-0/play.mp3")
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("Sample Track")
                            .setArtist("Demo")
                            .build()
                    )
                    .build()
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sessionToken = SessionToken(this, ComponentName(this, PlayerService::class.java))
        MediaController.Builder(this, sessionToken).buildAsync()
            .addListener({
                controller = it
                playerView.player = it
            }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        super.onStop()
        controller?.release()
        controller = null
    }

    private fun showVideoSection() {
        videoSection.visibility = View.VISIBLE
        musicSection.visibility = View.GONE
    }

    private fun showMusicSection() {
        videoSection.visibility = View.GONE
        musicSection.visibility = View.VISIBLE
    }
}
