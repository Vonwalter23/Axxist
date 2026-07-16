package com.axxist.app.runtime.audio.provider

/**
 * VoiceActivityDetector - Contract for voice activity detection.
 * 
 * This interface defines the contract for future VAD implementations.
 * Implementations can include:
 * - Energy-based detection
 * - ML-based detection
 * - WebRTC VAD
 * - Silero VAD
 */
interface VoiceActivityDetector {
    
    /**
     * Voice activity detection result.
     */
    data class VADResult(
        val isVoice: Boolean,
        val confidence: Float = 0.0f,
        val energy: Float = 0.0f,
        val metadata: Map<String, Any> = emptyMap()
    )
    
    /**
     * VAD configuration.
     */
    data class VADConfig(
        val threshold: Float = 0.5f,
        val minSpeechDurationMs: Int = 250,
        val maxSpeechDurationMs: Int = 30000,
        val minSilenceDurationMs: Int = 300,
        val sampleRate: Int = 16000
    )
    
    /**
     * Initialize the VAD.
     */
    suspend fun initialize(config: VADConfig): Boolean
    
    /**
     * Process audio frame and detect voice activity.
     */
    suspend fun detect(audioData: ByteArray): VADResult
    
    /**
     * Reset the detector state.
     */
    fun reset()
    
    /**
     * Set VAD mode (aggressive levels).
     */
    suspend fun setMode(mode: VADMode): Boolean
    
    /**
     * Check if voice is currently detected.
     */
    fun isVoiceActive(): Boolean
    
    /**
     * Set detection listener.
     */
    fun setListener(listener: VADListener?)
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Check if detector is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * VAD mode/aggressiveness levels.
     */
    enum class VADMode {
        QUALITY,    // Most accurate, least aggressive
        LOW_BITRATE,
        AGGRESSIVE,
        VERY_AGGRESSIVE  // Most aggressive
    }
    
    /**
     * Listener interface for VAD events.
     */
    interface VADListener {
        fun onVoiceStart()
        fun onVoiceEnd()
        fun onVADResult(result: VADResult)
    }
}
