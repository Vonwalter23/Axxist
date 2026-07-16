package com.axxist.app.runtime.wakeword

/**
 * WakeWordProvider - Contract for wake word detection engines.
 * 
 * This interface defines the contract for future wake word implementations.
 * Implementations can include:
 * - Picovoice Porcupine
 * - Snowboy
 * - TensorFlow Lite models
 * - Whisper keyword detection
 * - Custom neural networks
 * - Energy-based detection
 */
interface WakeWordProvider {
    
    /**
     * Wake word detection result.
     */
    data class WakeWordResult(
        val keyword: String,
        val confidence: Float = 1.0f,
        val metadata: Map<String, Any> = emptyMap()
    )
    
    /**
     * Wake word configuration.
     */
    data class WakeWordConfig(
        val keywords: List<String> = listOf("hey axxist", "axxist"),
        val sensitivity: Float = 0.5f,
        val audioSampleRate: Int = 16000,
        val audioChunkSize: Int = 512
    )
    
    /**
     * Supported wake word engine types.
     */
    enum class EngineType {
        PORCUPINE,
        SNOWBOY,
        TFLITE,
        WHISPER_KEYWORD,
        CUSTOM,
        NONE
    }
    
    /**
     * Initialize the wake word detector.
     */
    suspend fun initialize(config: WakeWordConfig): Boolean
    
    /**
     * Start listening for wake words.
     */
    suspend fun startListening(): Boolean
    
    /**
     * Stop listening.
     */
    suspend fun stopListening(): Boolean
    
    /**
     * Process audio data and check for wake word.
     * Returns the detected wake word or null.
     */
    suspend fun processAudio(audioData: ByteArray): WakeWordResult?
    
    /**
     * Check if currently listening.
     */
    fun isListening(): Boolean
    
    /**
     * Set detection listener.
     */
    fun setListener(listener: WakeWordListener?)
    
    /**
     * Get the engine type.
     */
    fun getEngineType(): EngineType
    
    /**
     * Get supported keywords.
     */
    fun getSupportedKeywords(): List<String>
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Check if provider is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Listener interface for wake word events.
     */
    interface WakeWordListener {
        fun onWakeWordDetected(result: WakeWordResult)
        fun onError(error: String)
        fun onStateChanged(isListening: Boolean)
    }
}
