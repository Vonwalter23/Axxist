package com.axxist.app.runtime.action.validator

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.ActionDefinition
import com.axxist.app.runtime.action.model.ActionError
import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionStatus

/**
 * Validator for action requests.
 */
class ActionValidator {
    
    companion object {
        private const val TAG = "ActionValidator"
    }
    
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<ActionError> = emptyList(),
        val warnings: List<String> = emptyList()
    )
    
    /**
     * Validate an action request.
     */
    fun validate(request: ActionRequest, definition: ActionDefinition): ValidationResult {
        val errors = mutableListOf<ActionError>()
        val warnings = mutableListOf<String>()
        
        // Check if action is active
        if (definition.status != ActionStatus.ACTIVE) {
            errors.add(ActionError.invalidAction(
                "Action ${definition.id} is not active (status: ${definition.status})"
            ))
        }
        
        // Check required entities are present
        for (entity in definition.requiredEntities) {
            if (!hasEntity(request, entity)) {
                warnings.add("Optional entity $entity not provided")
            }
        }
        
        // Check required parameters
        for (param in definition.requiredEntities) {
            if (!hasParameter(request, param)) {
                warnings.add("Parameter $param not provided")
            }
        }
        
        // Check timeout
        if (request.retryPolicy != null && request.retryPolicy.maxRetries > 0) {
            val totalTimeout = definition.timeout * (request.retryPolicy.maxRetries + 1)
            if (totalTimeout > 300000) { // 5 minutes max
                warnings.add("Total timeout may exceed recommended limit")
            }
        }
        
        val isValid = errors.isEmpty()
        
        if (isValid) {
            Logger.d(TAG, "Validation passed for action ${request.actionId}")
        } else {
            Logger.w(TAG, "Validation failed for action ${request.actionId}: ${errors.size} errors")
        }
        
        return ValidationResult(
            isValid = isValid,
            errors = errors,
            warnings = warnings
        )
    }
    
    /**
     * Validate action compatibility.
     */
    fun validateCompatibility(
        request: ActionRequest,
        definition: ActionDefinition,
        availablePermissions: List<String>
    ): ValidationResult {
        val errors = mutableListOf<ActionError>()
        
        // Check if all required permissions are available
        for (permission in definition.requiredPermissions) {
            if (permission !in availablePermissions) {
                errors.add(ActionError.permissionDenied(permission))
            }
        }
        
        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
    
    private fun hasEntity(request: ActionRequest, entityType: String): Boolean {
        return request.parameters.containsKey(entityType.lowercase()) ||
               request.parameters.containsKey(entityType)
    }
    
    private fun hasParameter(request: ActionRequest, param: String): Boolean {
        return request.parameters.containsKey(param.lowercase()) ||
               request.parameters.containsKey(param)
    }
}
