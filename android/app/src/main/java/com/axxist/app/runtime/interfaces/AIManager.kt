package com.axxist.app.runtime.interfaces

/**
 * AIManager Interface - Prepared for future AI capabilities.
 * 
 * This interface defines the contract for AI processing.
 * Implementation will be added in STAGE_06 (AI Router).
 */
interface AIManager {
    
    /**
     * AI Provider type.
     */
    enum class AIProvider {
        GROQ,
        LOCAL,
        CUSTOM
    }
    
    /**
     * Initialize the AI system.
     */
    suspend fun initialize(): Boolean
    
    /**
     * Process a user query and return response.
     */
    suspend fun processQuery(
        query: String,
        context: Map<String, Any> = emptyMap()
    ): AIResponse
    
    /**
     * Set the active AI provider.
     */
    suspend fun setProvider(provider: AIProvider): Boolean
    
    /**
     * Get the current AI provider.
     */
    fun getCurrentProvider(): AIProvider
    
    /**
     * Check if AI is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Check if provider is configured.
     */
    fun isProviderConfigured(provider: AIProvider): Boolean
    
    /**
     * Configure API key for provider.
     */
    suspend fun configureProvider(provider: AIProvider, apiKey: String): Boolean
    
    /**
     * Test connection to current provider.
     */
    suspend fun testConnection(): Boolean
    
    /**
     * Release resources.
     */
    suspend fun release()
    
    /**
     * AI Response data class.
     */
    data class AIResponse(
        val text: String,
        val provider: AIProvider,
        val confidence: Float = 1.0f,
        val metadata: Map<String, Any> = emptyMap(),
        val error: String? = null
    )
}
