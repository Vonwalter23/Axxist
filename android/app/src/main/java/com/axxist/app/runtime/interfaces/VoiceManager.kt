package com.axxist.app.runtime.interfaces

/**
 * VoiceManager Interface - Prepared for future voice capabilities.
 * 
 * This interface defines the contract for voice-related functionality.
 * Implementation will be added in STAGE_03 (Audio Core) and STAGE_04 (Wake Word).
 */
interface VoiceManager {
    
    /**
     * Initialize the voice system.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Start listening for voice input.
     */
    suspend fun startListening(): Boolean
    
    /**
     * Stop listening.
     */
    suspend fun stopListening(): Boolean
    
    /**
     * Check if currently listening.
     */
    fun isListening(): Boolean
    
    /**
     * Set wake word detection enabled.
     */
    suspend fun setWakeWordEnabled(enabled: Boolean)
    
    /**
     * Check if wake word detection is enabled.
     */
    fun isWakeWordEnabled(): Boolean
    
    /**
     * Process audio (for speech recognition).
     */
    suspend fun processAudio(audioData: ByteArray): String?
    
    /**
     * Synthesize speech from text.
     */
    suspend fun synthesize(text: String): ByteArray?
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Check if module is available.
     */
    fun isAvailable(): Boolean
}
