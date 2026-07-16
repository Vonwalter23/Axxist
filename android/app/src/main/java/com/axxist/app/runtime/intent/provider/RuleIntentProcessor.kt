package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.Intent

/**
 * Interface for rule-based intent processing.
 * Uses pattern matching and predefined rules.
 */
interface RuleIntentProcessor {
    
    /**
     * Process input text and return detected intent.
     */
    suspend fun process(input: String): RuleBasedResult
    
    /**
     * Check if this processor can handle the input.
     */
    fun canProcess(input: String): Boolean
    
    /**
     * Get the processor name.
     */
    fun getName(): String = "RuleBasedIntentProcessor"
    
    data class RuleBasedResult(
        val intent: Intent?,
        val matchedPattern: String? = null,
        val confidence: Float = 0f,
        val error: String? = null
    ) {
        val isSuccess: Boolean get() = intent != null && error == null
    }
}
