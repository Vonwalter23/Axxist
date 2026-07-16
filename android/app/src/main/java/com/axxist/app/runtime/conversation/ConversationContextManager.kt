package com.axxist.app.runtime.conversation

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.conversation.model.ConversationSession
import com.axxist.app.runtime.conversation.model.Message
import com.axxist.app.runtime.conversation.model.MessageRole

/**
 * ConversationContextManager - Manages conversation context and temporary storage.
 * 
 * Responsibilities:
 * - Maintain temporary context
 * - Store messages from current session
 * - Clear context when needed
 */
class ConversationContextManager {
    
    companion object {
        private const val TAG = "ConversationContextManager"
        
        @Volatile
        private var instance: ConversationContextManager? = null
        
        fun getInstance(): ConversationContextManager {
            return instance ?: synchronized(this) {
                instance ?: ConversationContextManager().also { instance = it }
            }
        }
        
        const val MAX_CONTEXT_MESSAGES = 50
        const val MAX_CONTEXT_SIZE_CHARS = 32000
    }
    
    private var currentSession: ConversationSession? = null
    private val systemMessages = mutableListOf<Message>()
    
    /**
     * Start a new conversation session.
     */
    fun startSession(): ConversationSession {
        Logger.d(TAG, "Starting new conversation session")
        currentSession = ConversationSession.create()
        return currentSession!!
    }
    
    /**
     * Get current session.
     */
    fun getCurrentSession(): ConversationSession? = currentSession
    
    /**
     * Check if there's an active session.
     */
    fun hasActiveSession(): Boolean = currentSession?.isActive() == true
    
    /**
     * Add a message to the current session.
     */
    fun addMessage(message: Message) {
        currentSession?.addMessage(message)
        Logger.d(TAG, "Added message to session: ${message.role} - ${message.content.take(50)}...")
    }
    
    /**
     * Add a user message.
     */
    fun addUserMessage(content: String): Message {
        val message = Message.userMessage(content)
        addMessage(message)
        return message
    }
    
    /**
     * Add an assistant message.
     */
    fun addAssistantMessage(content: String): Message {
        val message = Message.assistantMessage(content)
        addMessage(message)
        return message
    }
    
    /**
     * Add a system message.
     */
    fun addSystemMessage(content: String): Message {
        val message = Message.systemMessage(content)
        systemMessages.add(message)
        return message
    }
    
    /**
     * Get all messages including system messages.
     */
    fun getAllMessages(): List<Message> {
        val sessionMessages = currentSession?.getAllMessages() ?: emptyList()
        return systemMessages + sessionMessages
    }
    
    /**
     * Get recent messages for context.
     */
    fun getRecentMessages(count: Int = 10): List<Message> {
        return getAllMessages().takeLast(count)
    }
    
    /**
     * Get context for AI processing.
     */
    fun getContextForAI(): String {
        val messages = getAllMessages()
        val contextBuilder = StringBuilder()
        
        for (message in messages) {
            val rolePrefix = when (message.role) {
                MessageRole.SYSTEM -> "[SYSTEM]"
                MessageRole.USER -> "[USER]"
                MessageRole.ASSISTANT -> "[ASSISTANT]"
            }
            contextBuilder.appendLine("$rolePrefix ${message.content}")
        }
        
        return contextBuilder.toString()
    }
    
    /**
     * Get context as message list.
     */
    fun getContextAsMessages(): List<Message> {
        return getAllMessages()
    }
    
    /**
     * Get message count.
     */
    fun getMessageCount(): Int = currentSession?.getMessageCount() ?: 0
    
    /**
     * End current session.
     */
    fun endSession() {
        currentSession?.end()
        Logger.d(TAG, "Session ended: ${currentSession?.id}")
    }
    
    /**
     * Clear all context.
     */
    fun clearContext() {
        Logger.d(TAG, "Clearing conversation context")
        currentSession?.clearMessages()
        systemMessages.clear()
    }
    
    /**
     * Reset everything.
     */
    fun reset() {
        Logger.d(TAG, "Resetting ConversationContextManager")
        currentSession = null
        systemMessages.clear()
    }
    
    /**
     * Get summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "hasActiveSession" to hasActiveSession(),
        "sessionId" to (currentSession?.id ?: "none"),
        "messageCount" to getMessageCount(),
        "systemMessagesCount" to systemMessages.size,
        "sessionDuration" to (currentSession?.getDurationFormatted() ?: "00:00")
    )
}
