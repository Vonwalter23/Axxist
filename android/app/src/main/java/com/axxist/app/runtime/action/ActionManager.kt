package com.axxist.app.runtime.action

import android.content.Context
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.diagnostics.ActionDiagnosticsCollector
import com.axxist.app.runtime.action.executor.ActionExecutor
import com.axxist.app.runtime.action.model.Action
import com.axxist.app.runtime.action.model.ActionError
import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionResult
import com.axxist.app.runtime.action.model.ActionState
import com.axxist.app.runtime.action.permission.PermissionChecker
import com.axxist.app.runtime.action.registry.ActionRegistry
import com.axxist.app.runtime.action.retry.RetryManager
import com.axxist.app.runtime.action.router.ActionRouter
import com.axxist.app.runtime.action.state.ActionStateManager
import com.axxist.app.runtime.action.validator.ActionValidator
import java.util.UUID

/**
 * ActionManager - Main coordinator for the Action Framework.
 * 
 * Coordinates:
 * - ActionRegistry
 * - ActionValidator
 * - ActionRouter
 * - ActionExecutor
 * - PermissionChecker
 * - RetryManager
 * - Diagnostics
 * - StateManager
 */
class ActionManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "ActionManager"
        
        @Volatile
        private var instance: ActionManager? = null
        
        fun initialize(context: Context): ActionManager {
            return instance ?: synchronized(this) {
                instance ?: ActionManager(context.applicationContext).also { 
                    instance = it
                    it.initialize()
                }
            }
        }
        
        fun getInstance(): ActionManager {
            return instance ?: throw IllegalStateException("ActionManager not initialized")
        }
    }
    
    private val registry = ActionRegistry()
    private val validator = ActionValidator()
    private val router = ActionRouter()
    private val executor = ActionExecutor()
    private val permissionChecker = PermissionChecker(context)
    private val retryManager = RetryManager()
    private val diagnostics = ActionDiagnosticsCollector()
    private val stateManager = ActionStateManager()
    
    private val activeActions = mutableMapOf<String, Action>()
    private var isInitialized = false
    
    /**
     * Initialize the Action Framework.
     */
    fun initialize() {
        if (isInitialized) {
            Logger.w(TAG, "Already initialized")
            return
        }
        
        Logger.d(TAG, "Initializing Action Framework...")
        
        // Initialize registry with base actions
        registry.initialize()
        
        isInitialized = true
        Logger.i(TAG, "Action Framework initialized with ${registry.count()} actions")
    }
    
    /**
     * Check if framework is ready.
     */
    fun isReady(): Boolean = isInitialized
    
    /**
     * Get current state.
     */
    fun getState(): ActionFrameworkState {
        return ActionFrameworkState(
            isReady = isInitialized,
            registeredActions = registry.count(),
            activeActions = activeActions.size,
            diagnostics = diagnostics.getSummary()
        )
    }
    
    /**
     * Register a new action.
     */
    fun registerAction(definition: com.axxist.app.runtime.action.model.ActionDefinition): Boolean {
        return registry.register(definition)
    }
    
    /**
     * Unregister an action.
     */
    fun unregisterAction(actionId: String): Boolean {
        return registry.unregister(actionId)
    }
    
    /**
     * Get an action definition.
     */
    fun getActionDefinition(actionId: String): com.axxist.app.runtime.action.model.ActionDefinition? {
        return registry.get(actionId)
    }
    
    /**
     * Get all registered actions.
     */
    fun getAllActions(): List<com.axxist.app.runtime.action.model.ActionDefinition> {
        return registry.getAll()
    }
    
    /**
     * Execute an action.
     * Returns a placeholder result (infrastructure only).
     */
    suspend fun executeAction(request: ActionRequest): ActionResult {
        val requestId = request.actionId + "_" + UUID.randomUUID().toString().take(8)
        
        Logger.d(TAG, "Executing action: ${request.actionId}")
        
        // Get definition
        val definition = registry.get(request.actionId)
        if (definition == null) {
            Logger.e(TAG, "Action not found: ${request.actionId}")
            return ActionResult.failure(
                requestId = requestId,
                actionId = request.actionId,
                error = ActionError.invalidAction("Action not found: ${request.actionId}")
            )
        }
        
        // Create action instance
        val action = Action(
            id = definition.id,
            definition = definition,
            request = request
        )
        
        // Start diagnostics
        val diag = diagnostics.startTracking(action.id, requestId)
        
        // Validate
        val validationResult = validator.validate(request, definition)
        if (!validationResult.isValid) {
            diagnostics.updateDiagnostics(
                requestId = requestId,
                state = ActionState.ERROR,
                error = ActionError.validationFailed(
                    "Validation failed: ${validationResult.errors.joinToString { it.message }}"
                )
            )
            
            return ActionResult.failure(
                requestId = requestId,
                actionId = request.actionId,
                error = ActionError.validationFailed(
                    "Validation failed",
                    mapOf("errors" to validationResult.errors.map { it.message })
                )
            )
        }
        
        // Check permissions
        val missingPermissions = permissionChecker.getMissingPermissions(definition.requiredPermissions)
        if (missingPermissions.isNotEmpty()) {
            diagnostics.updateDiagnostics(
                requestId = requestId,
                state = ActionState.ERROR
            )
            
            return ActionResult.failure(
                requestId = requestId,
                actionId = request.actionId,
                error = ActionError.permissionDenied(missingPermissions.first())
            )
        }
        
        // Select execution plan
        val executionPlan = router.selectExecutionType(definition)
        Logger.d(TAG, "Execution plan: ${executionPlan.description}")
        
        // Track active action
        activeActions[requestId] = action
        
        // Execute (infrastructure only)
        val result = executor.execute(action)
        
        // Update diagnostics
        diagnostics.updateDiagnostics(
            requestId = requestId,
            state = result.state,
            error = result.error
        )
        
        // Remove from active
        activeActions.remove(requestId)
        
        return result
    }
    
    /**
     * Validate an action request without executing.
     */
    fun validateAction(request: ActionRequest): ActionValidator.ValidationResult {
        val definition = registry.get(request.actionId)
            ?: return ActionValidator.ValidationResult(
                isValid = false,
                errors = listOf(ActionError.invalidAction("Action not found"))
            )
        
        return validator.validate(request, definition)
    }
    
    /**
     * Check if an action is registered.
     */
    fun isActionRegistered(actionId: String): Boolean {
        return registry.isRegistered(actionId)
    }
    
    /**
     * Get active actions count.
     */
    fun getActiveActionsCount(): Int = activeActions.size
    
    /**
     * Get diagnostics summary.
     */
    fun getDiagnosticsSummary(): Map<String, Any> = diagnostics.getSummary()
    
    /**
     * Clear diagnostics.
     */
    fun clearDiagnostics() = diagnostics.clearAll()
    
    /**
     * Reset the framework.
     */
    fun reset() {
        Logger.d(TAG, "Resetting Action Framework...")
        
        activeActions.clear()
        diagnostics.clearAll()
        stateManager.clear()
        executor.clear()
        
        isInitialized = false
        Logger.d(TAG, "Action Framework reset complete")
    }
    
    data class ActionFrameworkState(
        val isReady: Boolean,
        val registeredActions: Int,
        val activeActions: Int,
        val diagnostics: Map<String, Any>
    )
}
