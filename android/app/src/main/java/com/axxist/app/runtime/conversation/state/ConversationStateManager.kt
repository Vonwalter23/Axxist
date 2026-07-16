package com.axxist.app.runtime.conversation.state

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.ConversationEvent
import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ConversationStateManager - Manages the state transitions of the conversation system.
 */
class ConversationStateManager {
    
    companion object {
        private const val TAG = "ConversationStateManager"
        
        @Volatile
        private var instance: ConversationStateManager? = null
        
        fun getInstance(): ConversationStateManager {
            return instance ?: synchronized(this) {
                instance ?: ConversationStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(ConversationState.IDLE)
    val state: StateFlow<ConversationState> = _state.asStateFlow()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: ConversationState,
        val to: ConversationState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentState(): ConversationState = _state.value
    
    fun canTransitionTo(targetState: ConversationState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: ConversationState, errorMessage: String? = null): Boolean {
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
        val event = ConversationEvent.StateChanged(currentState, newState, errorMessage)
        EventBusManager.emitConversationEvent(event)
        
        Logger.d(TAG, "Conversation state transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun startConversation(): Boolean {
        return when (_state.value) {
            ConversationState.IDLE, ConversationState.ENDED -> {
                transitionTo(ConversationState.LISTENING)
            }
            else -> {
                Logger.w(TAG, "Cannot start conversation from state: ${_state.value}")
                false
            }
        }
    }
    
    fun startProcessing(): Boolean {
        return if (_state.value == ConversationState.LISTENING) {
            transitionTo(ConversationState.PROCESSING)
        } else {
            Logger.w(TAG, "Cannot start processing from state: ${_state.value}")
            false
        }
    }
    
    fun startResponding(): Boolean {
        return if (_state.value == ConversationState.PROCESSING) {
            transitionTo(ConversationState.RESPONDING)
        } else {
            Logger.w(TAG, "Cannot start responding from state: ${_state.value}")
            false
        }
    }
    
    fun endConversation(): Boolean {
        return when (_state.value) {
            ConversationState.LISTENING, ConversationState.PROCESSING, 
            ConversationState.RESPONDING -> {
                transitionTo(ConversationState.ENDED)
            }
            else -> {
                Logger.w(TAG, "Cannot end conversation from state: ${_state.value}")
                false
            }
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(ConversationState.ERROR, message)
        if (wasActive) {
            EventBusManager.emitConversationEvent(ConversationEvent.Error(message))
        }
        return result
    }
    
    fun reset(): Boolean {
        val oldState = _state.value
        _state.value = ConversationState.IDLE
        stateHistory.clear()
        Logger.d(TAG, "Conversation state manager reset from $oldState")
        return true
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
}
