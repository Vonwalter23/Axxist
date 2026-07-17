package com.axxist.app.runtime.intent.model

/**
 * Result of confidence evaluation.
 */
data class ConfidenceResult(
    val confidence: Float,
    val level: ConfidenceLevel,
    val factors: Map<String, Float> = emptyMap(),
    val passedThreshold: Boolean = false
)
