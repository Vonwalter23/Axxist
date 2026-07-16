package com.axxist.app.runtime.intent.model

/**
 * Result of intent validation.
 */
data class ValidationResult(
    val isValid: Boolean,
    val missingEntities: List<EntityType> = emptyList(),
    val invalidParameters: List<String> = emptyList(),
    val warnings: List<String> = emptyList(),
    val errors: List<String> = emptyList()
) {
    companion object {
        fun valid(): ValidationResult = ValidationResult(isValid = true)
        
        fun invalid(
            missingEntities: List<EntityType> = emptyList(),
            errors: List<String> = emptyList()
        ): ValidationResult = ValidationResult(
            isValid = false,
            missingEntities = missingEntities,
            errors = errors
        )
    }
}
