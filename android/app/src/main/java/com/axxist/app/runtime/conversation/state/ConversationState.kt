package com.axxist.app.runtime.conversation.state

/**
 * Conversation State - Represents the possible states of the conversation system.
 */
enum class ConversationState {
    IDLE,
    LISTENING,
    PROCESSING,
    RESPONDING,
    ENDED,
    ERROR;
    
    fun isActive(): Boolean = this == LISTENING || this == PROCESSING || this == RESPONDING
    
    fun canTransitionTo(target: ConversationState): Boolean {
        return when (this) {
            IDLE -> target == LISTENING
            LISTENING -> target == PROCESSING || target == ENDED || target == ERROR
            PROCESSING -> target == RESPONDING || target == ENDED || target == ERROR
            RESPONDING -> target == LISTENING || target == ENDED || target == ERROR
            ENDED -> target == IDLE
            ERROR -> target == IDLE || target == LISTENING
        }
    }
}
