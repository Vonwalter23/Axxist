package com.axxist.app.runtime.audio.provider

/**
 * SpeechRecognizerProvider - Contract for speech recognition engines.
 * 
 * This interface defines the contract for future speech-to-text implementations.
 * Implementations can include:
 * - Android SpeechRecognizer (on-device)
 * - Cloud-based services (Groq, Google, etc.)
 * - Local ML models (Vosk, Whisper, etc.)
 */
interface SpeechRecognizerProvider {
    
    /**
     * Recognition result.
     */
    data class RecognitionResult(
        val text: String,
        val confidence: Float = 1.0f,
        val isFinal: Boolean = true,
        val alternatives: List<String> = emptyList(),
        val metadata: Map<String, Any> = emptyMap(),
        val error: String? = null
    )
    
    /**
     * Recognition state.
     */
    enum class RecognitionState {
        IDLE,
        LISTENING,
        PROCESSING,
        RESULT,
        ERROR
    }
    
    /**
     * Initialize the speech recognizer.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Start continuous listening for speech.
     */
    suspend fun startListening(): Boolean
    
    /**
     * Stop listening.
     */
    suspend fun stopListening(): Boolean
    
    /**
     * Process audio data and return recognition result.
     */
    suspend fun recognize(audioData: ByteArray): RecognitionResult
    
    /**
     * Check if currently listening.
     */
    fun isListening(): Boolean
    
    /**
     * Set recognition listener.
     */
    fun setListener(listener: RecognitionListener?)
    
    /**
     * Set recognition language.
     */
    suspend fun setLanguage(language: String): Boolean
    
    /**
     * Get supported languages.
     */
    fun getSupportedLanguages(): List<String>
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Check if recognizer is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Listener interface for recognition events.
     */
    interface RecognitionListener {
        fun onReadyForSpeech()
        fun onBeginningOfSpeech()
        fun onEndOfSpeech()
        fun onResult(result: RecognitionResult)
        fun onError(error: String, code: Int = -1)
        fun onStateChanged(state: RecognitionState)
    }
}
