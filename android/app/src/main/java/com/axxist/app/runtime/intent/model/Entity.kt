package com.axxist.app.runtime.intent.model

/**
 * Entity extracted from user input.
 */
data class Entity(
    val type: EntityType,
    val value: String,
    val confidence: Float = 1.0f,
    val startIndex: Int = -1,
    val endIndex: Int = -1,
    val metadata: Map<String, Any> = emptyMap()
) {
    fun isValid(): Boolean = value.isNotBlank() && confidence > 0f
}
