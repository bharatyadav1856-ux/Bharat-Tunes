package com.bharat.tunes

import android.app.Activity
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class PlayerActivity : Activity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(12, 12, 18)
        window.navigationBarColor = Color.rgb(12, 12, 18)

        val songUriText = intent.getStringExtra("SONG_URI")
        val songTitle = intent.getStringExtra("SONG_TITLE") ?: "Unknown Song"

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(30, 30, 30, 30)
            setBackgroundColor(Color.rgb(12, 12, 18))
        }

        val nowPlaying = TextView(this).apply {
            text = "NOW PLAYING"
            textSize = 16f
            setTextColor(Color.rgb(170, 140, 255))
            gravity = Gravity.CENTER
        }

        val title = TextView(this).apply {
            text = "🎵\n\n$songTitle"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 30, 0, 50)
        }

        playButton = Button(this).apply {
            text = "▶ PLAY"
            setOnClickListener {
                playOrPause()
            }
        }

        val backButton = Button(this).apply {
            text = "← BACK TO LIBRARY"
            setOnClickListener {
                finish()
            }
        }

        root.addView(nowPlaying)
        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        root.addView(playButton)
        root.addView(backButton)

        setContentView(root)

        if (songUriText != null) {
            try {
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(this@PlayerActivity, Uri.parse(songUriText))
                    prepare()
                    start()
                }

                playButton.text = "⏸ PAUSE"

            } catch (e: Exception) {
                Toast.makeText(
                    this,
                    "This song could not be played.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun playOrPause() {
        val player = mediaPlayer ?: return

        if (player.isPlaying) {
            player.pause()
            playButton.text = "▶ PLAY"
        } else {
            player.start()
            playButton.text = "⏸ PAUSE"
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
