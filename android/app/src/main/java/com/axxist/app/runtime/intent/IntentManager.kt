package com.axxist.app.runtime.intent

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.IntentEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.evaluator.ConfidenceEvaluator
import com.axxist.app.runtime.intent.extractor.EntityExtractor
import com.axxist.app.runtime.intent.model.ConfidenceLevel
import com.axxist.app.runtime.intent.model.Intent
import com.axxist.app.runtime.intent.model.IntentResult
import com.axxist.app.runtime.intent.model.ValidationResult
import com.axxist.app.runtime.intent.registry.IntentRegistry
import com.axxist.app.runtime.intent.router.IntentDiagnosticsCollector
import com.axxist.app.runtime.intent.router.IntentRouter
import com.axxist.app.runtime.intent.state.IntentState
import com.axxist.app.runtime.intent.state.IntentStateManager
import com.axxist.app.runtime.intent.validator.IntentValidator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * IntentManager - Main coordinator for the Intent Framework.
 * 
 * Responsibilities:
 * - Receive text input from ConversationManager
 * - Coordinate all intent processing components
 * - Never execute actions directly
 * 
 * Flow:
 * Input Text → Entity Extraction → Intent Matching → Validation → Result
 */
class IntentManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "IntentManager"
        
        @Volatile
        private var instance: IntentManager? = null
        
        fun initialize(context: Context): IntentManager {
            return instance ?: synchronized(this) {
                instance ?: IntentManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): IntentManager {
            return instance ?: throw IllegalStateException("IntentManager not initialized")
        }
    }
    
    private val stateManager = IntentStateManager.getInstance()
    private val registry = IntentRegistry.getInstance()
    private val validator = IntentValidator.getInstance()
    private val extractor = EntityExtractor.getInstance()
    private val evaluator = ConfidenceEvaluator.getInstance()
    private val router = IntentRouter.getInstance()
    private val diagnosticsCollector = IntentDiagnosticsCollector.getInstance()
    
    private var isInitialized = false
    private val listeners = mutableListOf<IntentListener>()
    
    /**
     * Listener interface for intent events.
     */
    interface IntentListener {
        fun onIntentAnalyzed(result: IntentResult)
        fun onIntentReady(result: IntentResult)
        fun onIntentFailed(result: IntentResult)
        fun onError(error: String)
    }
    
    /**
     * Initialize the Intent Framework.
     */
    fun initialize(): Boolean {
        Logger.d(TAG, "Initializing IntentManager...")
        
        if (isInitialized) {
            Logger.w(TAG, "IntentManager already initialized")
            return true
        }
        
        isInitialized = true
        Logger.d(TAG, "IntentManager initialized successfully")
        Logger.d(TAG, "Registered intents: ${registry.getIntentCount()}")
        
        return true
    }
    
    /**
     * Process text input and return intent result.
     */
    suspend fun processInput(text: String): IntentResult = withContext(Dispatchers.Default) {
        Logger.d(TAG, "Processing input: ${text.take(50)}...")
        
        if (!isInitialized) {
            Logger.e(TAG, "IntentManager not initialized")
            return@withContext IntentResult.empty(
                IntentDiagnosticsCollector.getInstance().apply {
                    recordError("IntentManager not initialized")
                }.build()
            )
        }
        
        diagnosticsCollector.startAnalysis()
        EventBusManager.emitIntentEvent(IntentEvent.AnalysisStarted)
        
        if (!stateManager.startAnalysis()) {
            Logger.e(TAG, "Cannot start analysis - invalid state")
            return@withContext IntentResult.empty(diagnosticsCollector.build())
        }
        
        try {
            // Step 1: Extract entities
            val entities = extractor.extract(text)
            diagnosticsCollector.setEntitiesExtracted(entities.size)
            EventBusManager.emitIntentEvent(IntentEvent.EntityExtracted(entities))
            
            // Step 2: Select processing method
            val selectedMethod = router.autoSelectMethod(text)
            diagnosticsCollector.setMethod(selectedMethod)
            
            // Step 3: Match intent (placeholder - rule-based matching)
            val intent = matchIntent(text, entities, selectedMethod)
            diagnosticsCollector.setProvider("IntentRegistry")
            
            if (intent == null) {
                Logger.w(TAG, "No intent matched for input")
                stateManager.fail()
                EventBusManager.emitIntentEvent(IntentEvent.Failed("No intent matched"))
                
                val result = IntentResult.empty(diagnosticsCollector.build())
                notifyListenersOfFailure(result)
                return@withContext result
            }
            
            stateManager.matched()
            EventBusManager.emitIntentEvent(IntentEvent.Matched(intent.id, intent.confidence))
            
            // Step 4: Validate intent
            diagnosticsCollector.startValidation()
            val validationResult = validator.validate(intent)
            EventBusManager.emitIntentEvent(IntentEvent.Validated(intent.id, validationResult.isValid))
            
            if (!stateManager.startValidation()) {
                Logger.e(TAG, "Cannot start validation - invalid state")
            }
            
            // Step 5: Evaluate confidence
            val confidenceResult = evaluator.evaluate(intent)
            val finalConfidence = confidenceResult.confidence
            
            EventBusManager.emitIntentEvent(
                IntentEvent.ConfidenceUpdated(intent.id, finalConfidence, confidenceResult.level)
            )
            
            // Update intent with final confidence
            val finalIntent = intent.copy(confidence = finalConfidence)
            
            if (!stateManager.ready()) {
                Logger.w(TAG, "Could not transition to ready state")
            }
            
            EventBusManager.emitIntentEvent(IntentEvent.Ready(finalIntent.id))
            
            val result = if (validationResult.isValid) {
                IntentResult.success(finalIntent, validationResult, diagnosticsCollector.build())
            } else {
                IntentResult.failure(finalIntent, validationResult, diagnosticsCollector.build())
            }
            
            notifyListenersOfResult(result)
            return@withContext result
            
        } catch (e: Exception) {
            Logger.e(TAG, "Error processing intent", e)
            diagnosticsCollector.recordError(e.message ?: "Unknown error")
            stateManager.error(e.message ?: "Unknown error")
            EventBusManager.emitIntentEvent(IntentEvent.Error(e.message ?: "Unknown error"))
            
            val result = IntentResult.failure(
                intent = null,
                validation = ValidationResult.invalid(errors = listOf(e.message ?: "Unknown error")),
                diagnostics = diagnosticsCollector.build()
            )
            
            notifyListenersOfFailure(result)
            return@withContext result
        }
    }
    
    /**
     * Match intent using rule-based approach.
     */
    private fun matchIntent(
        text: String,
        entities: List<com.axxist.app.runtime.intent.model.Entity>,
        method: com.axxist.app.runtime.intent.model.IntentProcessingMethod
    ): Intent? {
        val lowercaseText = text.lowercase()
        
        // Try to match based on keywords
        val matchedIntent = when {
            // Communication intents
            containsAny(lowercaseText, "call", "llamar", "phone", "marcar") ->
                registry.getIntent("CALL_CONTACT")
            containsAny(lowercaseText, "whatsapp", "whats") ->
                registry.getIntent("SEND_WHATSAPP")
            containsAny(lowercaseText, "email", "correo", "mail") ->
                registry.getIntent("SEND_EMAIL")
            
            // Media intents
            containsAny(lowercaseText, "play", "reproducir", "spotify", "music", "música") ->
                registry.getIntent("PLAY_SPOTIFY")
            
            // Navigation intents
            containsAny(lowercaseText, "navigate", "ir a", "go to", "directions", "cómo llegar") ->
                registry.getIntent("NAVIGATE_TO")
            
            // Productivity intents
            containsAny(lowercaseText, "remind", "recordar", "reminder", "recuerdame") ->
                registry.getIntent("CREATE_REMINDER")
            containsAny(lowercaseText, "alarm", "alarma", "despertador", "wake me up") ->
                registry.getIntent("CREATE_ALARM")
            
            // System intents
            containsAny(lowercaseText, "open", "abrir", "launch", "iniciar", "ejecuta") ->
                registry.getIntent("OPEN_APP")
            
            // Search intents
            containsAny(lowercaseText, "search", "buscar", "look up", "google") ->
                registry.getIntent("SEARCH_WEB")
            
            // Memory intents
            containsAny(lowercaseText, "notification", "notificación", "qué tengo", "read") ->
                registry.getIntent("READ_NOTIFICATIONS")
            
            else -> null
        }
        
        if (matchedIntent == null) {
            return createUnknownIntent(text, method)
        }
        
        // Calculate confidence based on entity presence
        var confidence = 0.7f
        if (entities.isNotEmpty()) {
            confidence += 0.1f
        }
        
        return Intent(
            id = matchedIntent.id,
            name = matchedIntent.name,
            category = matchedIntent.category,
            confidence = confidence.coerceIn(0f, 1f),
            entities = entities,
            provider = method,
            originalText = text
        )
    }
    
    /**
     * Create an unknown intent result.
     */
    private fun createUnknownIntent(
        text: String,
        method: com.axxist.app.runtime.intent.model.IntentProcessingMethod
    ): Intent {
        return Intent(
            id = "UNKNOWN",
            name = "Unknown",
            category = com.axxist.app.runtime.intent.model.IntentCategory.CUSTOM,
            confidence = 0.3f,
            entities = extractor.let { kotlinx.coroutines.runBlocking { it.extract(text) } },
            provider = method,
            originalText = text
        )
    }
    
    /**
     * Check if text contains any of the keywords.
     */
    private fun containsAny(text: String, vararg keywords: String): Boolean {
        return keywords.any { text.contains(it) }
    }
    
    /**
     * Get current state.
     */
    fun getState(): IntentState = stateManager.getCurrentState()
    
    /**
     * Check if framework is ready.
     */
    fun isReady(): Boolean = isInitialized && stateManager.getCurrentState().isReady()
    
    /**
     * Get available intents.
     */
    fun getAvailableIntents(): List<com.axxist.app.runtime.intent.model.IntentDefinition> {
        return registry.getAllIntents()
    }
    
    /**
     * Get intent count.
     */
    fun getIntentCount(): Int = registry.getIntentCount()
    
    /**
     * Add listener.
     */
    fun addListener(listener: IntentListener) {
        listeners.add(listener)
    }
    
    /**
     * Remove listener.
     */
    fun removeListener(listener: IntentListener) {
        listeners.remove(listener)
    }
    
    /**
     * Get summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isInitialized" to isInitialized,
        "intentCount" to registry.getIntentCount(),
        "availableMethods" to router.getAvailableMethods().map { it.name }
    )
    
    /**
     * Reset the framework.
     */
    fun reset() {
        Logger.d(TAG, "Resetting IntentManager...")
        stateManager.reset()
        diagnosticsCollector.reset()
        listeners.clear()
        Logger.d(TAG, "IntentManager reset complete")
    }
    
    private fun notifyListenersOfResult(result: IntentResult) {
        listeners.forEach { listener ->
            try {
                if (result.isReadyForExecution) {
                    listener.onIntentReady(result)
                } else {
                    listener.onIntentAnalyzed(result)
                }
            } catch (e: Exception) {
                Logger.e(TAG, "Error notifying listener", e)
            }
        }
    }
    
    private fun notifyListenersOfFailure(result: IntentResult) {
        listeners.forEach { listener ->
            try {
                listener.onIntentFailed(result)
            } catch (e: Exception) {
                Logger.e(TAG, "Error notifying listener", e)
            }
        }
    }
}
