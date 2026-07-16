package com.axxist.app.core

import com.axxist.app.core.build.BuildConfiguration
import com.axxist.app.core.capability.CapabilityManager
import com.axxist.app.core.config.ConfigKeys
import com.axxist.app.core.config.ConfigManager
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.lifecycle.AppLifecycleManager
import com.axxist.app.core.logger.LogLevel
import com.axxist.app.core.logger.Logger
import android.content.Context

/**
 * AndroidCore - Main entry point for the Android Core.
 * 
 * This is the central coordinator for all core modules.
 * It initializes all core modules in the correct order.
 */
object AndroidCore {
    
    private const val TAG = "AndroidCore"
    
    private lateinit var context: Context
    private lateinit var lifecycleManager: AppLifecycleManager
    
    private var isInitialized = false
    
    /**
     * Initialize the Android Core.
     * Must be called once during app startup.
     */
    fun initialize(context: Context) {
        if (isInitialized) {
            Logger.w(TAG, "AndroidCore already initialized")
            return
        }
        
        this.context = context.applicationContext
        
        // Initialize in order: Logger -> EventBus -> Config -> Capability -> Lifecycle
        initializeLogger()
        initializeEventBus()
        initializeConfig()
        initializeCapabilities()
        initializeLifecycle()
        initializeBuildConfig()
        
        isInitialized = true
        Logger.d(TAG, "AndroidCore initialized successfully")
    }
    
    private fun initializeLogger() {
        Logger.initialize(LogLevel.DEBUG)
        Logger.d(TAG, "Logger initialized")
    }
    
    private fun initializeEventBus() {
        EventBusManager.initialize()
        Logger.d(TAG, "EventBus initialized")
    }
    
    private fun initializeConfig() {
        ConfigManager.initialize(context)
        
        // Set app version in config
        val config = ConfigManager.getInstance()
        config.setString(ConfigKeys.APP_VERSION, BuildConfiguration.VERSION_NAME)
        config.setBoolean(ConfigKeys.DEBUG_MODE, BuildConfiguration.IS_DEBUG)
        
        Logger.d(TAG, "ConfigManager initialized")
    }
    
    private fun initializeCapabilities() {
        CapabilityManager.initialize()
        Logger.d(TAG, "CapabilityManager initialized")
    }
    
    private fun initializeLifecycle() {
        lifecycleManager = AppLifecycleManager()
        lifecycleManager.initialize(context as android.app.Application)
        Logger.d(TAG, "AppLifecycleManager initialized")
    }
    
    private fun initializeBuildConfig() {
        BuildConfiguration.initialize()
        Logger.d(TAG, "BuildConfiguration initialized")
    }
    
    /**
     * Get the application context.
     */
    fun getContext(): Context {
        return context
    }
    
    /**
     * Get the ConfigManager instance.
     */
    fun getConfigManager(): ConfigManager {
        return ConfigManager.getInstance()
    }
    
    /**
     * Get the EventBusManager instance.
     */
    fun getEventBusManager(): EventBusManager {
        return EventBusManager
    }
    
    /**
     * Get the CapabilityManager instance.
     */
    fun getCapabilityManager(): CapabilityManager {
        return CapabilityManager
    }
    
    /**
     * Get the AppLifecycleManager instance.
     */
    fun getLifecycleManager(): AppLifecycleManager {
        return lifecycleManager
    }
    
    /**
     * Check if AndroidCore is initialized.
     */
    fun isInitialized(): Boolean = isInitialized
    
    /**
     * Shutdown the Android Core.
     * Should be called when the app is being destroyed.
     */
    fun shutdown() {
        Logger.d(TAG, "Shutting down AndroidCore...")
        EventBusManager.shutdown()
        CapabilityManager.clear()
        Logger.setEnabled(false)
        isInitialized = false
        Logger.d(TAG, "AndroidCore shutdown complete")
    }
}
