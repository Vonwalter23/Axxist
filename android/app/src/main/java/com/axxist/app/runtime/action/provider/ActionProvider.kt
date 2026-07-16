package com.axxist.app.runtime.action.provider

import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionResult

/**
 * Interface for action providers.
 * Providers implement actual action execution logic.
 */
interface ActionProvider {
    
    /**
     * Execute an action.
     */
    suspend fun execute(request: ActionRequest): ActionResult
    
    /**
     * Check if this provider can handle the action.
     */
    fun canHandle(actionId: String): Boolean
    
    /**
     * Get the provider name.
     */
    fun getName(): String
    
    /**
     * Get the provider priority (higher = preferred).
     */
    fun getPriority(): Int = 0
}
