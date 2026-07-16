package com.axxist.app.runtime.action.state

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.ActionState

/**
 * Manager for action state transitions.
 */
class ActionStateManager {
    
    companion object {
        private const val TAG = "ActionStateManager"
    }
    
    private val stateHistory = mutableMapOf<String, MutableList<StateTransition>>()
    
    data class StateTransition(
        val fromState: ActionState,
        val toState: ActionState,
        val timestamp: Long = System.currentTimeMillis()
    )
    
    /**
     * Transition to a new state.
     */
    fun transition(actionId: String, fromState: ActionState, toState: ActionState): Boolean {
        if (!fromState.canTransitionTo(toState)) {
            Logger.w(TAG, "Invalid transition for $actionId: $fromState -> $toState")
            return false
        }
        
        val transition = StateTransition(fromState, toState)
        stateHistory.getOrPut(actionId) { mutableListOf() }.add(transition)
        
        Logger.d(TAG, "State transition for $actionId: $fromState -> $toState")
        return true
    }
    
    /**
     * Check if a transition is valid.
     */
    fun canTransition(fromState: ActionState, toState: ActionState): Boolean {
        return fromState.canTransitionTo(toState)
    }
    
    /**
     * Get state history for an action.
     */
    fun getHistory(actionId: String): List<StateTransition> {
        return stateHistory[actionId]?.toList() ?: emptyList()
    }
    
    /**
     * Clear history for an action.
     */
    fun clearHistory(actionId: String) {
        stateHistory.remove(actionId)
    }
    
    /**
     * Clear all history.
     */
    fun clear() {
        stateHistory.clear()
    }
}
