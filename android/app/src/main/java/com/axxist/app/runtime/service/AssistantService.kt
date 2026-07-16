package com.axxist.app.runtime.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.axxist.app.MainActivity
import com.axxist.app.R
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.ServiceEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.manager.RuntimeManager

/**
 * AssistantService - Foreground Service for Axxist Runtime.
 * 
 * This service maintains the Axxist assistant alive in the background.
 * It publishes a permanent notification and manages the runtime lifecycle.
 */
class AssistantService : Service() {
    
    companion object {
        private const val TAG = "AssistantService"
        
        const val CHANNEL_ID = "axxist_runtime_channel"
        const val NOTIFICATION_ID = 1001
        
        const val ACTION_START = "com.axxist.app.action.START_SERVICE"
        const val ACTION_STOP = "com.axxist.app.action.STOP_SERVICE"
        
        fun startService(context: Context) {
            val intent = Intent(context, AssistantService::class.java).apply {
                action = ACTION_START
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
        
        fun stopService(context: Context) {
            val intent = Intent(context, AssistantService::class.java).apply {
                action = ACTION_STOP
            }
            context.startService(intent)
        }
    }
    
    private var isRunning = false
    
    override fun onCreate() {
        super.onCreate()
        Logger.d(TAG, "Service onCreate")
        createNotificationChannel()
        EventBusManager.emitServiceEvent(ServiceEvent.Created)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d(TAG, "Service onStartCommand: ${intent?.action}")
        
        when (intent?.action) {
            ACTION_START -> {
                startForeground(NOTIFICATION_ID, createNotification())
                isRunning = true
                EventBusManager.emitServiceEvent(ServiceEvent.ForegroundStarted)
                RuntimeManager.getInstance().onServiceStarted()
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                isRunning = false
                EventBusManager.emitServiceEvent(ServiceEvent.ForegroundStopped)
                RuntimeManager.getInstance().onServiceStopped()
                stopSelf()
            }
        }
        
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    
    override fun onDestroy() {
        Logger.d(TAG, "Service onDestroy")
        isRunning = false
        EventBusManager.emitServiceEvent(ServiceEvent.ForegroundStopped)
        EventBusManager.emitServiceEvent(ServiceEvent.Destroyed)
        RuntimeManager.getInstance().onServiceStopped()
        super.onDestroy()
    }
    
    override fun onTaskRemoved(rootIntent: Intent?) {
        Logger.d(TAG, "Service onTaskRemoved")
        super.onTaskRemoved(rootIntent)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Axxist Runtime",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notificación permanente del asistente Axxist"
                setShowBadge(false)
                enableLights(false)
                enableVibration(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
            Logger.d(TAG, "Notification channel created: $CHANNEL_ID")
        }
    }
    
    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = PendingIntent.getService(
            this,
            1,
            Intent(this, AssistantService::class.java).apply {
                action = ACTION_STOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Axxist")
            .setContentText("Asistente preparado")
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    fun isServiceRunning(): Boolean = isRunning
}
