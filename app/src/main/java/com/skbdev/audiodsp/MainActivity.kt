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

    private var isServiceRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
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
            textSize = 26f
            setTextColor(Color.parseColor("#00FF66"))
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(0, 0, 0, 15)
        }
        layout.addView(title)

        val subtitle = TextView(this).apply {
            text = "Rootless 768kHz Ultra High-Res Upsampler\nAMOLED Power Saver & Native C++ Engine"
            textSize = 14f
            setTextColor(Color.parseColor("#888888"))
            setPadding(0, 0, 0, 80)
        }
        layout.addView(subtitle)

        val toggleBtn = Button(this).apply {
            text = "START 768kHz DSP ENGINE"
            setBackgroundColor(Color.parseColor("#00FF66"))
            setTextColor(Color.BLACK)
            setTypeface(null, android.graphics.Typeface.BOLD)
            setPadding(20, 35, 20, 35)
            setOnClickListener {
                val intent = Intent(this@MainActivity, AudioDSPService::class.java)
                if (!isServiceRunning) {
                    // Start Service
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            startForegroundService(intent)
                        } else {
                            startService(intent)
                        }
                        isServiceRunning = true
                        text = "STOP 768kHz DSP ENGINE"
                        setBackgroundColor(Color.parseColor("#FF3333")) // Red warning color for stop action
                        setTextColor(Color.WHITE)
                        subtitle.text = "Rootless 768kHz Ultra High-Res Upsampler\nStatus: RUNNING (768kHz Active)"
                        Toast.makeText(this@MainActivity, "768kHz DSP Engine Engaged!", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Stop Service
                    try {
                        stopService(intent)
                        isServiceRunning = false
                        text = "START 768kHz DSP ENGINE"
                        setBackgroundColor(Color.parseColor("#00FF66")) // Neon green for start action
                        setTextColor(Color.BLACK)
                        subtitle.text = "Rootless 768kHz Ultra High-Res Upsampler\nStatus: DEACTIVATED (Idle)"
                        Toast.makeText(this@MainActivity, "DSP Engine Deactivated.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        layout.addView(toggleBtn)

        setContentView(layout)
    }
}
