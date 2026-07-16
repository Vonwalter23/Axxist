package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.ConfidenceLevel
import com.axxist.app.runtime.intent.model.Intent

/**
 * Interface for confidence evaluation providers.
 */
interface ConfidenceProvider {
    
    /**
     * Evaluate confidence for an intent.
     */
    fun evaluate(intent: Intent): ConfidenceResult
    
    /**
     * Evaluate confidence with additional context.
     */
    fun evaluate(intent: Intent, context: Map<String, Any>): ConfidenceResult
    
    /**
     * Get the minimum confidence threshold.
     */
    fun getMinimumThreshold(): Float = 0.5f
    
    /**
     * Get the provider name.
     */
    fun getName(): String = "ConfidenceProvider"
    
    data class ConfidenceResult(
        val confidence: Float,
        val level: ConfidenceLevel,
        val factors: Map<String, Float> = emptyMap(),
        val passedThreshold: Boolean = confidence >= 0.5f
    )
}
