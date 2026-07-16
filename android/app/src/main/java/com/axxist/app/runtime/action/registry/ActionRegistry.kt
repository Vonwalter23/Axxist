package com.axxist.app.runtime.action.registry

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.ActionCategory
import com.axxist.app.runtime.action.model.ActionDefinition
import com.axxist.app.runtime.action.model.ActionPriority
import com.axxist.app.runtime.action.model.ActionStatus
import com.axxist.app.runtime.action.model.ActionType
import com.axxist.app.runtime.action.model.RetryPolicy

/**
 * Registry for action definitions.
 */
class ActionRegistry {
    
    companion object {
        private const val TAG = "ActionRegistry"
    }
    
    private val actions = mutableMapOf<String, ActionDefinition>()
    
    /**
     * Register an action definition.
     */
    fun register(definition: ActionDefinition): Boolean {
        if (actions.containsKey(definition.id)) {
            Logger.w(TAG, "Action ${definition.id} already registered")
            return false
        }
        
        actions[definition.id] = definition
        Logger.d(TAG, "Registered action: ${definition.id}")
        return true
    }
    
    /**
     * Unregister an action.
     */
    fun unregister(actionId: String): Boolean {
        val removed = actions.remove(actionId) != null
        if (removed) {
            Logger.d(TAG, "Unregistered action: $actionId")
        }
        return removed
    }
    
    /**
     * Get an action definition.
     */
    fun get(actionId: String): ActionDefinition? {
        return actions[actionId]
    }
    
    /**
     * Get all registered actions.
     */
    fun getAll(): List<ActionDefinition> {
        return actions.values.toList()
    }
    
    /**
     * Get actions by category.
     */
    fun getByCategory(category: ActionCategory): List<ActionDefinition> {
        return actions.values.filter { it.category == category }
    }
    
    /**
     * Get active actions.
     */
    fun getActive(): List<ActionDefinition> {
        return actions.values.filter { it.status == ActionStatus.ACTIVE }
    }
    
    /**
     * Check if an action is registered.
     */
    fun isRegistered(actionId: String): Boolean {
        return actions.containsKey(actionId)
    }
    
    /**
     * Get actions count.
     */
    fun count(): Int = actions.size
    
    /**
     * Clear all registered actions.
     */
    fun clear() {
        actions.clear()
        Logger.d(TAG, "Registry cleared")
    }
    
