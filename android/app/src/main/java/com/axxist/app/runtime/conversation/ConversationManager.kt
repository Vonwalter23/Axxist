package com.axxist.app.runtime.conversation

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.ConversationEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.conversation.model.Message
import com.axxist.app.runtime.conversation.model.MessageRole
import com.axxist.app.runtime.conversation.provider.AIProvider
import com.axxist.app.runtime.conversation.provider.IntentProcessor
import com.axxist.app.runtime.conversation.provider.ResponseGenerator
import com.axxist.app.runtime.conversation.state.ConversationState
import com.axxist.app.runtime.conversation.state.ConversationStateManager

/**
 * ConversationManager - Main coordinator for the conversation system.
 * 
 * Responsibilities:
 * - Start/end conversation
 * - Administer state
 * - Coordinate messages
 * - Integrate with AudioManager, WakeWordManager, RuntimeManager
 * 
 * Flow:
 * Wake Word Detected → Conversation Started → Audio Input → Processing → Response
 */
class ConversationManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "ConversationManager"
        
        @Volatile
        private var instance: ConversationManager? = null
        
        fun initialize(context: Context): ConversationManager {
            return instance ?: synchronized(this) {
                instance ?: ConversationManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): ConversationManager {
            return instance ?: throw IllegalStateException("ConversationManager not initialized")
        }
    }
    
    private val stateManager = ConversationStateManager.getInstance()
    private val contextManager = ConversationContextManager.getInstance()
    private val listeners = mutableListOf<ConversationListener>()
    
    private var aiProvider: AIProvider? = null
    private var intentProcessor: IntentProcessor? = null
    private var responseGenerator: ResponseGenerator? = null
    
    /**
     * Conversation listener interface.
     */
    interface ConversationListener {
        fun onConversationStarted(sessionId: String)
        fun onUserMessageReceived(message: Message)
        fun onProcessingStarted()
        fun onAssistantResponseReady(response: String)
        fun onConversationEnded()
        fun onConversationError(error: String)
        fun onStateChanged(state: ConversationState, previousState: ConversationState)
    }
    
    init {
        Logger.d(TAG, "ConversationManager initialized")
    }
    
    // ========== Provider Setters ==========
    
    fun setAIProvider(provider: AIProvider?) {
        this.aiProvider = provider
        Logger.d(TAG, "AI Provider set: ${provider?.getProviderType()?.name ?: "none"}")
    }
    
    fun setIntentProcessor(processor: IntentProcessor?) {
        this.intentProcessor = processor
        Logger.d(TAG, "Intent Processor set: ${if (processor != null) "available" else "none"}")
    }
    
    fun setResponseGenerator(generator: ResponseGenerator?) {
        this.responseGenerator = generator
        Logger.d(TAG, "Response Generator set: ${if (generator != null) "available" else "none"}")
    }
    
    // ========== Session Management ==========
    
    /**
     * Start a new conversation.
     */
    fun startConversation(): Boolean {
        Logger.d(TAG, "Starting conversation...")
        
        if (stateManager.getCurrentState().isActive()) {
            Logger.w(TAG, "Conversation already active")
            return true
        }
        
        if (!stateManager.startConversation()) {
            return false
        }
        
        val session = contextManager.startSession()
        
        EventBusManager.emitConversationEvent(ConversationEvent.Started(session.id))
        notifyListeners { it.onConversationStarted(session.id) }
        
        Logger.d(TAG, "Conversation started: ${session.id}")
        return true
    }
    
    /**
     * End the current conversation.
     */
    fun endConversation(): Boolean {
        Logger.d(TAG, "Ending conversation...")
        
        val previousState = stateManager.getCurrentState()
        
        if (!stateManager.endConversation()) {
            return false
        }
        
        contextManager.endSession()
        
        EventBusManager.emitConversationEvent(ConversationEvent.Ended)
        notifyListeners { it.onConversationEnded() }
        
        Logger.d(TAG, "Conversation ended")
        return true
    }
    
    /**
     * Handle user input (from audio transcription).
     */
    suspend fun handleUserInput(text: String): Boolean {
        Logger.d(TAG, "Processing user input: ${text.take(50)}...")
        
        if (!stateManager.startProcessing()) {
            Logger.e(TAG, "Cannot process from state: ${stateManager.getCurrentState()}")
            return false
        }
        
        // Add user message
        val userMessage = contextManager.addUserMessage(text)
        EventBusManager.emitConversationEvent(ConversationEvent.UserMessageReceived(userMessage.id, text))
        notifyListeners { it.onUserMessageReceived(userMessage) }
        
        EventBusManager.emitConversationEvent(ConversationEvent.ProcessingStarted)
        notifyListeners { it.onProcessingStarted() }
        
        // Process the message (placeholder for actual AI processing)
        awaitAssistantResponse(text)
        
        return true
    }
    
    /**
     * Await and emit assistant response.
     */
    private suspend fun awaitAssistantResponse(userText: String) {
        stateManager.startResponding()
        
        // In a real implementation, this would call the AI provider
        // For now, we create a placeholder response
        val responseText = "Procesando: $userText"
        
        val assistantMessage = contextManager.addAssistantMessage(responseText)
        
        EventBusManager.emitConversationEvent(ConversationEvent.AssistantResponseReady(assistantMessage.id, responseText))
        notifyListeners { it.onAssistantResponseReady(responseText) }
        
        // Return to listening state
        stateManager.startConversation()
    }
    
    /**
     * Get current conversation state.
     */
    fun getState(): ConversationState = stateManager.getCurrentState()
    
    /**
     * Check if conversation is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Get current session.
     */
    fun getCurrentSession() = contextManager.getCurrentSession()
    
    /**
     * Get context manager.
     */
    fun getContextManager() = contextManager
    
    // ========== Listener Management ==========
    
    fun addListener(listener: ConversationListener) {
        listeners.add(listener)
    }
    
    fun removeListener(listener: ConversationListener) {
        listeners.remove(listener)
    }
    
    // ========== Summary & Reset ==========
    
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "sessionId" to (contextManager.getCurrentSession()?.id ?: "none"),
        "messageCount" to contextManager.getMessageCount(),
        "hasAIProvider" to (aiProvider != null),
        "hasIntentProcessor" to (intentProcessor != null),
        "hasResponseGenerator" to (responseGenerator != null)
    )
    
    fun reset() {
        Logger.d(TAG, "Resetting ConversationManager...")
        stateManager.reset()
        contextManager.reset()
        listeners.clear()
        aiProvider = null
        intentProcessor = null
        responseGenerator = null
    }
    
    private fun notifyListeners(action: (ConversationListener) -> Unit) {
        listeners.forEach { listener ->
            try {
                action(listener)
            } catch (e: Exception) {
                Logger.e(TAG, "Error notifying listener", e)
            }
        }
    }
}
