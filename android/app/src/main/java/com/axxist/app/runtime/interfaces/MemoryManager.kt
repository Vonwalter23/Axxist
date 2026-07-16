package com.axxist.app.runtime.interfaces

/**
 * MemoryManager Interface - Prepared for future memory capabilities.
 * 
 * This interface defines the contract for conversation memory and context.
 * Implementation will be added in STAGE_15 (Memory Engine).
 */
interface MemoryManager {
    
    /**
     * Memory type.
     */
    enum class MemoryType {
        SHORT_TERM,
        LONG_TERM,
        EPISODIC,
        SEMANTIC
    }
    
    /**
     * Initialize the memory system.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Store a memory entry.
     */
    suspend fun store(entry: MemoryEntry): Boolean
    
    /**
     * Retrieve memories.
     */
    suspend fun retrieve(query: String, limit: Int = 10): List<MemoryEntry>
    
    /**
     * Get memories by type.
     */
    suspend fun getByType(type: MemoryType): List<MemoryEntry>
    
    /**
     * Get recent memories.
     */
    suspend fun getRecent(limit: Int = 10): List<MemoryEntry>
    
    /**
     * Delete a memory entry.
     */
    suspend fun delete(memoryId: String): Boolean
    
    /**
     * Clear all memories.
     */
    suspend fun clear(): Boolean
    
    /**
     * Get context for AI processing.
     */
    suspend fun getContext(maxTokens: Int = 4000): String
    
    /**
     * Add conversation to memory.
     */
    suspend fun addConversation(
        role: String,
        content: String,
        metadata: Map<String, Any> = emptyMap()
    ): Boolean
    
    /**
     * Get conversation history.
     */
    suspend fun getConversationHistory(limit: Int = 20): List<MemoryEntry>
    
    /**
     * Check if memory is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * Memory entry data class.
     */
    data class MemoryEntry(
        val id: String,
        val content: String,
        val type: MemoryType,
        val timestamp: Long = System.currentTimeMillis(),
        val importance: Float = 0.5f,
        val metadata: Map<String, Any> = emptyMap()
    )
}
