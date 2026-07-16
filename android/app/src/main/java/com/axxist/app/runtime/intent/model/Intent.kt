package com.axxist.app.runtime.intent.model

/**
 * Parsed intent from user input.
 */
data class Intent(
    val id: String,
    val name: String,
    val category: IntentCategory,
    val confidence: Float,
    val entities: List<Entity> = emptyList(),
    val parameters: Map<String, Any> = emptyMap(),
    val provider: IntentProcessingMethod = IntentProcessingMethod.RULE_BASED,
    val timestamp: Long = System.currentTimeMillis(),
    val originalText: String = ""
) {
    fun getEntity(type: EntityType): Entity? {
        return entities.find { it.type == type }
    }
    
    fun getEntities(type: EntityType): List<Entity> {
        return entities.filter { it.type == type }
    }
    
    fun getConfidenceLevel(): ConfidenceLevel {
        return ConfidenceLevel.fromValue(confidence)
    }
    
    fun isHighConfidence(): Boolean {
        return confidence >= 0.7f
    }
    
    fun isLowConfidence(): Boolean {
        return confidence < 0.5f
    }
}
