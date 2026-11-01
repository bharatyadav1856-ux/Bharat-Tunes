package com.bharat.tunes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {

    private val storagePermissionCode = 100
    private lateinit var songList: LinearLayout
    private lateinit var message: TextView

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

        message = TextView(this).apply {
            text = "Tap Scan My Music to find songs."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 20)
        }

        val scanButton = Button(this).apply {
            text = "SCAN MY MUSIC"
            setOnClickListener {
                checkPermissionAndScan()
            }
        }

        songList = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scrollView = ScrollView(this).apply {
            addView(songList)
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(heading)
        root.addView(message)
        root.addView(scanButton)

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

    private fun checkPermissionAndScan() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            scanMusic()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                storagePermissionCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )

        if (requestCode == storagePermissionCode &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            scanMusic()
        } else {
            message.text = "Permission is needed to scan your music."
        }
    }

    private fun scanMusic() {
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

        var songCount = 0

        cursor?.use {
            val idColumn =
                it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            val titleColumn =
                it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            while (it.moveToNext()) {
                val songId = it.getLong(idColumn)
                val songTitle = it.getString(titleColumn)

                val songUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    songId.toString()
                )

                val songButton = Button(this).apply {
                    text = "🎵 $songTitle"
                    isAllCaps = false

                    setOnClickListener {
                        val intent =
                            Intent(this@MainActivity, PlayerActivity::class.java)

                        intent.putExtra("SONG_URI", songUri.toString())
                        intent.putExtra("SONG_TITLE", songTitle)

                        startActivity(intent)
                    }
                }

                songList.addView(songButton)
                songCount++
            }
        }

        message.text = if (songCount > 0) {
            "Found $songCount song(s). Tap a song to open the player! 🎵"
        } else {
            "No music files found."
        }
    }
}
