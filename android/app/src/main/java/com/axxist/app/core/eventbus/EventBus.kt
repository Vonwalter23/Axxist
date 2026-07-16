package com.axxist.app.core.eventbus

import com.axxist.app.runtime.state.RuntimeState
import com.axxist.app.runtime.audio.state.AudioState
import com.axxist.app.runtime.wakeword.state.WakeWordState
import com.axxist.app.runtime.conversation.state.ConversationState
import com.axxist.app.runtime.ai.state.AIState
import com.axxist.app.runtime.intent.model.ConfidenceLevel
import com.axxist.app.runtime.intent.model.Entity
import com.axxist.app.runtime.intent.model.IntentState

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
 * Audio lifecycle events.
 */
sealed class AudioEvent : AxxistEvent {
    data object Initializing : AudioEvent()
    data object Started : AudioEvent()
    data object Stopped : AudioEvent()
    data class StateChanged(
        val fromState: AudioState,
        val toState: AudioState,
        val errorMessage: String? = null
    ) : AudioEvent()
    data class Error(val message: String) : AudioEvent()
    data object VoiceActivityDetected : AudioEvent()
    data object VoiceActivityEnded : AudioEvent()
}

/**
 * Wake word lifecycle events.
 */
sealed class WakeWordEvent : AxxistEvent {
    data object Initializing : WakeWordEvent()
    data object Ready : WakeWordEvent()
    data object Listening : WakeWordEvent()
    data object Stopped : WakeWordEvent()
    data class Detected(val keyword: String, val confidence: Float) : WakeWordEvent()
    data class StateChanged(
        val fromState: WakeWordState,
        val toState: WakeWordState,
        val errorMessage: String? = null
    ) : WakeWordEvent()
    data class Error(val message: String) : WakeWordEvent()
}

/**
 * Conversation lifecycle events.
 */
sealed class ConversationEvent : AxxistEvent {
    data class Started(val sessionId: String) : ConversationEvent()
    data class UserMessageReceived(val messageId: String, val text: String) : ConversationEvent()
    data object ProcessingStarted : ConversationEvent()
    data class AssistantResponseReady(val messageId: String, val text: String) : ConversationEvent()
    data object Ended : ConversationEvent()
    data class StateChanged(
        val fromState: ConversationState,
        val toState: ConversationState,
        val errorMessage: String? = null
    ) : ConversationEvent()
    data class Error(val message: String) : ConversationEvent()
}

/**
 * AI lifecycle events.
 */
sealed class AIEvent : AxxistEvent {
    data object RequestStarted : AIEvent()
    data class ProviderSelected(val providerType: String) : AIEvent()
    data class ResponseReceived(val content: String, val provider: String, val tokens: Int) : AIEvent()
    data class StateChanged(
        val fromState: AIState,
        val toState: AIState,
        val errorMessage: String? = null
    ) : AIEvent()
    data class Error(val message: String) : AIEvent()
}

/**
 * Intent Framework lifecycle events.
 */
sealed class IntentEvent : AxxistEvent {
    data object AnalysisStarted : IntentEvent()
    data class Matched(val intentId: String, val confidence: Float) : IntentEvent()
    data class Validated(val intentId: String, val isValid: Boolean) : IntentEvent()
    data class EntityExtracted(val entities: List<Entity>) : IntentEvent()
    data class ConfidenceUpdated(val intentId: String, val confidence: Float, val level: ConfidenceLevel) : IntentEvent()
    data class Ready(val intentId: String) : IntentEvent()
    data class Failed(val reason: String) : IntentEvent()
    data class Error(val message: String) : IntentEvent()
    data class StateChanged(
        val fromState: IntentState,
        val toState: IntentState,
        val errorMessage: String? = null
    ) : IntentEvent()
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
    
    // Audio events
    const val AUDIO_INITIALIZING = "audio_initializing"
    const val AUDIO_STARTED = "audio_starting"
    const val AUDIO_STOPPED = "audio_stopped"
    const val AUDIO_STATE_CHANGED = "audio_state_changed"
    const val AUDIO_ERROR = "audio_error"
    const val VOICE_ACTIVITY_DETECTED = "voice_activity_detected"
    const val VOICE_ACTIVITY_ENDED = "voice_activity_ended"
    
    // Wake word events
    const val WAKE_WORD_INITIALIZING = "wake_word_initializing"
    const val WAKE_WORD_READY = "wake_word_ready"
    const val WAKE_WORD_LISTENING = "wake_word_listening"
    const val WAKE_WORD_DETECTED = "wake_word_detected"
    const val WAKE_WORD_STOPPED = "wake_word_stopped"
    const val WAKE_WORD_ERROR = "wake_word_error"
    const val WAKE_WORD_STATE_CHANGED = "wake_word_state_changed"
    
    // Conversation events
    const val CONVERSATION_STARTED = "conversation_started"
    const val USER_MESSAGE_RECEIVED = "user_message_received"
    const val PROCESSING_STARTED = "processing_started"
    const val ASSISTANT_RESPONSE_READY = "assistant_response_ready"
    const val CONVERSATION_ENDED = "conversation_ended"
    const val CONVERSATION_STATE_CHANGED = "conversation_state_changed"
    const val CONVERSATION_ERROR = "conversation_error"
    
    // AI events
    const val AI_REQUEST_STARTED = "ai_request_started"
    const val AI_PROVIDER_SELECTED = "ai_provider_selected"
    const val AI_RESPONSE_RECEIVED = "ai_response_received"
    const val AI_STATE_CHANGED = "ai_state_changed"
    const val AI_ERROR = "ai_error"
    
    // Intent events
    const val INTENT_ANALYSIS_STARTED = "intent_analysis_started"
    const val INTENT_MATCHED = "intent_matched"
    const val INTENT_VALIDATED = "intent_validated"
    const val ENTITY_EXTRACTED = "entity_extracted"
    const val CONFIDENCE_UPDATED = "confidence_updated"
    const val INTENT_READY = "intent_ready"
    const val INTENT_FAILED = "intent_failed"
    const val INTENT_ERROR = "intent_error"
    const val INTENT_STATE_CHANGED = "intent_state_changed"
}
