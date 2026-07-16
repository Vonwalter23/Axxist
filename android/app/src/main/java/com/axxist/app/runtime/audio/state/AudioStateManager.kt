package com.axxist.app.runtime.audio.state

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.AudioEvent
import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * AudioStateManager - Manages the state transitions of the audio system.
 */
class AudioStateManager {
    
    companion object {
        private const val TAG = "AudioStateManager"
        
        @Volatile
        private var instance: AudioStateManager? = null
        
        fun getInstance(): AudioStateManager {
            return instance ?: synchronized(this) {
                instance ?: AudioStateManager().also { instance = it }
            }
        }
    }
    
    private val _state = MutableStateFlow(AudioState.IDLE)
    val state: StateFlow<AudioState> = _state.asStateFlow()
    
    private val stateHistory = mutableListOf<StateTransition>()
    private val maxHistorySize = 50
    
    data class StateTransition(
        val from: AudioState,
        val to: AudioState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    fun getCurrentState(): AudioState = _state.value
    
    fun canTransitionTo(targetState: AudioState): Boolean {
        return _state.value.canTransitionTo(targetState)
    }
    
    fun transitionTo(newState: AudioState, errorMessage: String? = null): Boolean {
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
        val event = AudioEvent.StateChanged(currentState, newState, errorMessage)
        EventBusManager.emitAudioEvent(event)
        
        Logger.d(TAG, "Audio state transition: $currentState -> $newState")
        errorMessage?.let { Logger.e(TAG, it) }
        
        return true
    }
    
    fun initialize(): Boolean {
        return when (_state.value) {
            AudioState.IDLE, AudioState.ERROR -> {
                transitionTo(AudioState.INITIALIZING)
            }
            else -> {
                Logger.w(TAG, "Cannot initialize from state: ${_state.value}")
                false
            }
        }
    }
    
    fun initializeComplete(): Boolean {
        return if (_state.value == AudioState.INITIALIZING) {
            transitionTo(AudioState.LISTENING)
        } else {
            Logger.w(TAG, "Cannot complete initialization from state: ${_state.value}")
            false
        }
    }
    
    fun startListening(): Boolean {
        return when (_state.value) {
            AudioState.IDLE -> {
                transitionTo(AudioState.INITIALIZING)
                transitionTo(AudioState.LISTENING)
                true
            }
            AudioState.LISTENING -> {
                Logger.w(TAG, "Already listening")
                true
            }
            else -> {
                Logger.w(TAG, "Cannot start listening from state: ${_state.value}")
                false
            }
        }
    }
    
    fun startProcessing(): Boolean {
        return if (_state.value == AudioState.LISTENING) {
            transitionTo(AudioState.PROCESSING)
        } else {
            Logger.w(TAG, "Cannot start processing from state: ${_state.value}")
            false
        }
    }
    
    fun processingComplete(): Boolean {
        return if (_state.value == AudioState.PROCESSING) {
            transitionTo(AudioState.LISTENING)
        } else {
            Logger.w(TAG, "Cannot complete processing from state: ${_state.value}")
            false
        }
    }
    
    fun stop(): Boolean {
        return when (_state.value) {
            AudioState.LISTENING, AudioState.PROCESSING, AudioState.INITIALIZING -> {
                transitionTo(AudioState.STOPPING)
                true
            }
            else -> {
                Logger.w(TAG, "Cannot stop from state: ${_state.value}")
                false
            }
        }
    }
    
    fun stopComplete(): Boolean {
        return if (_state.value == AudioState.STOPPING) {
            transitionTo(AudioState.IDLE)
        } else {
            Logger.w(TAG, "Cannot complete stop from state: ${_state.value}")
            false
        }
    }
    
    fun error(message: String): Boolean {
        val wasActive = _state.value.isActive()
        val result = transitionTo(AudioState.ERROR, message)
        if (wasActive) {
            EventBusManager.emitAudioEvent(AudioEvent.Error(message))
        }
        return result
    }
    
    fun reset(): Boolean {
        val oldState = _state.value
        _state.value = AudioState.IDLE
        stateHistory.clear()
        Logger.d(TAG, "Audio state manager reset from $oldState")
        return true
    }
    
    fun getStateHistory(): List<StateTransition> = stateHistory.toList()
}
