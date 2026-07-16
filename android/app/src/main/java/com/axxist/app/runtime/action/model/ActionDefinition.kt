package com.axxist.app.runtime.action.model

/**
 * Definition of an action registered in the system.
 */
data class ActionDefinition(
    val id: String,
    val name: String,
    val description: String,
    val category: ActionCategory,
    val type: ActionType = ActionType.LOCAL,
    val requiredPermissions: List<String> = emptyList(),
    val requiredEntities: List<String> = emptyList(),
    val optionalParameters: List<String> = emptyList(),
    val priority: ActionPriority = ActionPriority.NORMAL,
    val timeout: Long = 30000L, // 30 seconds default
    val retryPolicy: RetryPolicy = RetryPolicy(),
    val status: ActionStatus = ActionStatus.REGISTERED,
    val version: String = "1.0",
    val examples: List<String> = emptyList()
) {
    fun isExecutable(): Boolean = status == ActionStatus.ACTIVE
    
    fun requiresPermission(permission: String): Boolean = permission in requiredPermissions
}
