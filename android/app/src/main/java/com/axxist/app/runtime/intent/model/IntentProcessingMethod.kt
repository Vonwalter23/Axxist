package com.axxist.app.runtime.intent.model

/**
 * Processing method used to detect the intent.
 */
enum class IntentProcessingMethod {
    RULE_BASED,    // Pattern matching and rules
    AI_BASED,      // AI/LLM interpretation
    HYBRID         // Combination of rule-based and AI
}
