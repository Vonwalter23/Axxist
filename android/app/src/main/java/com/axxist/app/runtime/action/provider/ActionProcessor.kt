package com.axxist.app.runtime.action.provider

import com.axxist.app.runtime.action.model.ActionRequest
import com.axxist.app.runtime.action.model.ActionResult

/**
 * Main processor interface for the Action Framework.
 */
interface ActionProcessor {
    
    /**
     * Process an action request.
     */
    suspend fun process(request: ActionRequest): ActionResult
    
    /**
     * Check if this processor is available.
     */
    fun isAvailable(): Boolean
    
    /**
     * Get the processor name.
     */
    fun getName(): String
}
