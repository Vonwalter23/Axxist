package com.axxist.app.runtime.action.model

/**
 * Execution types for actions.
 */
enum class ActionType {
    ANDROID,    // Android system action
    LOCAL,      // Local execution
    EXTERNAL,   // External API/service
    HYBRID      // Combination of types
}
