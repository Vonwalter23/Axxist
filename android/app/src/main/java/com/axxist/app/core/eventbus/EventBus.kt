package com.axxist.app.core.eventbus

/**
 * Base event interface for the EventBus system.
 */
interface AxxistEvent

/**
 * Standard events for the application lifecycle.
 */
sealed class AppEvent : AxxistEvent {
    data object Started : AppEvent()
    data object Stopped : AppEvent()
    data object Paused : AppEvent()
    data object Resumed : AppEvent()
}

/**
 * Module lifecycle events.
 */
sealed class ModuleEvent : AxxistEvent {
    data class Loaded(val moduleName: String) : ModuleEvent()
    data class Error(val moduleName: String, val error: Throwable) : ModuleEvent()
    data class Unloaded(val moduleName: String) : ModuleEvent()
}

/**
 * Configuration events.
 */
sealed class ConfigEvent : AxxistEvent {
    data object Updated : ConfigEvent()
    data class KeyUpdated(val key: String) : ConfigEvent()
}

/**
 * Event type definitions.
 */
object EventTypes {
    const val APP_STARTED = "app_started"
    const val APP_STOPPED = "app_stopped"
    const val APP_PAUSED = "app_paused"
    const val APP_RESUMED = "app_resumed"
    const val MODULE_LOADED = "module_loaded"
    const val MODULE_ERROR = "module_error"
    const val MODULE_UNLOADED = "module_unloaded"
    const val CONFIG_UPDATED = "config_updated"
    const val CONFIG_KEY_UPDATED = "config_key_updated"
}
