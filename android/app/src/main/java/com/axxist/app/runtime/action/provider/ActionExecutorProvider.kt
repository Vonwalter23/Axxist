package com.axxist.app.runtime.action.provider

import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionResult

/**
 * Interface for action executors.
 * Executors are responsible for running specific action types.
 */
interface ActionExecutorProvider {
    
    /**
     * Check if this executor can handle the action.
     */
    fun canExecute(actionId: String): Boolean
    
    /**
     * Execute the action synchronously or asynchronously.
     */
    suspend fun execute(request: ActionRequest): ActionResult
    
    /**
     * Validate that the action can be executed.
     */
    suspend fun validate(request: ActionRequest): ValidationResult
    
    /**
     * Get the executor name.
     */
    fun getName(): String
    
    /**
     * Get supported action types.
     */
    fun getSupportedTypes(): List<String>
    
    data class ValidationResult(
        val isValid: Boolean,
        val errors: List<String> = emptyList(),
        val warnings: List<String> = emptyList()
    )
}
