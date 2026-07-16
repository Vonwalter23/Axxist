package com.axxist.app.runtime.wakeword.state

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.WakeWordEvent
import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * WakeWordStateManager - Manages the state transitions of the wake word detection system.
 */
class WakeWordStateManager {
    
    companion object {
        private const val TAG = "WakeWordStateManager"
        
        @Volatile
        private var instance: WakeWordStateManager? = null
        
        fun getInstance(): WakeWordStateManager {
            return instance ?: synchronized(this) {
                instance ?: WakeWordStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(WakeWordState.DISABLED)
    val state: StateFlow<WakeWordState> = _state.asStateFlow()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: WakeWordState,
        val to: WakeWordState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentState(): WakeWordState = _state.value
    
    fun canTransitionTo(targetState: WakeWordState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: WakeWordState, errorMessage: String? = null): Boolean {
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
        val event = WakeWordEvent.StateChanged(currentState, newState, errorMessage)
        EventBusManager.emitWakeWordEvent(event)
        
        Logger.d(TAG, "WakeWord state transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun initialize(): Boolean {
        return when (_state.value) {
            WakeWordState.DISABLED, WakeWordState.ERROR -> {
                transitionTo(WakeWordState.INITIALIZING)
            }
            else -> {
                Logger.w(TAG, "Cannot initialize from state: ${_state.value}")
                false
            }
        }
    }
    
    fun initializeComplete(): Boolean {
        return if (_state.value == WakeWordState.INITIALIZING) {
            transitionTo(WakeWordState.LISTENING)
        } else {
            Logger.w(TAG, "Cannot complete initialization from state: ${_state.value}")
            false
        }
    }
    
    fun startListening(): Boolean {
        return when (_state.value) {
            WakeWordState.DISABLED, WakeWordState.ERROR -> {
                initialize()
                transitionTo(WakeWordState.LISTENING)
                true
            }
            WakeWordState.LISTENING -> {
                Logger.w(TAG, "Already listening")
                true
            }
            else -> {
                Logger.w(TAG, "Cannot start listening from state: ${_state.value}")
                false
            }
        }
    }
    
    fun detected(): Boolean {
        return if (_state.value == WakeWordState.LISTENING) {
            transitionTo(WakeWordState.DETECTED)
        } else {
            Logger.w(TAG, "Cannot set detected from state: ${_state.value}")
            false
        }
    }
    
    fun resetAfterDetection(): Boolean {
        return if (_state.value == WakeWordState.DETECTED) {
            transitionTo(WakeWordState.LISTENING)
        } else {
            Logger.w(TAG, "Cannot reset from state: ${_state.value}")
            false
        }
    }
    
    fun stop(): Boolean {
        return when (_state.value) {
            WakeWordState.LISTENING, WakeWordState.DETECTED, WakeWordState.INITIALIZING -> {
                transitionTo(WakeWordState.DISABLED)
                true
            }
            else -> {
                Logger.w(TAG, "Cannot stop from state: ${_state.value}")
                false
            }
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(WakeWordState.ERROR, message)
        if (wasActive) {
            EventBusManager.emitWakeWordEvent(WakeWordEvent.Error(message))
        }
        return result
    }
    
    fun reset(): Boolean {
        val oldState = _state.value
        _state.value = WakeWordState.DISABLED
        stateHistory.clear()
        Logger.d(TAG, "WakeWord state manager reset from $oldState")
        return true
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
}
