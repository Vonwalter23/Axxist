package com.axxist.app.runtime.action.model

/**
 * Status for action definitions.
 */
enum class ActionStatus {
    REGISTERED,    // Registered in system
    ACTIVE,        // Can be executed
    DEPRECATED,    // Should not be used
    EXPERIMENTAL,  // Under development
    DISABLED       // Temporarily disabled
}
