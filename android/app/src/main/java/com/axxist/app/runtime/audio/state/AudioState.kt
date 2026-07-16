package com.axxist.app.runtime.audio.state

/**
 * Audio State - Represents the possible states of the audio system.
 */
enum class AudioState {
    IDLE,
    INITIALIZING,
    LISTENING,
    PROCESSING,
    STOPPING,
    ERROR;
    
    fun isActive(): Boolean = this == LISTENING || this == PROCESSING
    
    fun canTransitionTo(target: AudioState): Boolean {
        return when (this) {
            IDLE -> target == INITIALIZING
            INITIALIZING -> target == LISTENING || target == ERROR || target == IDLE
            LISTENING -> target == PROCESSING || target == STOPPING || target == ERROR
            PROCESSING -> target == LISTENING || target == STOPPING || target == ERROR
            STOPPING -> target == IDLE || target == ERROR
            ERROR -> target == IDLE || target == INITIALIZING
        }
    }
}
