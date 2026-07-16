package com.axxist.app.runtime.action.model

/**
 * Metadata for actions.
 */
data class ActionMetadata(
    val intentId: String? = null,
    val sessionId: String? = null,
    val userId: String? = null,
    val source: String = "unknown",
    val timestamp: Long = System.currentTimeMillis(),
    val tags: List<String> = emptyList(),
    val extras: Map<String, Any> = emptyMap()
)
