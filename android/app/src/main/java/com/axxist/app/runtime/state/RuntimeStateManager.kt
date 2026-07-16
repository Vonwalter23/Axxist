package com.axxist.app.runtime.state

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.RuntimeEvent
import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * RuntimeStateManager - Manages the state transitions of the Runtime.
 */
class RuntimeStateManager {
    
    companion object {
        private const val TAG = "RuntimeStateManager"
        
        @Volatile
        private var instance: RuntimeStateManager? = null
        
        fun getInstance(): RuntimeStateManager {
            return instance ?: synchronized(this) {
                instance ?: RuntimeStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(RuntimeState.STOPPED)
    val state: StateFlow<RuntimeState> = _state.asStateFlow()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: RuntimeState,
        val to: RuntimeState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentState(): RuntimeState = _state.value
    
    fun canTransitionTo(targetState: RuntimeState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: RuntimeState, errorMessage: String? = null): Boolean {
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
        
        val event = RuntimeEvent.StateChanged(currentState, newState, errorMessage)
        EventBusManager.emitRuntimeEvent(event)
        
        Logger.d(TAG, "State transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun start(): Boolean {
        return when (_state.value) {
            RuntimeState.STOPPED, RuntimeState.ERROR -> {
                transitionTo(RuntimeState.STARTING)
            }
            else -> {
                Logger.w(TAG, "Cannot start from state: ${_state.value}")
                false
            }
        }
    }
    
    fun running(): Boolean {
        return if (_state.value == RuntimeState.STARTING) {
            transitionTo(RuntimeState.RUNNING)
        } else {
            Logger.w(TAG, "Cannot set running from state: ${_state.value}")
            false
        }
    }
    
    fun pause(): Boolean {
        return transitionTo(RuntimeState.PAUSED)
    }
    
    fun resume(): Boolean {
        return if (_state.value == RuntimeState.PAUSED) {
            transitionTo(RuntimeState.RUNNING)
        } else {
            Logger.w(TAG, "Cannot resume from state: ${_state.value}")
            false
        }
    }
    
    fun stop(): Boolean {
        return when (_state.value) {
            RuntimeState.RUNNING, RuntimeState.PAUSED, RuntimeState.STARTING -> {
                transitionTo(RuntimeState.STOPPING)
                true
            }
            else -> {
                Logger.w(TAG, "Cannot stop from state: ${_state.value}")
                false
            }
        }
    }
    
    fun stopComplete(): Boolean {
        return if (_state.value == RuntimeState.STOPPING) {
            transitionTo(RuntimeState.STOPPED)
        } else {
            Logger.w(TAG, "Cannot complete stop from state: ${_state.value}")
            false
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(RuntimeState.ERROR, message)
        if (wasActive) {
            EventBusManager.emitRuntimeEvent(RuntimeEvent.Error(message))
        }
        return result
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
    
    fun getUptime(): Long {
        val current = _state.value
        return if (current.isActive() && stateHistory.isNotEmpty()) {
            val runningTransition = stateHistory.lastOrNull { it.to == RuntimeState.RUNNING }
            if (runningTransition != null) {
                System.currentTimeMillis() - runningTransition.timestamp
            } else {
                0L
            }
        } else {
            0L
        }
    }
    
    fun reset() {
        val oldState = _state.value
        _state.value = RuntimeState.STOPPED
        stateHistory.clear()
        Logger.d(TAG, "State manager reset from $oldState")
    }
}
