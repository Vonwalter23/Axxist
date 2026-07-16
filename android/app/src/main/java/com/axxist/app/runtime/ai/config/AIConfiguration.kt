package com.axxist.app.runtime.ai.config

import com.axxist.app.runtime.conversation.provider.AIProvider

/**
 * AI Configuration - Settings for AI providers and routing.
 */
data class AIConfiguration(
    val enabled: Boolean = true,
    val preferredProvider: AIProvider.ProviderType = AIProvider.ProviderType.GROQ,
    val fallbackProvider: AIProvider.ProviderType? = null,
    val timeoutMs: Long = 30000,
    val retryCount: Int = 2,
    val model: String = "default",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 1000,
    val systemPrompt: String = "You are Axxist, a helpful voice assistant. Respond concisely and helpfully.",
    val enableFallback: Boolean = true,
    val logRequests: Boolean = true
) {
    
    companion object {
        const val DEFAULT_TIMEOUT_MS = 30000L
        const val DEFAULT_RETRY_COUNT = 2
        
        const val DEFAULT_SYSTEM_PROMPT = "You are Axxist, a helpful voice assistant. Respond concisely and helpfully."
        
        fun default(): AIConfiguration = AIConfiguration()
    }
    
    fun isProviderAvailable(provider: AIProvider.ProviderType): Boolean {
        return provider == preferredProvider || provider == fallbackProvider
    }
    
    fun getTimeout(): Long = timeoutMs.coerceIn(5000, 120000)
    
    fun getValidatedRetryCount(): Int = retryCount.coerceIn(0, 5)
}
