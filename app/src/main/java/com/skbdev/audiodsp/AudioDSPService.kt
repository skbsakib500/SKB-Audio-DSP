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

        init {
            System.loadLibrary("audio_dsp_jni")
        }
    }

    private external fun nativeInitDSP(sampleRate: Int): Boolean
    private external fun nativeProcessBuffer(inputBuffer: IntArray): IntArray?
    private external fun nativeCloseDSP()

    private var isEngineRunning = false

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isEngineRunning = true
        
        // Initialize 768kHz Ultra High-Res Upsampling
        nativeInitDSP(768000)
        Log.d(TAG, "🚀 SKB Audio DSP Foreground Service Running (768kHz Mode)")

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SKB Audio DSP Active")
            .setContentText("768kHz Ultra High-Res Upsampler Engaged")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        // Low overhead heartbeat thread to keep service alive and responsive
        Thread {
            val dummyBuffer = intArrayOf(100, 200, 300)
            while (isEngineRunning) {
                try {
                    Thread.sleep(5000) // ৫ সেকেন্ড পর পর হালকা পালস
                    nativeProcessBuffer(dummyBuffer)
                } catch (e: InterruptedException) {
                    break
                }
            }
        }.start()

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isEngineRunning = false
        nativeCloseDSP()
        Log.d(TAG, "🛑 SKB Audio DSP Service Destroyed.")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Audio DSP Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
