package com.axxist.app.runtime.wakeword.state

/**
 * WakeWord State - Represents the possible states of the wake word detection system.
 */
enum class WakeWordState {
    DISABLED,
    INITIALIZING,
    LISTENING,
    DETECTED,
    ERROR;
    
    fun isActive(): Boolean = this == LISTENING
    
    fun canTransitionTo(target: WakeWordState): Boolean {
        return when (this) {
            DISABLED -> target == INITIALIZING
            INITIALIZING -> target == LISTENING || target == ERROR || target == DISABLED
            LISTENING -> target == DETECTED || target == DISABLED || target == ERROR
            DETECTED -> target == LISTENING || target == DISABLED
            ERROR -> target == DISABLED || target == INITIALIZING
        }
    }
}
