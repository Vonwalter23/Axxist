package com.axxist.app.runtime.manager

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.RuntimeEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.service.AssistantService
import com.axxist.app.runtime.state.RuntimeState
import com.axxist.app.runtime.state.RuntimeStateManager

/**
 * RuntimeManager - Main coordinator for the Axxist Runtime.
 * 
 * Responsible for:
 * - Starting the Runtime
 * - Stopping the Runtime
 * - Restarting the Runtime
 * - Querying state
 * - Registering modules
 * 
 * No other module should start or stop the service directly.
 */
class RuntimeManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "RuntimeManager"
        
        @Volatile
        private var instance: RuntimeManager? = null
        
        fun initialize(context: Context): RuntimeManager {
            return instance ?: synchronized(this) {
                instance ?: RuntimeManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): RuntimeManager {
            return instance ?: throw IllegalStateException("RuntimeManager not initialized")
        }
    }
    
    private val stateManager = RuntimeStateManager.getInstance()
    private val registeredModules = mutableMapOf<String, ModuleInfo>()
    private var serviceStarted = false
    
    data class ModuleInfo(
        val name: String,
        val version: String,
        val status: ModuleStatus = ModuleStatus.REGISTERED
    )
    
    enum class ModuleStatus {
        REGISTERED,
        INITIALIZED,
        RUNNING,
        ERROR,
        STOPPED
    }
    
    init {
        Logger.d(TAG, "RuntimeManager initialized")
    }
    
    /**
     * Start the Runtime.
     */
    fun start(): Boolean {
        Logger.d(TAG, "Starting Runtime...")
        
        if (stateManager.getCurrentState().isActive()) {
            Logger.w(TAG, "Runtime already running")
            return true
        }
        
        if (!stateManager.start()) {
            Logger.e(TAG, "Failed to start Runtime - invalid state transition")
            return false
        }
        
        try {
            AssistantService.startService(context)
            serviceStarted = true
            
            // Mark as running after service starts
            stateManager.running()
            
            EventBusManager.emitRuntimeEvent(RuntimeEvent.Started)
            Logger.d(TAG, "Runtime started successfully")
            return true
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to start service", e)
            stateManager.error("Service start failed: ${e.message}")
            return false
        }
    }
    
    /**
     * Stop the Runtime.
     */
    fun stop(): Boolean {
        Logger.d(TAG, "Stopping Runtime...")
        
        if (!stateManager.getCurrentState().isActive() && 
            stateManager.getCurrentState() != RuntimeState.STARTING) {
            Logger.w(TAG, "Runtime not running")
            return true
        }
        
        if (!stateManager.stop()) {
            Logger.e(TAG, "Failed to stop Runtime - invalid state transition")
            return false
        }
        
        try {
            if (serviceStarted) {
                AssistantService.stopService(context)
                serviceStarted = false
            }
            
            stateManager.stopComplete()
            EventBusManager.emitRuntimeEvent(RuntimeEvent.Stopped)
            Logger.d(TAG, "Runtime stopped successfully")
            return true
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to stop service", e)
            stateManager.error("Service stop failed: ${e.message}")
            return false
        }
    }
    
    /**
     * Restart the Runtime.
     */
    fun restart(): Boolean {
        Logger.d(TAG, "Restarting Runtime...")
        stop()
        Thread.sleep(500) // Brief pause
        return start()
    }
    
    /**
     * Get current runtime state.
     */
    fun getState(): RuntimeState = stateManager.getCurrentState()
    
    /**
     * Check if Runtime is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Register a module with the Runtime.
     */
    fun registerModule(name: String, version: String): Boolean {
        if (registeredModules.containsKey(name)) {
            Logger.w(TAG, "Module already registered: $name")
            return false
        }
        
        registeredModules[name] = ModuleInfo(name, version)
        Logger.d(TAG, "Module registered: $name v$version")
        return true
    }
    
    /**
     * Update module status.
     */
    fun updateModuleStatus(name: String, status: ModuleStatus): Boolean {
        val module = registeredModules[name] ?: run {
            Logger.w(TAG, "Module not found: $name")
            return false
        }
        
        registeredModules[name] = module.copy(status = status)
        Logger.d(TAG, "Module $name status updated: $status")
        return true
    }
    
    /**
     * Get registered modules.
     */
    fun getRegisteredModules(): Map<String, ModuleInfo> = registeredModules.toMap()
    
    /**
     * Get module info.
     */
    fun getModuleInfo(name: String): ModuleInfo? = registeredModules[name]
    
    /**
     * Called when the service starts.
     */
    fun onServiceStarted() {
        Logger.d(TAG, "Service started callback")
    }
    
    /**
     * Called when the service stops.
     */
    fun onServiceStopped() {
        Logger.d(TAG, "Service stopped callback")
    }
    
    /**
     * Get runtime uptime in milliseconds.
     */
    fun getUptime(): Long = stateManager.getUptime()
    
    /**
     * Get runtime summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "uptime" to getUptime(),
        "modulesCount" to registeredModules.size,
        "serviceStarted" to serviceStarted
    )
    
    /**
     * Reset the Runtime (for testing).
     */
    fun reset() {
        Logger.d(TAG, "Resetting Runtime...")
        stop()
        stateManager.reset()
        registeredModules.clear()
        serviceStarted = false
    }
}
