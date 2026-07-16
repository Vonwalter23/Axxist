package com.axxist.app.runtime.conversation.model

/**
 * Conversation session status.
 */
enum class SessionStatus {
    ACTIVE,
    ENDED,
    ERROR
}

/**
 * Conversation session - represents an active conversation.
 */
data class ConversationSession(
    val id: String,
    val startTime: Long = System.currentTimeMillis(),
    var endTime: Long? = null,
    var status: SessionStatus = SessionStatus.ACTIVE
) {
    
    private val _messages = mutableListOf<Message>()
    
    /**
     * Add a message to the session.
     */
    fun addMessage(message: Message) {
        _messages.add(message)
    }
    
    /**
     * Get all messages as a list.
     */
    fun getAllMessages(): List<Message> = _messages.toList()
    
    /**
     * Get user messages.
     */
    fun getUserMessages(): List<Message> = _messages.filter { it.role == MessageRole.USER }
    
    /**
     * Get assistant messages.
     */
    fun getAssistantMessages(): List<Message> = _messages.filter { it.role == MessageRole.ASSISTANT }
    
    /**
     * Get the last user message.
     */
    fun getLastUserMessage(): Message? = getUserMessages().lastOrNull()
    
    /**
     * Get the last assistant message.
     */
    fun getLastAssistantMessage(): Message? = getAssistantMessages().lastOrNull()
    
    /**
     * Get message count.
     */
    fun getMessageCount(): Int = _messages.size
    
    /**
     * Get session duration in milliseconds.
     */
    fun getDuration(): Long {
        val end = endTime ?: System.currentTimeMillis()
        return end - startTime
    }
    
    /**
     * Get session duration formatted.
     */
    fun getDurationFormatted(): String {
        val duration = getDuration()
        val seconds = (duration / 1000) % 60
        val minutes = (duration / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    
    /**
     * End the session.
     */
    fun end() {
        endTime = System.currentTimeMillis()
        status = SessionStatus.ENDED
    }
    
    /**
     * Mark session as error.
     */
    fun markError() {
        endTime = System.currentTimeMillis()
        status = SessionStatus.ERROR
    }
    
    /**
     * Check if session is active.
     */
    fun isActive(): Boolean = status == SessionStatus.ACTIVE
    
    /**
     * Clear all messages.
     */
    fun clearMessages() {
        _messages.clear()
    }
    
    companion object {
        /**
         * Create a new session.
         */
        fun create(): ConversationSession {
            return ConversationSession(
                id = generateSessionId(),
                startTime = System.currentTimeMillis()
            )
        }
        
        private fun generateSessionId(): String {
            return "session_${System.currentTimeMillis()}_${(Math.random() * 10000).toInt()}"
        }
    }
}
