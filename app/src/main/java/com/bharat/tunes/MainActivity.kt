package com.bharat.tunes

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(12, 12, 18)
        window.navigationBarColor = Color.rgb(12, 12, 18)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 40, 24, 24)
            setBackgroundColor(Color.rgb(12, 12, 18))
        }

        val title = TextView(this).apply {
            text = "BHARAT TUNES"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "YOUR MUSIC. YOUR VIBE."
            textSize = 13f
            setTextColor(Color.rgb(170, 140, 255))
            gravity = Gravity.CENTER
        }

        val heading = TextView(this).apply {
            text = "Your Music Library"
            textSize = 23f
            setTextColor(Color.WHITE)
            setPadding(0, 55, 0, 15)
        }

        val message = TextView(this).apply {
            text = "Tap Scan My Music to find songs."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 20)
        }

        val scanButton = Button(this).apply {
            text = "SCAN MY MUSIC"
        }

        // यह जगह गानों की list दिखाने के लिए है
        val songList = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scrollView = ScrollView(this).apply {
            addView(songList)
        }

        scanButton.setOnClickListener {
            scanMusic(songList, message)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(heading)
        root.addView(message)
        root.addView(
            scanButton,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        root.addView(
            scrollView,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
    }

    private fun scanMusic(songList: LinearLayout, message: TextView) {

        // पुरानी list हटाओ
        songList.removeAllViews()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.TITLE + " ASC"
        )

        if (cursor != null && cursor.moveToFirst()) {

            val idColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            val titleColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            var songCount = 0

            do {
                val songId = cursor.getLong(idColumn)
                val songTitle = cursor.getString(titleColumn)

                val songUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    songId.toString()
                )

                val songButton = Button(this).apply {
                    text = "🎵 $songTitle"
                    isAllCaps = false
                    setOnClickListener {
                        playSong(songUri, songTitle)
                    }
                }

                songList.addView(
                    songButton,
                    LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )

                songCount++

            } while (cursor.moveToNext())

            message.text = "Found $songCount song(s). Tap a song to play! 🎵"

            cursor.close()

        } else {
            message.text = "No music files found on your phone."
            cursor?.close()
        }
    }

    private fun playSong(uri: Uri, title: String) {

        // अगर कोई पुराना गाना चल रहा है तो बंद करो
        mediaPlayer?.release()

        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(this@MainActivity, uri)
                prepare()
                start()
            }

            Toast.makeText(
                this,
                "Now playing: $title 🎵",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Could not play this song.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
