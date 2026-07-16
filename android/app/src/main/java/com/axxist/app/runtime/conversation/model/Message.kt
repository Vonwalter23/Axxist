package com.axxist.app.runtime.conversation.model

/**
 * Message role in a conversation.
 */
enum class MessageRole {
    SYSTEM,
    USER,
    ASSISTANT;
    
    companion object {
        fun fromString(value: String): MessageRole {
            return when (value.lowercase()) {
                "system" -> SYSTEM
                "user" -> USER
                "assistant" -> ASSISTANT
                else -> USER
            }
        }
    }
}

/**
 * Message content type.
 */
enum class MessageContentType {
    TEXT,
    AUDIO,
    IMAGE,
    TOOL_CALL,
    TOOL_RESULT
}

/**
 * Message in a conversation.
 */
data class Message(
    val id: String,
    val role: MessageRole,
    val content: String,
    val contentType: MessageContentType = MessageContentType.TEXT,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, Any> = emptyMap()
) {
    companion object {
        fun create(
            role: MessageRole,
            content: String,
            contentType: MessageContentType = MessageContentType.TEXT,
            metadata: Map<String, Any> = emptyMap()
        ): Message {
            return Message(
                id = generateId(),
                role = role,
                content = content,
                contentType = contentType,
                timestamp = System.currentTimeMillis(),
                metadata = metadata
            )
        }
        
        fun userMessage(content: String, metadata: Map<String, Any> = emptyMap()): Message {
            return create(MessageRole.USER, content, MessageContentType.TEXT, metadata)
        }
        
        fun assistantMessage(content: String, metadata: Map<String, Any> = emptyMap()): Message {
            return create(MessageRole.ASSISTANT, content, MessageContentType.TEXT, metadata)
        }
        
        fun systemMessage(content: String, metadata: Map<String, Any> = emptyMap()): Message {
            return create(MessageRole.SYSTEM, content, MessageContentType.TEXT, metadata)
        }
        
        private fun generateId(): String {
            return "msg_${System.currentTimeMillis()}_${(Math.random() * 10000).toInt()}"
        }
    }
}
