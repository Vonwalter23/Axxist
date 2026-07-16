package com.axxist.app.runtime.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.axxist.app.core.config.ConfigKeys
import com.axxist.app.core.config.ConfigManager
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.RuntimeEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.manager.RuntimeManager

/**
 * BootReceiver - BroadcastReceiver that detects device boot.
 * 
 * IMPORTANT: This receiver does NOT automatically start the service.
 * It respects Android restrictions and user preferences.
 * 
 * The actual auto-start behavior should be controlled by user settings.
 */
class BootReceiver : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BootReceiver"
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "Boot broadcast received: ${intent.action}")
        
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            handleBootCompleted(context)
        }
    }
    
    private fun handleBootCompleted(context: Context) {
        Logger.d(TAG, "Device boot completed")
        
        // Emit boot completed event
        EventBusManager.emitRuntimeEvent(RuntimeEvent.BootCompleted)
        
        // Check if auto-start is enabled in preferences
        // NOTE: In this stage, we just log. Actual auto-start will be
        // implemented when user preferences are added (future stage)
        try {
            val config = ConfigManager.getInstance()
            val autoStartEnabled = config.getBoolean("auto_start_on_boot", false)
            
            if (autoStartEnabled) {
                Logger.d(TAG, "Auto-start enabled, starting Runtime...")
                RuntimeManager.getInstance().start()
            } else {
                Logger.d(TAG, "Auto-start disabled, Runtime will not start automatically")
            }
        } catch (e: Exception) {
            Logger.e(TAG, "Error checking auto-start preference", e)
        }
        
        Logger.d(TAG, "Boot handling complete")
    }
}
