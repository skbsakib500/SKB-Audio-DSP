package com.skbdev.audiodsp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AudioDSPService : Service() {

    companion object {
        const val CHANNEL_ID = "SKBAudioDSPChannel"
        const val NOTIFICATION_ID = 2070
    }

    // Load the native C++ compiled library (.so)
    init {
        System.loadLibrary("audio_dsp_jni")
    }

    // Declare Native JNI function
    external fun nativeProcessAudio(inputPath: String): String

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Build the permanent Foreground Notification so Android never kills the service
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SKB 2070 Neural DSP Active 🎧")
            .setContentText("768kHz Hi-Res Quantum Upsampling & 8D Spatial Running...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        // Trigger native C++ processing in background
        val result = nativeProcessAudio("dummy_stream_path")
        android.util.Log.d("SKB-DSP", result)

        // START_STICKY ensures service restarts if system kills it
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "SKB Audio DSP Foreground Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
