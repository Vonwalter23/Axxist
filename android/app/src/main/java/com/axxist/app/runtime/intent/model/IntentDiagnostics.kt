package com.axxist.app.runtime.intent.model

/**
 * Diagnostic information for intent processing.
 */
data class IntentDiagnostics(
    val analysisDurationMs: Long = 0,
    val methodUsed: IntentProcessingMethod? = null,
    val providerSelected: String? = null,
    val entitiesExtractedCount: Int = 0,
    val validationDurationMs: Long = 0,
    val totalDurationMs: Long = 0,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val metadata: Map<String, Any> = emptyMap()
)
