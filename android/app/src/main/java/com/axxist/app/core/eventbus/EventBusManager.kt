package com.axxist.app.core.eventbus

import com.axxist.app.core.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * EventBus Manager - Internal event system.
 * 
 * Provides a centralized event system for communication between modules.
 */
object EventBusManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val _events = MutableSharedFlow<AxxistEvent>(replay = 0, extraBufferCapacity = 64)
    private val events: SharedFlow<AxxistEvent> = _events
    
    private val listeners = mutableMapOf<String, MutableList<(AxxistEvent) -> Unit>>()
    
    private const val TAG = "EventBus"

    /**
     * Initialize the EventBus.
     */
    fun initialize() {
        Logger.d(TAG, "EventBus initialized")
        emit(AppEvent.Started)
    }

    /**
     * Subscribe to all events.
     */
    fun subscribe(callback: (AxxistEvent) -> Unit) {
        scope.launch {
            events.collect { event ->
                try {
                    callback(event)
                } catch (e: Exception) {
                    Logger.e(TAG, "Error in event callback", e)
                }
            }
        }
    }

    /**
     * Subscribe to a specific event type.
     */
    fun <T : AxxistEvent> subscribe(eventType: String, callback: (T) -> Unit) {
        listeners.getOrPut(eventType) { mutableListOf() }.add { event ->
            @Suppress("UNCHECKED_CAST")
            callback(event as T)
        }
        
        scope.launch {
            events.collect { event ->
                val eventName = getEventName(event)
                if (eventName == eventType) {
                    try {
                        @Suppress("UNCHECKED_CAST")
                        callback(event as T)
                    } catch (e: Exception) {
                        Logger.e(TAG, "Error in event callback for $eventType", e)
                    }
                }
            }
        }
    }

    /**
     * Emit an event.
     */
    fun emit(event: AxxistEvent) {
        scope.launch {
            Logger.d(TAG, "Emitting event: ${getEventName(event)}")
            _events.emit(event)
            
            val eventName = getEventName(event)
            listeners[eventName]?.forEach { callback ->
                try {
                    callback(event)
                } catch (e: Exception) {
                    Logger.e(TAG, "Error in direct listener for $eventName", e)
                }
            }
        }
    }

    /**
     * Emit an app event.
     */
    fun emitAppEvent(event: AppEvent) {
        emit(event)
    }

    /**
     * Emit a module event.
     */
    fun emitModuleEvent(event: ModuleEvent) {
        emit(event)
    }

    /**
     * Emit a config event.
     */
    fun emitConfigEvent(event: ConfigEvent) {
        emit(event)
    }

    /**
     * Emit a runtime event.
     */
    fun emitRuntimeEvent(event: RuntimeEvent) {
        emit(event)
    }

    /**
     * Emit a service event.
     */
    fun emitServiceEvent(event: ServiceEvent) {
        emit(event)
    }

    /**
     * Get the event name for logging purposes.
     */
    private fun getEventName(event: AxxistEvent): String {
        return when (event) {
            is AppEvent.Started -> EventTypes.APP_STARTED
            is AppEvent.Stopped -> EventTypes.APP_STOPPED
            is AppEvent.Paused -> EventTypes.APP_PAUSED
            is AppEvent.Resumed -> EventTypes.APP_RESUMED
            is ModuleEvent.Loaded -> EventTypes.MODULE_LOADED
            is ModuleEvent.Error -> EventTypes.MODULE_ERROR
            is ModuleEvent.Unloaded -> EventTypes.MODULE_UNLOADED
            is ConfigEvent.Updated -> EventTypes.CONFIG_UPDATED
            is ConfigEvent.KeyUpdated -> EventTypes.CONFIG_KEY_UPDATED
            is RuntimeEvent.Started -> EventTypes.RUNTIME_STARTED
            is RuntimeEvent.Stopped -> EventTypes.RUNTIME_STOPPED
            is RuntimeEvent.StateChanged -> EventTypes.RUNTIME_STATE_CHANGED
            is RuntimeEvent.Error -> EventTypes.RUNTIME_ERROR
            is RuntimeEvent.HealthCheck -> EventTypes.RUNTIME_HEALTH_CHECK
            is RuntimeEvent.BootCompleted -> EventTypes.BOOT_COMPLETED
            is ServiceEvent.Created -> EventTypes.SERVICE_CREATED
            is ServiceEvent.Destroyed -> EventTypes.SERVICE_DESTROYED
            is ServiceEvent.ForegroundStarted -> EventTypes.SERVICE_FOREGROUND_STARTED
            is ServiceEvent.ForegroundStopped -> EventTypes.SERVICE_FOREGROUND_STOPPED
            else -> event.javaClass.simpleName
        }
    }

    /**
     * Clear all listeners.
     */
    fun clearListeners() {
        listeners.clear()
        Logger.d(TAG, "All listeners cleared")
    }

    /**
     * Shutdown the EventBus.
     */
    fun shutdown() {
        emit(AppEvent.Stopped)
        clearListeners()
        Logger.d(TAG, "EventBus shutdown")
    }
}
