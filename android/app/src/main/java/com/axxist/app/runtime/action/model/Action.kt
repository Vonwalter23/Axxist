package com.axxist.app.runtime.action.model

/**
 * Executable action instance.
 */
data class Action(
    val id: String,
    val definition: ActionDefinition,
    val request: ActionRequest,
    val state: ActionState = ActionState.IDLE,
    val retryCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun canExecute(): Boolean {
        return state == ActionState.IDLE || 
               state == ActionState.FAILED ||
               state == ActionState.ERROR
    }
    
    fun canRetry(): Boolean {
        return (state == ActionState.FAILED || state == ActionState.ERROR) &&
               retryCount < definition.retryPolicy.maxRetries
    }
    
    fun withState(newState: ActionState): Action {
        return copy(state = newState, updatedAt = System.currentTimeMillis())
    }
    
    fun incrementRetry(): Action {
        return copy(retryCount = retryCount + 1, updatedAt = System.currentTimeMillis())
    }
}
