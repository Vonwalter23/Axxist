package com.axxist.app.runtime.intent.validator

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.EntityType
import com.axxist.app.runtime.intent.model.Intent
import com.axxist.app.runtime.intent.model.IntentDefinition
import com.axxist.app.runtime.intent.model.ValidationResult
import com.axxist.app.runtime.intent.registry.IntentRegistry

/**
 * Validator for intent entities and parameters.
 */
class IntentValidator(
    private val registry: IntentRegistry = IntentRegistry.getInstance()
) {
    
    companion object {
        private const val TAG = "IntentValidator"
        
        @Volatile
        private var instance: IntentValidator? = null
        
        fun getInstance(): IntentValidator {
            return instance ?: synchronized(this) {
                instance ?: IntentValidator().also { instance = it }
            }
        }
    }
    
    /**
     * Validate an intent against its definition.
     */
    fun validate(intent: Intent): ValidationResult {
        val definition = registry.getIntent(intent.id)
        
        if (definition == null) {
            Logger.w(TAG, "Intent definition not found: ${intent.id}")
            return ValidationResult.invalid(
                errors = listOf("Intent definition not found")
            )
        }
        
        return validateWithDefinition(intent, definition)
    }
    
    /**
     * Validate intent with explicit definition.
     */
    fun validateWithDefinition(intent: Intent, definition: IntentDefinition): ValidationResult {
        val missingEntities = mutableListOf<EntityType>()
        val invalidParameters = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val errors = mutableListOf<String>()
        
        // Check required entities
        for (requiredEntity in definition.requiredEntities) {
            val entity = intent.getEntity(requiredEntity)
            if (entity == null) {
                missingEntities.add(requiredEntity)
            } else if (!entity.isValid()) {
                invalidParameters.add("Entity '${requiredEntity.name}' has invalid value")
            }
        }
        
        // Check confidence threshold
        if (intent.confidence < MIN_CONFIDENCE_THRESHOLD) {
            warnings.add("Confidence ${intent.confidence} below minimum threshold $MIN_CONFIDENCE_THRESHOLD")
        }
        
        // Check entity confidence
        for (entity in intent.entities) {
            if (entity.confidence < ENTITY_CONFIDENCE_THRESHOLD) {
                warnings.add("Entity '${entity.type.name}' has low confidence: ${entity.confidence}")
            }
        }
        
        // Validate parameters
        for ((key, value) in intent.parameters) {
            if (value == null) {
                invalidParameters.add("Parameter '$key' is null")
            } else if (value is String && value.isBlank()) {
                invalidParameters.add("Parameter '$key' is empty")
            }
        }
        
        val isValid = missingEntities.isEmpty() && invalidParameters.isEmpty() && errors.isEmpty()
        
        return ValidationResult(
            isValid = isValid,
            missingEntities = missingEntities,
            invalidParameters = invalidParameters,
            warnings = warnings,
            errors = errors
        )
    }
    
    /**
     * Check if intent passes minimum requirements.
     */
    fun passesMinimumRequirements(intent: Intent): Boolean {
        val confidenceOk = intent.confidence >= MIN_CONFIDENCE_THRESHOLD
        val entitiesOk = intent.entities.isNotEmpty()
        
        return confidenceOk && entitiesOk
    }
    
    /**
     * Get validation summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "minConfidenceThreshold" to MIN_CONFIDENCE_THRESHOLD,
        "entityConfidenceThreshold" to ENTITY_CONFIDENCE_THRESHOLD
    )
    
    private companion object {
        const val MIN_CONFIDENCE_THRESHOLD = 0.5f
        const val ENTITY_CONFIDENCE_THRESHOLD = 0.3f
    }
}
