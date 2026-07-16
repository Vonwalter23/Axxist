package com.axxist.app.runtime.intent.model

/**
 * Confidence levels for intent detection.
 */
enum class ConfidenceLevel {
    VERY_LOW,  // 0.0 - 0.2
    LOW,       // 0.2 - 0.4
    MEDIUM,    // 0.4 - 0.6
    HIGH,      // 0.6 - 0.8
    VERY_HIGH; // 0.8 - 1.0
    
    companion object {
        fun fromValue(value: Float): ConfidenceLevel {
            return when {
                value < 0.2f -> VERY_LOW
                value < 0.4f -> LOW
                value < 0.6f -> MEDIUM
                value < 0.8f -> HIGH
                else -> VERY_HIGH
            }
        }
    }
}
