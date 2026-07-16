package com.axxist.app.runtime.conversation.provider

/**
 * ResponseGenerator - Contract for response generation.
 * 
 * This interface defines the contract for future response generation.
 * Implementations can include:
 * - AI-powered responses
 * - Template-based responses
 * - Canned responses
 * - Custom response handlers
 */
interface ResponseGenerator {
    
    /**
     * Generated response.
     */
    data class GeneratedResponse(
        val text: String,
        val confidence: Float = 1.0f,
        val metadata: Map<String, Any> = emptyMap(),
        val error: String? = null
    )
    
    /**
     * Response style options.
     */
    enum class ResponseStyle {
        NATURAL,
        CONCISE,
        DETAILED,
        FORMAL,
        CASUAL
    }
    
    /**
     * Initialize the response generator.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Generate a response.
     */
    suspend fun generate(context: String): GeneratedResponse
    
    /**
     * Generate with specific style.
     */
    suspend fun generate(context: String, style: ResponseStyle): GeneratedResponse
    
    /**
     * Check if generator is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Release resources.
     */
    suspend fun release()
}
