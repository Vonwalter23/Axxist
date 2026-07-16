package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.Intent

/**
 * Base interface for all intent processors.
 * This is the main entry point for intent processing.
 */
interface IntentProcessor {
    
    /**
     * Process input text and return detected intent.
     */
    suspend fun process(input: String): ProcessorResult
    
    /**
     * Check if this processor is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Get the processor type.
     */
    fun getProcessorType(): ProcessorType
    
    /**
     * Get the processor name.
     */
    fun getName(): String
    
    enum class ProcessorType {
        RULE_BASED,
        AI_BASED,
        HYBRID
    }
    
    data class ProcessorResult(
        val intent: Intent?,
        val success: Boolean,
        val confidence: Float,
        val method: ProcessorType,
        val error: String? = null
    )
}
