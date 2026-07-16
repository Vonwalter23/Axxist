package com.axxist.app.runtime.ai

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.AIEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.ai.config.AIConfiguration
import com.axxist.app.runtime.ai.provider.GroqProvider
import com.axxist.app.runtime.ai.provider.GeminiProvider
import com.axxist.app.runtime.ai.provider.OpenAIProvider
import com.axxist.app.runtime.ai.provider.LocalAIProvider
import com.axxist.app.runtime.ai.state.AIStateManager
import com.axxist.app.runtime.conversation.model.Message
import com.axxist.app.runtime.conversation.provider.AIProvider

/**
 * AIRouter - Routes AI requests to appropriate providers.
 * 
 * Responsibilities:
 * - Select the appropriate provider based on configuration
 * - Handle fallback to secondary providers
 * - Control provider availability
 * - Track provider performance
 */
class AIRouter private constructor() {
    
    companion object {
        private const val TAG = "AIRouter"
        
        @Volatile
        private var instance: AIRouter? = null
        
        fun getInstance(): AIRouter {
            return instance ?: synchronized(this) {
                instance ?: AIRouter().also { instance = it }
            }
        }
    }
    
    private val stateManager = AIStateManager.getInstance()
    
    private val providers = mutableMapOf<AIProvider.ProviderType, AIProvider>()
    private var activeProvider: AIProvider? = null
    private var currentConfig: AIConfiguration? = null
    
    init {
        // Initialize all providers
        registerProvider(GroqProvider())
        registerProvider(GeminiProvider())
        registerProvider(OpenAIProvider())
        registerProvider(LocalAIProvider())
    }
    
    /**
     * Initialize the router with configuration.
     */
    fun initialize(config: AIConfiguration): Boolean {
        Logger.d(TAG, "Initializing AIRouter...")
        
        currentConfig = config
        
        // Initialize all providers
        val aiConfig = AIProvider.AIConfig(
            model = config.model,
            temperature = config.temperature,
            maxTokens = config.maxTokens,
            systemPrompt = config.systemPrompt
        )
        
        var initializedCount = 0
        providers.forEach { (type, provider) ->
            try {
                kotlinx.coroutines.runBlocking {
                    if (provider.initialize(aiConfig)) {
                        stateManager.updateProviderStatus(type, true, true)
                        initializedCount++
                    } else {
                        stateManager.updateProviderStatus(type, false, true)
                    }
                }
            } catch (e: Exception) {
                Logger.e(TAG, "Failed to initialize provider: $type", e)
                stateManager.updateProviderStatus(type, false, true)
            }
        }
        
        Logger.d(TAG, "AIRouter initialized with $initializedCount providers")
        return initializedCount > 0
    }
    
    /**
     * Register a provider.
     */
    fun registerProvider(provider: AIProvider) {
        Logger.d(TAG, "Registering provider: ${provider.getProviderType()}")
        providers[provider.getProviderType()] = provider
    }
    
    /**
     * Unregister a provider.
     */
    fun unregisterProvider(type: AIProvider.ProviderType) {
        Logger.d(TAG, "Unregistering provider: $type")
        providers.remove(type)
    }
    
    /**
     * Get a provider by type.
     */
    fun getProvider(type: AIProvider.ProviderType): AIProvider? {
        return providers[type]
    }
    
    /**
     * Get all registered providers.
     */
    fun getAllProviders(): Map<AIProvider.ProviderType, AIProvider> {
        return providers.toMap()
    }
    
    /**
     * Select the best available provider based on configuration.
     */
    fun selectProvider(): AIProvider? {
        val config = currentConfig ?: return null
        
        if (!config.enabled) {
            Logger.w(TAG, "AI is disabled in configuration")
            return null
        }
        
        // Try preferred provider first
        val preferred = providers[config.preferredProvider]
        if (preferred != null && preferred.isAvailable()) {
            Logger.d(TAG, "Selected preferred provider: ${config.preferredProvider}")
            return preferred
        }
        
        // Try fallback provider
        config.fallbackProvider?.let { fallbackType ->
            val fallback = providers[fallbackType]
            if (fallback != null && fallback.isAvailable()) {
                Logger.d(TAG, "Selected fallback provider: $fallbackType")
                return fallback
            }
        }
        
        // Try any available provider
        val available = providers.values.firstOrNull { it.isAvailable() }
        if (available != null) {
            Logger.d(TAG, "Selected available provider: ${available.getProviderType()}")
            return available
        }
        
        Logger.e(TAG, "No available AI provider found")
        return null
    }
    
