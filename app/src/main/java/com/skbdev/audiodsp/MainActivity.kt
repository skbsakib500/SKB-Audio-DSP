package com.skbdev.audiodsp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // AMOLED Pure Black Background for maximum battery saving
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(60, 80, 60, 60)
            setBackgroundColor(Color.BLACK)
        }

        val title = TextView(this).apply {
            text = "SKB Audio DSP"
            textSize = 24f
            setTextColor(Color.parseColor("#00FF66")) // Neon Green accent for high-end look
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 20)
        }
        layout.addView(title)

        val subtitle = TextView(this).apply {
            text = "Rootless Hi-Res Upsampler & Native C++ DSP Engine\nAMOLED Power Saver Mode Active"
            textSize = 14f
            setTextColor(Color.parseColor("#888888"))
            setPadding(0, 0, 0, 60)
        }
        layout.addView(subtitle)

        val startBtn = Button(this).apply {
            text = "START DSP SERVICE"
            setBackgroundColor(Color.parseColor("#00FF66"))
            setTextColor(Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(20, 30, 20, 30)
            setOnClickListener {
                val intent = Intent(this@MainActivity, AudioDSPService::class.java)
                startForegroundService(intent)
            }
        }
        layout.addView(startBtn)

        setContentView(layout)
    }
}
