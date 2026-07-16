package com.axxist.app.runtime.intent.model

/**
 * Intent definition registered in the system.
 */
data class IntentDefinition(
    val id: String,
    val name: String,
    val description: String,
    val category: IntentCategory,
    val requiredEntities: List<EntityType> = emptyList(),
    val optionalEntities: List<EntityType> = emptyList(),
    val provider: IntentProcessingMethod = IntentProcessingMethod.RULE_BASED,
    val status: IntentStatus = IntentStatus.REGISTERED,
    val version: String = "1.0",
    val examples: List<String> = emptyList(),
    val patterns: List<String> = emptyList()
) {
    enum class IntentStatus {
        REGISTERED,
        ACTIVE,
        DEPRECATED,
        EXPERIMENTAL
    }
}