    /**
     * Send a request to AI and get response.
     */
    suspend fun sendRequest(messages: List<Message>): AIRouterResponse {
        val provider = selectProvider() ?: run {
            return AIRouterResponse(
                success = false,
                error = "No available AI provider"
            )
        }
        
        val providerType = provider.getProviderType()
        activeProvider = provider
        
        // Emit provider selected event
        EventBusManager.emitAIEvent(AIEvent.ProviderSelected(providerType.name))
        
        stateManager.recordProviderUsage(providerType)
        
        Logger.d(TAG, "Sending request to ${providerType.name}...")
        
        try {
            val response = provider.chatCompletion(messages)
            
            if (response.error != null) {
                stateManager.recordProviderError(providerType, response.error)
                
                // Try fallback if enabled
                if (currentConfig?.enableFallback == true) {
                    Logger.d(TAG, "Provider failed, trying fallback...")
                    return tryFallback(messages)
                }
                
                return AIRouterResponse(
                    success = false,
                    error = response.error,
                    providerUsed = providerType
                )
            }
            
            return AIRouterResponse(
                success = true,
                content = response.content,
                finishReason = response.finishReason.name,
                usage = response.usage,
                model = response.model,
                providerUsed = providerType
            )
            
        } catch (e: Exception) {
            Logger.e(TAG, "Error calling AI provider", e)
            stateManager.recordProviderError(providerType, e.message ?: "Unknown error")
            
            // Try fallback if enabled
            if (currentConfig?.enableFallback == true) {
                Logger.d(TAG, "Provider threw exception, trying fallback...")
                return tryFallback(messages)
            }
            
            return AIRouterResponse(
                success = false,
                error = e.message ?: "Unknown error",
                providerUsed = providerType
            )
        } finally {
            activeProvider = null
        }
    }
    
    /**
     * Try fallback provider.
     */
    private suspend fun tryFallback(messages: List<Message>): AIRouterResponse {
        val config = currentConfig ?: return AIRouterResponse(
            success = false,
            error = "No fallback configured"
        )
        
        // Try fallback provider
        config.fallbackProvider?.let { fallbackType ->
            val fallback = providers[fallbackType]
            if (fallback != null && fallback.isAvailable()) {
                Logger.d(TAG, "Trying fallback provider: $fallbackType")
                
                try {
                    val response = fallback.chatCompletion(messages)
                    
                    if (response.error == null) {
                        stateManager.recordProviderUsage(fallbackType)
                        return AIRouterResponse(
                            success = true,
                            content = response.content,
                            finishReason = response.finishReason.name,
                            usage = response.usage,
                            model = response.model,
                            providerUsed = fallbackType,
                            usedFallback = true
                        )
                    }
                } catch (e: Exception) {
                    Logger.e(TAG, "Fallback provider also failed", e)
                }
            }
        }
        
        return AIRouterResponse(
            success = false,
            error = "All AI providers failed"
        )
    }
    
    /**
     * Get the currently active provider.
     */
    fun getActiveProvider(): AIProvider? = activeProvider
    
    /**
     * Check if any provider is available.
     */
    fun hasAvailableProvider(): Boolean {
        return providers.values.any { it.isAvailable() }
    }
    
    /**
     * Release all providers.
     */
    suspend fun release() {
        Logger.d(TAG, "Releasing AIRouter...")
        providers.forEach { (_, provider) ->
            try {
                provider.release()
            } catch (e: Exception) {
                Logger.e(TAG, "Error releasing provider", e)
            }
        }
        providers.clear()
        activeProvider = null
        stateManager.reset()
    }
    
    /**
     * Response from AIRouter.
     */
    data class AIRouterResponse(
        val success: Boolean,
        val content: String = "",
        val error: String? = null,
        val providerUsed: AIProvider.ProviderType? = null,
        val usedFallback: Boolean = false,
        val finishReason: String? = null,
        val usage: AIProvider.Usage? = null,
        val model: String? = null
    )
}
