package com.axxist.app.runtime.intent.state

import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State manager for the Intent Framework.
 */
class IntentStateManager {
    
    companion object {
        private const val TAG = "IntentStateManager"
        
        @Volatile
        private var instance: IntentStateManager? = null
        
        fun getInstance(): IntentStateManager {
            return instance ?: synchronized(this) {
                instance ?: IntentStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(IntentState.IDLE)
    val state: StateFlow<IntentState> = _state.asStateFlow()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: IntentState,
        val to: IntentState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentState(): IntentState = _state.value
    
    fun canTransitionTo(targetState: IntentState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: IntentState, errorMessage: String? = null): Boolean {
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
        
        Logger.d(TAG, "Intent state transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun startAnalysis(): Boolean {
        return when (_state.value) {
            IntentState.IDLE -> transitionTo(IntentState.ANALYZING)
            else -> {
                Logger.w(TAG, "Cannot start analysis from state: ${_state.value}")
                false
            }
        }
    }
    
    fun matched(): Boolean {
        return if (_state.value == IntentState.ANALYZING) {
            transitionTo(IntentState.MATCHED)
        } else {
            Logger.w(TAG, "Cannot set matched from state: ${_state.value}")
            false
        }
    }
    
    fun startValidation(): Boolean {
        return if (_state.value == IntentState.MATCHED) {
            transitionTo(IntentState.VALIDATING)
        } else {
            Logger.w(TAG, "Cannot start validation from state: ${_state.value}")
            false
        }
    }
    
    fun ready(): Boolean {
        return if (_state.value == IntentState.VALIDATING) {
            transitionTo(IntentState.READY)
        } else {
            Logger.w(TAG, "Cannot set ready from state: ${_state.value}")
            false
        }
    }
    
    fun fail(): Boolean {
        val current = _state.value
        return if (current.isActive() || current == IntentState.MATCHED || current == IntentState.VALIDATING) {
            transitionTo(IntentState.FAILED)
        } else {
            Logger.w(TAG, "Cannot fail from state: $current")
            false
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(IntentState.ERROR, message)
        return result
    }
    
    fun reset(): Boolean {
        val result = transitionTo(IntentState.IDLE)
        if (result) {
            stateHistory.clear()
        }
        return result
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
}
