package com.axxist.app.runtime.action.executor

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.Action
import com.axxist.app.runtime.action.model.ActionError
import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionResult
import com.axxist.app.runtime.action.model.ActionState
import com.axxist.app.runtime.action.provider.ActionExecutorProvider

/**
 * Executor for actions.
 * Infrastructure only - no real execution.
 */
class ActionExecutor {
    
    companion object {
        private const val TAG = "ActionExecutor"
    }
    
    private val executors = mutableMapOf<String, ActionExecutorProvider>()
    
    /**
     * Register an executor provider.
     */
    fun registerExecutor(provider: ActionExecutorProvider) {
        for (type in provider.getSupportedTypes()) {
            executors[type] = provider
            Logger.d(TAG, "Registered executor for type: $type")
        }
    }
    
    /**
     * Unregister an executor.
     */
    fun unregisterExecutor(name: String) {
        executors.entries.removeIf { it.value.getName() == name }
    }
    
    /**
     * Execute an action.
     * Returns a placeholder result since no real execution is performed.
     */
    suspend fun execute(action: Action): ActionResult {
        val startTime = System.currentTimeMillis()
        
        Logger.d(TAG, "Executing action: ${action.id}")
        
        // Infrastructure placeholder - no real execution
        return ActionResult.failure(
            requestId = action.request.actionId,
            actionId = action.id,
            error = ActionError(
                code = "INFRASTRUCTURE_ONLY",
                message = "Action framework infrastructure only - no real execution implemented",
                isRetryable = false
            ),
            executionTimeMs = System.currentTimeMillis() - startTime
        )
    }
    
    /**
     * Validate an action request.
     */
    suspend fun validate(request: ActionRequest, executorType: String? = null): ActionExecutorProvider.ValidationResult {
        if (executorType != null) {
            val executor = executors[executorType]
            if (executor != null) {
                return executor.validate(request)
            }
        }
        
        // Check all executors
        for (executor in executors.values) {
            if (executor.canExecute(request.actionId)) {
                return executor.validate(request)
            }
        }
        
        return ActionExecutorProvider.ValidationResult(
            isValid = false,
            errors = listOf("No executor available for action: ${request.actionId}")
        )
    }
    
    /**
     * Get executor for action type.
     */
    fun getExecutor(actionId: String): ActionExecutorProvider? {
        return executors.values.find { it.canExecute(actionId) }
    }
    
    /**
     * Get all registered executors.
     */
    fun getRegisteredExecutors(): List<String> {
        return executors.values.map { it.getName() }.distinct()
    }
    
    /**
     * Clear all executors.
     */
    fun clear() {
        executors.clear()
        Logger.d(TAG, "All executors cleared")
    }
}
