package com.axxist.app.runtime.action.model

/**
 * States for the Action Framework.
 */
enum class ActionState {
    IDLE,        // Ready for execution
    QUEUED,      // Action queued for execution
    VALIDATING,  // Validating action
    EXECUTING,   // Executing action
    COMPLETED,   // Action completed successfully
    FAILED,      // Action failed
    CANCELLED,   // Action cancelled
    ERROR;       // Error state
    
    fun isActive(): Boolean = this == QUEUED || this == VALIDATING || this == EXECUTING
    
    fun isTerminal(): Boolean = this == COMPLETED || this == FAILED || this == CANCELLED || this == ERROR
    
    fun canTransitionTo(target: ActionState): Boolean {
        return when (this) {
            IDLE -> target == QUEUED || target == VALIDATING
            QUEUED -> target == VALIDATING || target == CANCELLED || target == ERROR
            VALIDATING -> target == EXECUTING || target == FAILED || target == CANCELLED || target == ERROR
            EXECUTING -> target == COMPLETED || target == FAILED || target == CANCELLED || target == ERROR
            COMPLETED -> false // Terminal state
            FAILED -> target == IDLE || target == QUEUED // Can retry
            CANCELLED -> target == IDLE // Can restart
            ERROR -> target == IDLE || target == QUEUED // Can retry
        }
    }
}
