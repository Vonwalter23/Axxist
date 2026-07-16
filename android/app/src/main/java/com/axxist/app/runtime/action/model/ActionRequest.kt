package com.axxist.app.runtime.action.model

/**
 * Request to execute an action.
 */
data class ActionRequest(
    val actionId: String,
    val parameters: Map<String, Any> = emptyMap(),
    val priority: ActionPriority = ActionPriority.NORMAL,
    val metadata: ActionMetadata = ActionMetadata(),
    val retryPolicy: RetryPolicy? = null,
    val validationOnly: Boolean = false
) {
    fun getStringParam(key: String): String? = parameters[key] as? String
    fun getIntParam(key: String): Int? = parameters[key] as? Int
    fun getBooleanParam(key: String): Boolean? = parameters[key] as? Boolean
    fun getListParam(key: String): List<Any>? = parameters[key] as? List<*>>
}
