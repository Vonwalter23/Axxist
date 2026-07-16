package com.axxist.app.runtime.intent.state

/**
 * States for the Intent Framework.
 */
enum class IntentState {
    IDLE,        // Ready for new input
    ANALYZING,   // Analyzing input
    MATCHED,     // Intent matched
    VALIDATING,  // Validating intent
    READY,       // Ready for execution
    FAILED,      // Processing failed
    ERROR;       // Error state
    
    fun isActive(): Boolean = this == ANALYZING || this == VALIDATING
    
    fun isReady(): Boolean = this == READY || this == MATCHED
    
    fun canTransitionTo(target: IntentState): Boolean {
        return when (this) {
            IDLE -> target == ANALYZING
            ANALYZING -> target == MATCHED || target == FAILED || target == ERROR
            MATCHED -> target == VALIDATING || target == FAILED || target == ERROR
            VALIDATING -> target == READY || target == FAILED || target == ERROR
            READY -> target == IDLE
            FAILED -> target == IDLE
            ERROR -> target == IDLE
        }
    }
}
