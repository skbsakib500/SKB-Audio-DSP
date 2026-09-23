package com.skbdev.audiodsp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // সিম্পল ইউজার ইন্টারফেস যাতে অ্যাপ ওপেন করলেই কন্ট্রোল পাওয়া যায়
        val layout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        val title = TextView(this).apply {
            text = "SKB Audio DSP Engine"
            textSize = 20f
            setPadding(0, 0, 0, 30)
        }
        layout.addView(title)

        val startBtn = Button(this).apply {
            text = "Start Audio DSP Service"
            setOnClickListener {
                val intent = Intent(this@MainActivity, AudioDSPService::class.java)
                startForegroundService(intent)
            }
        }
        layout.addView(startBtn)

        setContentView(layout)
    }
}
