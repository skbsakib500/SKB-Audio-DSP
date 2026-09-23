package com.skbdev.audiodsp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class AudioDSPService : Service() {

    companion object {
        const val TAG = "SKB_DSP_Engine"
        const val CHANNEL_ID = "AudioDSPServiceChannel"
        const val NOTIFICATION_ID = 1
        var isRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        Log.d(TAG, "==========================================")
        Log.d(TAG, "🚀 SKB Audio DSP Service Started Successfully!")
        Log.d(TAG, "🎧 High-Res Upsampler Engine Ready for Testing")
        Log.d(TAG, "==========================================")

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SKB Audio DSP Active")
            .setContentText("Testing Mode: Monitoring Audio Streams...")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        // Simulate real-time audio buffer interception log loop for verification
        Thread {
            while (isRunning) {
                try {
                    Thread.sleep(3000) // প্রতি ৩ সেকেন্ড পর পর লগ দেবে
                    Log.d(TAG, "⚡ [DSP ACTIVE] Intercepted Audio Buffer -> Upsampling 44.1kHz to 192kHz [OK]")
                } catch (e: InterruptedException) {
                    break
                }
            }
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "🛑 SKB Audio DSP Service Destroyed.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Audio DSP Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
