package com.axxist.app.runtime.audio

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.AudioEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.audio.state.AudioState
import com.axxist.app.runtime.audio.state.AudioStateManager

/**
 * AudioManager - Main coordinator for the audio system.
 * 
 * Responsibilities:
 * - Control audio lifecycle
 * - Start/stop capture
 * - Maintain current state
 * - Integrate with RuntimeManager
 * - Handle permissions
 */
class AudioManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "AudioManager"
        
        @Volatile
        private var instance: AudioManager? = null
        
        fun initialize(context: Context): AudioManager {
            return instance ?: synchronized(this) {
                instance ?: AudioManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): AudioManager {
            return instance ?: throw IllegalStateException("AudioManager not initialized")
        }
    }
    
    private val stateManager = AudioStateManager.getInstance()
    private val listeners = mutableListOf<AudioManagerListener>()
    
    /**
     * AudioManager listener interface.
     */
    interface AudioManagerListener {
        fun onAudioStateChanged(state: AudioState, previousState: AudioState)
        fun onAudioError(error: String)
        fun onVoiceActivityDetected()
        fun onVoiceActivityEnded()
    }
    
    init {
        Logger.d(TAG, "AudioManager initialized")
    }
    
    /**
     * Check if audio permission is granted.
     */
    fun hasPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Get the required permission.
     */
    fun getRequiredPermission(): String = Manifest.permission.RECORD_AUDIO
    
    /**
     * Initialize the audio system.
     */
    suspend fun initialize(): Boolean {
        Logger.d(TAG, "Initializing audio system...")
        
        if (!hasPermission()) {
            Logger.w(TAG, "Audio permission not granted")
            stateManager.error("Permission not granted")
            return false
        }
        
        if (!stateManager.initialize()) {
            return false
        }
        
        EventBusManager.emitAudioEvent(AudioEvent.Initializing)
        
        // In a real implementation, we would initialize audio providers here
        // For now, we just mark initialization as complete
        
        stateManager.initializeComplete()
        Logger.d(TAG, "Audio system initialized")
        return true
    }
    
    /**
     * Start audio capture/listening.
     */
    suspend fun startListening(): Boolean {
        Logger.d(TAG, "Starting audio listening...")
        
        if (!hasPermission()) {
            Logger.e(TAG, "Cannot start listening: permission denied")
            stateManager.error("Permission not granted")
            return false
        }
        
        if (stateManager.getCurrentState() == AudioState.IDLE) {
            if (!initialize()) {
                return false
            }
        }
        
        if (!stateManager.startListening()) {
            return false
        }
        
        EventBusManager.emitAudioEvent(AudioEvent.Started)
        notifyListeners { it.onAudioStateChanged(stateManager.getCurrentState(), AudioState.INITIALIZING) }
        
        Logger.d(TAG, "Audio listening started")
        return true
    }
    
    /**
     * Start audio processing (e.g., for speech recognition).
     */
    suspend fun startProcessing(): Boolean {
        Logger.d(TAG, "Starting audio processing...")
        
        if (!stateManager.startProcessing()) {
            return false
        }
        
        notifyListeners { it.onAudioStateChanged(stateManager.getCurrentState(), AudioState.LISTENING) }
        return true
    }
    
    /**
     * Stop audio capture.
     */
    suspend fun stop(): Boolean {
        Logger.d(TAG, "Stopping audio...")
        
        val previousState = stateManager.getCurrentState()
        
        if (!stateManager.stop()) {
            return false
        }
        
        // In a real implementation, we would stop audio providers here
        
        stateManager.stopComplete()
        EventBusManager.emitAudioEvent(AudioEvent.Stopped)
        notifyListeners { it.onAudioStateChanged(stateManager.getCurrentState(), previousState) }
        
        Logger.d(TAG, "Audio stopped")
        return true
    }
    
    /**
     * Get current audio state.
     */
    fun getState(): AudioState = stateManager.getCurrentState()
    
    /**
     * Check if audio is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Notify voice activity detected.
     */
    fun notifyVoiceActivityDetected() {
        EventBusManager.emitAudioEvent(AudioEvent.VoiceActivityDetected)
        notifyListeners { it.onVoiceActivityDetected() }
    }
    
    /**
     * Notify voice activity ended.
     */
    fun notifyVoiceActivityEnded() {
        EventBusManager.emitAudioEvent(AudioEvent.VoiceActivityEnded)
        notifyListeners { it.onVoiceActivityEnded() }
    }
    
    /**
     * Set audio error.
     */
    fun setError(message: String) {
        stateManager.error(message)
        notifyListeners { it.onAudioError(message) }
    }
    
    /**
     * Add listener.
     */
    fun addListener(listener: AudioManagerListener) {
        listeners.add(listener)
    }
    
    /**
     * Remove listener.
     */
    fun removeListener(listener: AudioManagerListener) {
        listeners.remove(listener)
    }
    
    /**
     * Get audio summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "hasPermission" to hasPermission(),
        "listenersCount" to listeners.size
    )
    
    /**
     * Reset audio manager.
     */
    fun reset() {
        Logger.d(TAG, "Resetting AudioManager...")
        stateManager.reset()
        listeners.clear()
    }
    
    /**
     * Notify all listeners.
     */
    private fun notifyListeners(action: (AudioManagerListener) -> Unit) {
        listeners.forEach { listener ->
            try {
                action(listener)
            } catch (e: Exception) {
                Logger.e(TAG, "Error notifying listener", e)
            }
        }
    }
}
