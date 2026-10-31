
package com.bharat.tunes

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(12, 12, 18)
        window.navigationBarColor = Color.rgb(12, 12, 18)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 40, 24, 24)
            gravity = Gravity.CENTER_HORIZONTAL
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
            setPadding(0, 55, 0, 20)
        }

        val message = TextView(this).apply {
            text = "Your favourite songs belong here."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 30)
        }

        val scanButton = Button(this).apply {
            text = "SCAN MY MUSIC"
            setOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    "Music scanning coming in the next step!",
                    Toast.LENGTH_SHORT
                ).show()
            }
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

        setContentView(root)
    }
}
