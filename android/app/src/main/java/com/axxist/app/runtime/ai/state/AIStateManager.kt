package com.axxist.app.runtime.ai.state

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.AIEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.conversation.provider.AIProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * AIStateManager - Manages the state of the AI system.
 */
class AIStateManager {
    
    companion object {
        private const val TAG = "AIStateManager"
        
        @Volatile
        private var instance: AIStateManager? = null
        
        fun getInstance(): AIStateManager {
            return instance ?: synchronized(this) {
                instance ?: AIStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(AIState.IDLE)
    val state: StateFlow<AIState> = _state.asStateFlow()
    
    private val providerStatuses = mutableMapOf<AIProvider.ProviderType, AIProviderStatus>()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: AIState,
        val to: AIState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    init {
        // Initialize provider statuses
        AIProvider.ProviderType.entries.forEach { type ->
            providerStatuses[type] = AIProviderStatus(
                type = type,
                isAvailable = false,
                isEnabled = type == AIProvider.ProviderType.NONE
            )
        }
    }
    
    fun getCurrentState(): AIState = _state.value
    
    fun canTransitionTo(targetState: AIState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: AIState, errorMessage: String? = null): Boolean {
        val currentState = _state.value
        
        if (!currentState.canTransitionTo(newState)) {
            Logger.w(TAG, "Invalid state transition: $currentState -> $newState")
            return false
        }
        
        val transition = StateTransition(currentState, newState)
        stateHistory.add(transition)
        
        while (stateHistory.size > maxHistorySize) {
            stateHistory.removeAt(0)
        }
        
        _state.value = newState
        
        // Emit state change event
        val event = AIEvent.StateChanged(currentState, newState, errorMessage)
        EventBusManager.emitAIEvent(event)
        
        Logger.d(TAG, "AI state transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun enable(): Boolean {
        return when (_state.value) {
            AIState.DISABLED, AIState.ERROR -> {
                transitionTo(AIState.IDLE)
            }
            else -> {
                Logger.w(TAG, "Cannot enable from state: ${_state.value}")
                false
            }
        }
    }
    
    fun disable(): Boolean {
        val current = _state.value
        if (current.isActive()) {
            Logger.w(TAG, "Cannot disable while active")
            return false
        }
        return transitionTo(AIState.DISABLED)
    }
    
    fun startRequest(): Boolean {
        return when (_state.value) {
            AIState.IDLE -> transitionTo(AIState.REQUESTING)
            else -> {
                Logger.w(TAG, "Cannot start request from state: ${_state.value}")
                false
            }
        }
    }
    
    fun startResponse(): Boolean {
        return if (_state.value == AIState.REQUESTING) {
            transitionTo(AIState.RESPONDING)
        } else {
            Logger.w(TAG, "Cannot start response from state: ${_state.value}")
            false
        }
    }
    
    fun responseComplete(): Boolean {
        return if (_state.value == AIState.RESPONDING) {
            transitionTo(AIState.IDLE)
        } else {
            Logger.w(TAG, "Cannot complete response from state: ${_state.value}")
            false
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(AIState.ERROR, message)
        if (wasActive) {
            EventBusManager.emitAIEvent(AIEvent.Error(message))
        }
        return result
    }
    
    fun reset(): Boolean {
        _state.value = AIState.IDLE
        stateHistory.clear()
        Logger.d(TAG, "AI state manager reset")
        return true
    }
    
    // Provider status management
    
    fun updateProviderStatus(type: AIProvider.ProviderType, isAvailable: Boolean, isEnabled: Boolean = true) {
        val current = providerStatuses[type]
        providerStatuses[type] = current?.copy(
            isAvailable = isAvailable,
            isEnabled = isEnabled
        ) ?: AIProviderStatus(
            type = type,
            isAvailable = isAvailable,
            isEnabled = isEnabled
        )
        Logger.d(TAG, "Provider $type status: available=$isAvailable, enabled=$isEnabled")
    }
    
    fun getProviderStatus(type: AIProvider.ProviderType): AIProviderStatus? {
        return providerStatuses[type]
    }
    
    fun getAllProviderStatuses(): Map<AIProvider.ProviderType, AIProviderStatus> {
        return providerStatuses.toMap()
    }
    
    fun getAvailableProviders(): List<AIProvider.ProviderType> {
        return providerStatuses.entries
            .filter { it.value.isAvailable && it.value.isEnabled }
            .map { it.key }
    }
    
    fun recordProviderUsage(type: AIProvider.ProviderType) {
        val current = providerStatuses[type] ?: return
        providerStatuses[type] = current.copy(
            lastUsed = System.currentTimeMillis(),
            requestCount = current.requestCount + 1
        )
    }
    
    fun recordProviderError(type: AIProvider.ProviderType, error: String) {
        val current = providerStatuses[type] ?: return
        providerStatuses[type] = current.copy(
            lastError = error,
            errorCount = current.errorCount + 1
        )
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
}
