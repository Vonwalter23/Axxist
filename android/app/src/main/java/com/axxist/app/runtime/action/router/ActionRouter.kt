package com.axxist.app.runtime.action.router

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.ActionDefinition
import com.axxist.app.runtime.action.model.ActionType

/**
 * Router for selecting execution mechanism.
 */
class ActionRouter {
    
    companion object {
        private const val TAG = "ActionRouter"
    }
    
    /**
     * Select the execution mechanism for an action.
     */
    fun selectExecutionType(definition: ActionDefinition): ExecutionPlan {
        Logger.d(TAG, "Selecting execution type for: ${definition.id}")
        
        return when (definition.type) {
            ActionType.ANDROID -> ExecutionPlan(
                mechanism = ExecutionMechanism.ANDROID,
                requiresAndroid = true,
                requiresNetwork = false,
                description = "Android system execution"
            )
            
            ActionType.LOCAL -> ExecutionPlan(
                mechanism = ExecutionMechanism.LOCAL,
                requiresAndroid = false,
                requiresNetwork = false,
                description = "Local execution"
            )
            
            ActionType.EXTERNAL -> ExecutionPlan(
                mechanism = ExecutionMechanism.EXTERNAL,
                requiresAndroid = false,
                requiresNetwork = true,
                description = "External service/API execution"
            )
            
            ActionType.HYBRID -> ExecutionPlan(
                mechanism = ExecutionMechanism.HYBRID,
                requiresAndroid = true,
                requiresNetwork = true,
                description = "Hybrid execution (Android + External)"
            )
        }
    }
    
    /**
     * Select the best executor for an action.
     */
    fun selectExecutor(
        definition: ActionDefinition,
        availableExecutors: List<String>
    ): String? {
        if (availableExecutors.isEmpty()) {
            Logger.w(TAG, "No executors available for ${definition.id}")
            return null
        }
        
        // Select based on action type
        return when (definition.type) {
            ActionType.ANDROID -> availableExecutors.find { it.contains("android", ignoreCase = true) }
            ActionType.LOCAL -> availableExecutors.find { it.contains("local", ignoreCase = true) }
            ActionType.EXTERNAL -> availableExecutors.find { it.contains("external", ignoreCase = true) }
            ActionType.HYBRID -> availableExecutors.find { it.contains("hybrid", ignoreCase = true) }
        } ?: availableExecutors.firstOrNull()
    }
    
    enum class ExecutionMechanism {
        ANDROID,
        LOCAL,
        EXTERNAL,
        HYBRID,
        UNKNOWN
    }
    
    data class ExecutionPlan(
        val mechanism: ExecutionMechanism,
        val requiresAndroid: Boolean,
        val requiresNetwork: Boolean,
        val description: String
    )
}
