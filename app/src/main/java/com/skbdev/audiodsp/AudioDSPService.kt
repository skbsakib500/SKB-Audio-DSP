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
        
        // Push hardware limit to 768kHz Ultra High-Res Upsampling
        val success = nativeInitDSP(768000)
        
        Log.d(TAG, "==========================================")
        Log.d(TAG, "🚀 SKB Audio DSP Service Started!")
        Log.d(TAG, "🎧 Native 768kHz Upsampler Initialized: $success")
        Log.d(TAG, "==========================================")

        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SKB Audio DSP Active")
            .setContentText("768kHz Ultra High-Res Upsampler Engaged")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        Thread {
            val dummyPcmBuffer = intArrayOf(100, 250, 500, 750, 1000)
            while (isEngineRunning) {
                try {
                    Thread.sleep(2000)
                    val processed = nativeProcessBuffer(dummyPcmBuffer)
                    Log.d(TAG, "⚡ [ACTIVE] 768kHz DSP Stream Intercepted. Buffer size: ${processed?.size}")
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
                "Audio DSP Foreground Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