    /**
     * Initialize with base actions.
     */
    fun initialize() {
        // System actions
        register(ActionDefinition(
            id = "OPEN_APP",
            name = "Open App",
            description = "Open an application",
            category = ActionCategory.SYSTEM,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.QUERY_PACKAGES"),
            requiredEntities = listOf("APPLICATION"),
            priority = ActionPriority.NORMAL,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "CLOSE_APP",
            name = "Close App",
            description = "Close an application",
            category = ActionCategory.SYSTEM,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.FORCE_STOP_PACKAGES"),
            requiredEntities = listOf("APPLICATION"),
            priority = ActionPriority.NORMAL,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Communication actions
        register(ActionDefinition(
            id = "MAKE_CALL",
            name = "Make Call",
            description = "Make a phone call",
            category = ActionCategory.COMMUNICATION,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.CALL_PHONE"),
            requiredEntities = listOf("PHONE_NUMBER", "PERSON"),
            priority = ActionPriority.HIGH,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SEND_SMS",
            name = "Send SMS",
            description = "Send an SMS message",
            category = ActionCategory.COMMUNICATION,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.SEND_SMS"),
            requiredEntities = listOf("PHONE_NUMBER"),
            optionalParameters = listOf("MESSAGE"),
            priority = ActionPriority.HIGH,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SEND_WHATSAPP",
            name = "Send WhatsApp",
            description = "Send a WhatsApp message",
            category = ActionCategory.COMMUNICATION,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.READ_CONTACTS"),
            requiredEntities = listOf("PERSON"),
            optionalParameters = listOf("MESSAGE"),
            priority = ActionPriority.NORMAL,
            timeout = 15000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SEND_EMAIL",
            name = "Send Email",
            description = "Send an email",
            category = ActionCategory.COMMUNICATION,
            type = ActionType.HYBRID,
            requiredPermissions = listOf("android.permission.READ_CONTACTS"),
            requiredEntities = listOf("EMAIL"),
            optionalParameters = listOf("SUBJECT", "BODY"),
            priority = ActionPriority.NORMAL,
            timeout = 20000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Media actions
        register(ActionDefinition(
            id = "PLAY_MUSIC",
            name = "Play Music",
            description = "Play music or a specific song",
            category = ActionCategory.MEDIA,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.READ_MEDIA_AUDIO"),
            requiredEntities = emptyList(),
            optionalParameters = listOf("ARTIST", "SONG", "PLAYLIST"),
            priority = ActionPriority.NORMAL,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "STOP_MUSIC",
            name = "Stop Music",
            description = "Stop music playback",
            category = ActionCategory.MEDIA,
            type = ActionType.ANDROID,
            priority = ActionPriority.HIGH,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SET_VOLUME",
            name = "Set Volume",
            description = "Set system volume",
            category = ActionCategory.MEDIA,
            type = ActionType.ANDROID,
            requiredEntities = emptyList(),
            optionalParameters = listOf("LEVEL", "STREAM"),
            priority = ActionPriority.HIGH,
            timeout = 3000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Navigation actions
        register(ActionDefinition(
            id = "NAVIGATE_TO",
            name = "Navigate To",
            description = "Navigate to a location",
            category = ActionCategory.DEVICE,
            type = ActionType.ANDROID,
            requiredEntities = listOf("LOCATION"),
            priority = ActionPriority.HIGH,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SEARCH_NEARBY",
            name = "Search Nearby",
            description = "Search for nearby places",
            category = ActionCategory.SEARCH,
            type = ActionType.EXTERNAL,
            requiredEntities = listOf("LOCATION"),
            optionalParameters = listOf("QUERY"),
            priority = ActionPriority.NORMAL,
            timeout = 20000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Productivity actions
        register(ActionDefinition(
            id = "CREATE_REMINDER",
            name = "Create Reminder",
            description = "Create a reminder",
            category = ActionCategory.PRODUCTIVITY,
            type = ActionType.ANDROID,
            requiredEntities = emptyList(),
            optionalParameters = listOf("TITLE", "DATE", "TIME", "DESCRIPTION"),
            priority = ActionPriority.NORMAL,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "CREATE_ALARM",
            name = "Create Alarm",
            description = "Create an alarm",
            category = ActionCategory.PRODUCTIVITY,
            type = ActionType.ANDROID,
            requiredEntities = emptyList(),
            optionalParameters = listOf("TIME", "LABEL", "DAYS"),
            priority = ActionPriority.HIGH,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "CREATE_NOTE",
            name = "Create Note",
            description = "Create a note",
            category = ActionCategory.PRODUCTIVITY,
            type = ActionType.ANDROID,
            requiredEntities = emptyList(),
            optionalParameters = listOf("TITLE", "CONTENT"),
            priority = ActionPriority.NORMAL,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Device actions
        register(ActionDefinition(
            id = "TOGGLE_WIFI",
            name = "Toggle WiFi",
            description = "Turn WiFi on or off",
            category = ActionCategory.DEVICE,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.CHANGE_WIFI_STATE"),
            optionalParameters = listOf("STATE"),
            priority = ActionPriority.HIGH,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "TOGGLE_BLUETOOTH",
            name = "Toggle Bluetooth",
            description = "Turn Bluetooth on or off",
            category = ActionCategory.DEVICE,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.BLUETOOTH"),
            optionalParameters = listOf("STATE"),
            priority = ActionPriority.HIGH,
            timeout = 5000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "TOGGLE_FLASHLIGHT",
            name = "Toggle Flashlight",
            description = "Turn flashlight on or off",
            category = ActionCategory.DEVICE,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.CAMERA"),
            optionalParameters = listOf("STATE"),
            priority = ActionPriority.HIGH,
            timeout = 3000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "TAKE_PHOTO",
            name = "Take Photo",
            description = "Take a photo",
            category = ActionCategory.DEVICE,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.CAMERA"),
            optionalParameters = listOf("FLASH", "FRONT_CAMERA"),
            priority = ActionPriority.NORMAL,
            timeout = 15000L,
            status = ActionStatus.ACTIVE
        ))
        
        // Search actions
        register(ActionDefinition(
            id = "SEARCH_WEB",
            name = "Search Web",
            description = "Search the web",
            category = ActionCategory.SEARCH,
            type = ActionType.EXTERNAL,
            requiredEntities = listOf("TEXT"),
            priority = ActionPriority.NORMAL,
            timeout = 30000L,
            status = ActionStatus.ACTIVE
        ))
        
        register(ActionDefinition(
            id = "SEARCH_CONTACTS",
            name = "Search Contacts",
            description = "Search contacts",
            category = ActionCategory.SEARCH,
            type = ActionType.ANDROID,
            requiredPermissions = listOf("android.permission.READ_CONTACTS"),
            requiredEntities = listOf("PERSON"),
            priority = ActionPriority.NORMAL,
            timeout = 10000L,
            status = ActionStatus.ACTIVE
        ))
        
        Logger.i(TAG, "Registered ${actions.size} base actions")
    }
}
