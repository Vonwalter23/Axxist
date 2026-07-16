package com.axxist.app.runtime.conversation.provider

import com.axxist.app.runtime.conversation.model.Message

/**
 * AIProvider - Contract for AI processing providers.
 * 
 * This interface defines the contract for future AI implementations.
 * Implementations can include:
 * - Groq API
 * - OpenAI
 * - Gemini
 * - Local LLM
 * - Custom AI services
 */
interface AIProvider {
    
    /**
     * AI provider type.
     */
    enum class ProviderType {
        GROQ,
        OPENAI,
        GEMINI,
        ANTHROPIC,
        LOCAL,
        CUSTOM,
        NONE
    }
    
    /**
     * AI response result.
     */
    data class AIResponse(
        val content: String,
        val finishReason: FinishReason = FinishReason.STOP,
        val usage: Usage? = null,
        val model: String = "",
        val error: String? = null
    )
    
    enum class FinishReason {
        STOP,
        LENGTH,
        CONTENT_FILTER,
        TOOL_CALLS,
        ERROR
    }
    
    data class Usage(
        val promptTokens: Int = 0,
        val completionTokens: Int = 0,
        val totalTokens: Int = 0
    )
    
    /**
     * AI configuration.
     */
    data class AIConfig(
        val model: String = "default",
        val temperature: Float = 0.7f,
        val maxTokens: Int = 1000,
        val systemPrompt: String = ""
    )
    
    /**
     * Initialize the AI provider.
     */
    suspend fun initialize(config: AIConfig): Boolean
    
    /**
     * Send a chat completion request.
     */
    suspend fun chatCompletion(messages: List<Message>): AIResponse
    
    /**
     * Send a simple text completion request.
     */
    suspend fun textCompletion(prompt: String): AIResponse
    
    /**
     * Check if provider is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Get provider type.
     */
    fun getProviderType(): ProviderType
    
    /**
     * Get supported models.
     */
    fun getSupportedModels(): List<String>
    
    /**
     * Test connection.
     */
    suspend fun testConnection(): Boolean
    
    /**
     * Release resources.
     */
    suspend fun release()
}
