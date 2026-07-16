package com.axxist.app.runtime.action.diagnostics

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.ActionError
import com.axxist.app.runtime.action.model.ActionState

/**
 * Collector for action diagnostics.
 */
class ActionDiagnosticsCollector {
    
    companion object {
        private const val TAG = "ActionDiagnostics"
    }
    
    data class Diagnostics(
        val actionId: String,
        val requestId: String,
        val startTime: Long,
        val endTime: Long = 0,
        val executionTimeMs: Long = 0,
        val state: ActionState = ActionState.IDLE,
        val errors: List<ActionError> = emptyList(),
        val warnings: List<String> = emptyList(),
        val executorName: String? = null,
        val retryCount: Int = 0
    ) {
        val hasErrors: Boolean get() = errors.isNotEmpty()
        val hasWarnings: Boolean get() = warnings.isNotEmpty()
        val isSuccess: Boolean get() = state == ActionState.COMPLETED && errors.isEmpty()
    }
    
    private val diagnostics = mutableMapOf<String, Diagnostics>()
    
    /**
     * Start tracking an action.
     */
    fun startTracking(actionId: String, requestId: String): Diagnostics {
        val diag = Diagnostics(
            actionId = actionId,
            requestId = requestId,
            startTime = System.currentTimeMillis()
        )
        diagnostics[requestId] = diag
        Logger.d(TAG, "Started tracking: $actionId ($requestId)")
        return diag
    }
    
    /**
     * Update diagnostics.
     */
    fun updateDiagnostics(
        requestId: String,
        state: ActionState? = null,
        error: ActionError? = null,
        warning: String? = null,
        executorName: String? = null,
        retryCount: Int? = null
    ) {
        val current = diagnostics[requestId] ?: return
        
        val updated = current.copy(
            endTime = if (state?.isTerminal() == true) System.currentTimeMillis() else current.endTime,
            executionTimeMs = if (current.endTime > 0) current.endTime - current.startTime else 0,
            state = state ?: current.state,
            errors = if (error != null) current.errors + error else current.errors,
            warnings = if (warning != null) current.warnings + warning else current.warnings,
            executorName = executorName ?: current.executorName,
            retryCount = retryCount ?: current.retryCount
        )
        
        diagnostics[requestId] = updated
    }
    
    /**
     * Get diagnostics for a request.
     */
    fun getDiagnostics(requestId: String): Diagnostics? {
        return diagnostics[requestId]
    }
    
    /**
     * Get all diagnostics.
     */
    fun getAllDiagnostics(): List<Diagnostics> {
        return diagnostics.values.toList()
    }
    
    /**
     * Clear diagnostics for a request.
     */
    fun clear(requestId: String) {
        diagnostics.remove(requestId)
    }
    
    /**
     * Clear all diagnostics.
     */
    fun clearAll() {
        diagnostics.clear()
        Logger.d(TAG, "All diagnostics cleared")
    }
    
    /**
     * Get summary statistics.
     */
    fun getSummary(): Map<String, Any> {
        val all = diagnostics.values.toList()
        return mapOf(
            "totalActions" to all.size,
            "successfulActions" to all.count { it.isSuccess },
            "failedActions" to all.count { it.hasErrors },
            "warningsCount" to all.sumOf { it.warnings.size },
            "averageExecutionTime" to if (all.isNotEmpty()) 
                all.filter { it.executionTimeMs > 0 }.map { it.executionTimeMs }.average() 
                else 0.0,
            "maxExecutionTime" to (all.maxOfOrNull { it.executionTimeMs } ?: 0),
            "minExecutionTime" to (all.minOfOrNull { it.executionTimeMs } ?: 0)
        )
    }
}
