package com.axxist.app.runtime.conversation.provider

/**
 * IntentProcessor - Contract for intent processing.
 * 
 * This interface defines the contract for future intent processing implementations.
 * Implementations can include:
 * - Rule-based intent parsing
 * - AI-powered intent recognition
 * - Keyword matching
 * - Custom intent handlers
 */
interface IntentProcessor {
    
    /**
     * Intent result from processing.
     */
    data class IntentResult(
        val intent: String,
        val entities: Map<String, Any> = emptyMap(),
        val confidence: Float = 1.0f,
        val rawText: String = "",
        val error: String? = null
    )
    
    /**
     * Initialize the intent processor.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Process user input and extract intent.
     */
    suspend fun process(text: String): IntentResult
    
    /**
     * Process with context.
     */
    suspend fun process(text: String, context: Map<String, Any>): IntentResult
    
    /**
     * Check if processor is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Get supported intents.
     */
    fun getSupportedIntents(): List<String>
}
