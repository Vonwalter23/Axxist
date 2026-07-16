package com.axxist.app.runtime.wakeword

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.WakeWordEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.audio.AudioManager
import com.axxist.app.runtime.audio.state.AudioState
import com.axxist.app.runtime.wakeword.state.WakeWordState
import com.axxist.app.runtime.wakeword.state.WakeWordStateManager

/**
 * WakeWordManager - Main coordinator for wake word detection.
 * 
 * Responsibilities:
 * - Initialize wake word subsystem
 * - Start/stop detection
 * - Manage states
 * - Consume AudioInputProvider (not duplicate audio capture)
 */
class WakeWordManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "WakeWordManager"
        
        @Volatile
        private var instance: WakeWordManager? = null
        
        fun initialize(context: Context): WakeWordManager {
            return instance ?: synchronized(this) {
                instance ?: WakeWordManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): WakeWordManager {
            return instance ?: throw IllegalStateException("WakeWordManager not initialized")
        }
    }
    
    private val stateManager = WakeWordStateManager.getInstance()
    private var provider: WakeWordProvider? = null
    private val listeners = mutableListOf<WakeWordListener>()
    
    /**
     * WakeWord listener interface.
     */
    interface WakeWordListener {
        fun onWakeWordDetected(keyword: String, confidence: Float)
        fun onWakeWordStateChanged(state: WakeWordState, previousState: WakeWordState)
        fun onWakeWordError(error: String)
    }
    
    init {
        Logger.d(TAG, "WakeWordManager initialized")
    }
    
    /**
     * Set the wake word provider.
     */
    fun setProvider(wakeWordProvider: WakeWordProvider?) {
        this.provider = wakeWordProvider
        Logger.d(TAG, "Wake word provider set: ${wakeWordProvider?.getEngineType() ?: "none"}")
    }
    
    /**
     * Get current wake word provider.
     */
    fun getProvider(): WakeWordProvider? = provider
    
    /**
     * Initialize the wake word detection.
     */
    suspend fun initialize(config: WakeWordProvider.WakeWordConfig): Boolean {
        Logger.d(TAG, "Initializing wake word detection...")
        
        if (!stateManager.initialize()) {
            return false
        }
        
        EventBusManager.emitWakeWordEvent(WakeWordEvent.Initializing)
        
        // Provider is optional - manager can work without it for testing
        provider?.let { prov ->
            if (!prov.isAvailable()) {
                Logger.w(TAG, "Wake word provider not available")
                stateManager.error("Provider not available")
                return false
            }
            
            if (!prov.initialize(config)) {
                Logger.e(TAG, "Failed to initialize wake word provider")
                stateManager.error("Provider initialization failed")
                return false
            }
        }
        
        stateManager.initializeComplete()
        EventBusManager.emitWakeWordEvent(WakeWordEvent.Ready)
        Logger.d(TAG, "Wake word detection initialized")
        return true
    }
    
    /**
     * Start listening for wake words.
     */
    suspend fun startListening(): Boolean {
        Logger.d(TAG, "Starting wake word listening...")
        
        val currentState = stateManager.getCurrentState()
        if (currentState == WakeWordState.LISTENING) {
            Logger.w(TAG, "Already listening")
            return true
        }
        
        if (currentState == WakeWordState.DISABLED || currentState == WakeWordState.ERROR) {
            // Need to initialize first
            if (!initialize(WakeWordProvider.WakeWordConfig())) {
                return false
            }
        }
        
        if (!stateManager.startListening()) {
            return false
        }
        
        // Set up provider listener if available
        provider?.setListener(object : WakeWordProvider.WakeWordListener {
            override fun onWakeWordDetected(result: WakeWordProvider.WakeWordResult) {
                handleWakeWordDetected(result.keyword, result.confidence)
            }
            
            override fun onError(error: String) {
                stateManager.error(error)
            }
            
            override fun onStateChanged(isListening: Boolean) {
                // Provider state changed
            }
        })
        
        // Start the provider listening
        provider?.startListening()
        
        EventBusManager.emitWakeWordEvent(WakeWordEvent.Listening)
        notifyListeners { it.onWakeWordStateChanged(stateManager.getCurrentState(), WakeWordState.LISTENING) }
        
        Logger.d(TAG, "Wake word listening started")
        return true
    }
    
    /**
     * Stop listening.
     */
    suspend fun stop(): Boolean {
        Logger.d(TAG, "Stopping wake word listening...")
        
        val previousState = stateManager.getCurrentState()
        
        if (!stateManager.stop()) {
            return false
        }
        
        provider?.stopListening()
        provider?.setListener(null)
        
        EventBusManager.emitWakeWordEvent(WakeWordEvent.Stopped)
        notifyListeners { it.onWakeWordStateChanged(stateManager.getCurrentState(), previousState) }
        
        Logger.d(TAG, "Wake word listening stopped")
        return true
    }
    
    /**
     * Handle wake word detection.
     */
    private fun handleWakeWordDetected(keyword: String, confidence: Float) {
        Logger.d(TAG, "Wake word detected: $keyword (confidence: $confidence)")
        
        stateManager.detected()
        EventBusManager.emitWakeWordEvent(WakeWordEvent.Detected(keyword, confidence))
        notifyListeners { it.onWakeWordDetected(keyword, confidence) }
        
        // Automatically return to listening after detection
        stateManager.resetAfterDetection()
    }
    
    /**
     * Release all resources.
     */
    fun release() {
        Logger.d(TAG, "Releasing WakeWordManager resources...")
        kotlinx.coroutines.runBlocking {
            provider?.release()
        }
        provider = null
        stateManager.reset()
        listeners.clear()
        Logger.d(TAG, "WakeWordManager resources released")
    }
    
    /**
     * Get current wake word state.
     */
    fun getState(): WakeWordState = stateManager.getCurrentState()
    
    /**
     * Check if wake word detection is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Add listener.
     */
    fun addListener(listener: WakeWordListener) {
        listeners.add(listener)
    }
    
    /**
     * Remove listener.
     */
    fun removeListener(listener: WakeWordListener) {
        listeners.remove(listener)
    }
    
    /**
     * Get wake word summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "providerType" to (provider?.getEngineType()?.name ?: "NONE"),
        "providerAvailable" to (provider?.isAvailable() ?: false),
        "listenersCount" to listeners.size
    )
    
    /**
     * Reset wake word manager.
     */
    fun reset() {
        Logger.d(TAG, "Resetting WakeWordManager...")
        kotlinx.coroutines.runBlocking {
            provider?.release()
        }
        provider = null
        stateManager.reset()
        listeners.clear()
    }
    
    /**
     * Notify all listeners.
     */
    private fun notifyListeners(action: (WakeWordListener) -> Unit) {
        listeners.forEach { listener ->
            try {
                action(listener)
            } catch (e: Exception) {
                Logger.e(TAG, "Error notifying listener", e)
            }
        }
    }
}
