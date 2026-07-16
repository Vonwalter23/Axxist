package com.axxist.app.runtime.state

/**
 * Runtime State - Represents the possible states of the Axxist Runtime.
 */
enum class RuntimeState {
    STOPPED,
    STARTING,
    RUNNING,
    PAUSED,
    STOPPING,
    ERROR;
    
    fun isActive(): Boolean = this == RUNNING || this == PAUSED
    
    fun canTransitionTo(target: RuntimeState): Boolean {
        return when (this) {
            STOPPED -> target == STARTING
            STARTING -> target == RUNNING || target == ERROR || target == STOPPING
            RUNNING -> target == PAUSED || target == STOPPING || target == ERROR
            PAUSED -> target == RUNNING || target == STOPPING
            STOPPING -> target == STOPPED
            ERROR -> target == STOPPING || target == STARTING
        }
    }
}
