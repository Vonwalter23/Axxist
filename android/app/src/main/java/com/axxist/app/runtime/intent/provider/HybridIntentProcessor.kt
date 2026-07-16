package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.Intent

/**
 * Interface for hybrid intent processing.
 * Combines rule-based and AI-based approaches.
 */
interface HybridIntentProcessor {
    
    /**
     * Process input text using hybrid approach.
     */
    suspend fun process(input: String): HybridResult
    
    /**
     * Check if hybrid processing is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Get the processor name.
     */
    fun getName(): String = "HybridIntentProcessor"
    
    /**
     * Get priority mode (rule-first or ai-first).
     */
    fun getMode(): HybridMode = HybridMode.RULE_FIRST
    
    enum class HybridMode {
        RULE_FIRST,  // Try rules first, fallback to AI
        AI_FIRST,    // Try AI first, fallback to rules
        PARALLEL     // Run both and combine results
    }
    
    data class HybridResult(
        val intent: Intent?,
        val ruleBasedResult: RuleIntentProcessor.RuleBasedResult? = null,
        val aiBasedResult: AIIntentProcessor.AIBasedResult? = null,
        val finalConfidence: Float = 0f,
        val methodUsed: String = "unknown",
        val error: String? = null
    ) {
        val isSuccess: Boolean get() = intent != null && error == null
    }
}
