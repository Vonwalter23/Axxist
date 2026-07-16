package com.axxist.app.runtime.action.model

/**
 * Error information for failed actions.
 */
data class ActionError(
    val code: String,
    val message: String,
    val details: Map<String, Any> = emptyMap(),
    val isRetryable: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun invalidAction(message: String) = ActionError(
            code = "INVALID_ACTION",
            message = message
        )
        
        fun permissionDenied(permission: String) = ActionError(
            code = "PERMISSION_DENIED",
            message = "Permission denied: $permission",
            details = mapOf("permission" to permission)
        )
        
        fun validationFailed(message: String, details: Map<String, Any> = emptyMap()) = ActionError(
            code = "VALIDATION_FAILED",
            message = message,
            details = details
        )
        
        fun executionFailed(message: String, retryable: Boolean = false) = ActionError(
            code = "EXECUTION_FAILED",
            message = message,
            isRetryable = retryable
        )
        
        fun timeout(message: String) = ActionError(
            code = "TIMEOUT",
            message = message,
            isRetryable = true
        )
        
        fun unknown(message: String) = ActionError(
            code = "UNKNOWN",
            message = message,
            isRetryable = true
        )
    }
}
