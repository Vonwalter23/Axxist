package com.axxist.app.core.eventbus

import com.axxist.app.runtime.state.RuntimeState

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
 * Runtime lifecycle events.
 */
sealed class RuntimeEvent : AxxistEvent {
    data object Started : RuntimeEvent()
    data object Stopped : RuntimeEvent()
    data class StateChanged(
        val fromState: RuntimeState,
        val toState: RuntimeState,
        val errorMessage: String? = null
    ) : RuntimeEvent()
    data class Error(val message: String) : RuntimeEvent()
    data object HealthCheck : RuntimeEvent()
    data object BootCompleted : RuntimeEvent()
}

/**
 * Service lifecycle events.
 */
sealed class ServiceEvent : AxxistEvent {
    data object Created : ServiceEvent()
    data object Destroyed : ServiceEvent()
    data object ForegroundStarted : ServiceEvent()
    data object ForegroundStopped : ServiceEvent()
}

/**
 * Event type definitions.
 */
object EventTypes {
    // App events
    const val APP_STARTED = "app_started"
    const val APP_STOPPED = "app_stopped"
    const val APP_PAUSED = "app_paused"
    const val APP_RESUMED = "app_resumed"
    
    // Module events
    const val MODULE_LOADED = "module_loaded"
    const val MODULE_ERROR = "module_error"
    const val MODULE_UNLOADED = "module_unloaded"
    
    // Config events
    const val CONFIG_UPDATED = "config_updated"
    const val CONFIG_KEY_UPDATED = "config_key_updated"
    
    // Runtime events
    const val RUNTIME_STARTED = "runtime_started"
    const val RUNTIME_STOPPED = "runtime_stopped"
    const val RUNTIME_STATE_CHANGED = "runtime_state_changed"
    const val RUNTIME_ERROR = "runtime_error"
    const val RUNTIME_HEALTH_CHECK = "runtime_health_check"
    const val BOOT_COMPLETED = "boot_completed"
    
    // Service events
    const val SERVICE_CREATED = "service_created"
    const val SERVICE_DESTROYED = "service_destroyed"
    const val SERVICE_FOREGROUND_STARTED = "service_foreground_started"
    const val SERVICE_FOREGROUND_STOPPED = "service_foreground_stopped"
}
