package com.axxist.app.runtime.ai

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.AIEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.ai.config.AIConfiguration
import com.axxist.app.runtime.ai.state.AIStateManager
import com.axxist.app.runtime.conversation.model.Message
import com.axxist.app.runtime.conversation.provider.AIProvider

/**
 * AIManager - Main coordinator for AI operations.
 * 
 * Responsibilities:
 * - Receive conversation requests
 * - Coordinate providers
 * - Return responses
 * 
 * Flow:
 * User Message → Conversation Engine → AI Router → Selected Provider → Response
 */
class AIManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "AIManager"
        
        @Volatile
        private var instance: AIManager? = null
        
        fun initialize(context: Context): AIManager {
            return instance ?: synchronized(this) {
                instance ?: AIManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): AIManager {
            return instance ?: throw IllegalStateException("AIManager not initialized")
        }
    }
    
    private val stateManager = AIStateManager.getInstance()
    private val router = AIRouter.getInstance()
    private var isInitialized = false
    private var currentConfig: AIConfiguration? = null
    
    /**
     * Initialize the AI system.
     */
    fun initialize(config: AIConfiguration = AIConfiguration.default()): Boolean {
        Logger.d(TAG, "Initializing AIManager...")
        
        currentConfig = config
        
        if (!config.enabled) {
            Logger.w(TAG, "AI is disabled in configuration")
            stateManager.disable()
            return false
        }
        
        val success = router.initialize(config)
        
        if (success) {
            stateManager.enable()
            isInitialized = true
            Logger.d(TAG, "AIManager initialized successfully")
        } else {
            Logger.e(TAG, "Failed to initialize AI router")
        }
        
        return success
    }
    
    /**
     * Send a request to AI and get response.
     */
    suspend fun sendRequest(messages: List<Message>): AIResult {
        Logger.d(TAG, "Sending AI request with ${messages.size} messages")
        
        if (!isInitialized) {
            Logger.e(TAG, "AIManager not initialized")
            return AIResult.Error("AIManager not initialized")
        }
        
        if (!stateManager.startRequest()) {
            Logger.e(TAG, "Cannot start AI request from current state")
            return AIResult.Error("Invalid state for AI request")
        }
        
        // Emit request started event
        EventBusManager.emitAIEvent(AIEvent.RequestStarted)
        
        return try {
            val response = router.sendRequest(messages)
            
            stateManager.responseComplete()
            
            if (response.success) {
                EventBusManager.emitAIEvent(
                    AIEvent.ResponseReceived(
                        response.content,
                        response.providerUsed?.name ?: "unknown",
                        response.usage?.totalTokens ?: 0
                    )
                )
                
                AIResult.Success(
                    content = response.content,
                    provider = response.providerUsed?.name ?: "unknown",
                    tokens = response.usage?.totalTokens ?: 0,
                    usedFallback = response.usedFallback
                )
            } else {
                stateManager.error(response.error ?: "Unknown error")
                AIResult.Error(response.error ?: "Unknown error")
            }
            
        } catch (e: Exception) {
            Logger.e(TAG, "Error in AI request", e)
            stateManager.error(e.message ?: "Unknown error")
            AIResult.Error(e.message ?: "Unknown error")
        }
    }
    
    /**
     * Send a simple text request.
     */
    suspend fun sendTextRequest(text: String): AIResult {
        val message = Message.userMessage(text)
        return sendRequest(listOf(message))
    }
    
    /**
     * Get the current state.
     */
    fun getState() = stateManager.getCurrentState()
    
    /**
     * Check if AI is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Check if AI is enabled.
     */
    fun isEnabled(): Boolean = currentConfig?.enabled == true
    
    /**
     * Get the router.
     */
    fun getRouter(): AIRouter = router
    
    /**
     * Get configuration.
     */
    fun getConfiguration(): AIConfiguration? = currentConfig
    
    /**
     * Update configuration.
     */
    fun updateConfiguration(config: AIConfiguration): Boolean {
        Logger.d(TAG, "Updating AI configuration...")
        currentConfig = config
        return initialize(config)
    }
    
    /**
     * Get available providers.
     */
    fun getAvailableProviders(): List<AIProvider.ProviderType> {
        return stateManager.getAvailableProviders()
    }
    
    /**
     * Get provider status.
     */
    fun getProviderStatus(type: AIProvider.ProviderType) = stateManager.getProviderStatus(type)
    
    /**
     * Get summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "isEnabled" to isEnabled(),
        "availableProviders" to getAvailableProviders().map { it.name },
        "currentConfig" to mapOf(
            "preferredProvider" to (currentConfig?.preferredProvider?.name ?: "none"),
            "fallbackProvider" to (currentConfig?.fallbackProvider?.name ?: "none"),
            "timeout" to (currentConfig?.timeoutMs ?: 0),
            "enabled" to (currentConfig?.enabled ?: false)
        )
    )
    
    /**
     * Reset the AI system.
     */
    suspend fun reset() {
        Logger.d(TAG, "Resetting AIManager...")
        router.release()
        stateManager.reset()
        isInitialized = false
        currentConfig = null
    }
    
    /**
     * AI Result sealed class.
     */
    sealed class AIResult {
        data class Success(
            val content: String,
            val provider: String,
            val tokens: Int,
            val usedFallback: Boolean = false
        ) : AIResult()
        
        data class Error(val message: String) : AIResult()
    }
}
