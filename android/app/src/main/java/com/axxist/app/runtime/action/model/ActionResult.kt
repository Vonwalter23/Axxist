package com.axxist.app.runtime.action.model

/**
 * Result of action execution.
 */
data class ActionResult(
    val requestId: String,
    val actionId: String,
    val success: Boolean,
    val state: ActionState,
    val data: Map<String, Any> = emptyMap(),
    val error: ActionError? = null,
    val executionTimeMs: Long = 0,
    val retriesAttempted: Int = 0,
    val metadata: ActionMetadata = ActionMetadata(),
    val timestamp: Long = System.currentTimeMillis()
) {
    val isSuccessful: Boolean get() = success && state == ActionState.COMPLETED
    val isFailed: Boolean get() = !success || state == ActionState.FAILED
    val isRetryable: Boolean get() = error?.isRetryable == true && retriesAttempted < 3
    
    companion object {
        fun success(
            requestId: String,
            actionId: String,
            data: Map<String, Any> = emptyMap(),
            executionTimeMs: Long = 0,
            metadata: ActionMetadata = ActionMetadata()
        ) = ActionResult(
            requestId = requestId,
            actionId = actionId,
            success = true,
            state = ActionState.COMPLETED,
            data = data,
            executionTimeMs = executionTimeMs,
            metadata = metadata
        )
        
        fun failure(
            requestId: String,
            actionId: String,
            error: ActionError,
            executionTimeMs: Long = 0,
            retriesAttempted: Int = 0,
            metadata: ActionMetadata = ActionMetadata()
        ) = ActionResult(
            requestId = requestId,
            actionId = actionId,
            success = false,
            state = if (error.code == "PERMISSION_DENIED") ActionState.ERROR else ActionState.FAILED,
            error = error,
            executionTimeMs = executionTimeMs,
            retriesAttempted = retriesAttempted,
            metadata = metadata
        )
    }
}
