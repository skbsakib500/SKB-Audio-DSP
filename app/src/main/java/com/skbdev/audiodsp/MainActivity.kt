package com.skbdev.audiodsp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check & Request Notification Permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE)
            }
        }

        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(60, 80, 60, 60)
            setBackgroundColor(Color.BLACK)
        }

        val title = TextView(this).apply {
            text = "SKB Audio DSP"
            textSize = 24f
            setTextColor(Color.parseColor("#00FF66"))
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 20)
        }
        layout.addView(title)

        val subtitle = TextView(this).apply {
            text = "Rootless Hi-Res Upsampler Engine\nStatus: Ready to Engage"
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
                try {
                    val intent = Intent(this@MainActivity, AudioDSPService::class.java)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(intent)
                    } else {
                        startService(intent)
                    }
                    Toast.makeText(this@MainActivity, "DSP Service Started Successfully!", Toast.LENGTH_SHORT).show()
                    subtitle.text = "Rootless Hi-Res Upsampler Engine\nStatus: RUNNING (Active)"
                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        layout.addView(startBtn)

        setContentView(layout)
    }
}
