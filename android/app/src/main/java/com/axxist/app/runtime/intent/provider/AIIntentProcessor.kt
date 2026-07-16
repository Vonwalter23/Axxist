package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.Intent

/**
 * Interface for AI-based intent processing.
 * Uses AI/LLM for interpretation.
 */
interface AIIntentProcessor {
    
    /**
     * Process input text using AI and return detected intent.
     */
    suspend fun process(input: String): AIBasedResult
    
    /**
     * Check if AI processing is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Get the provider name.
     */
    fun getName(): String = "AIIntentProcessor"
    
    /**
     * Get the AI model being used.
     */
    fun getModel(): String = "unknown"
    
    data class AIBasedResult(
        val intent: Intent?,
        val rawResponse: String? = null,
        val confidence: Float = 0f,
        val error: String? = null
    ) {
        val isSuccess: Boolean get() = intent != null && error == null
    }
}
