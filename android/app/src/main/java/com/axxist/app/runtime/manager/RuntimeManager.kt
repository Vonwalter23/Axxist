package com.axxist.app.runtime.manager

import android.content.Context
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.RuntimeEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.service.AssistantService
import com.axxist.app.runtime.state.RuntimeState
import com.axxist.app.runtime.state.RuntimeStateManager
import com.axxist.app.runtime.audio.AudioManager
import com.axxist.app.runtime.audio.state.AudioState
import com.axxist.app.runtime.wakeword.WakeWordManager
import com.axxist.app.runtime.wakeword.state.WakeWordState
import com.axxist.app.runtime.conversation.ConversationManager
import com.axxist.app.runtime.conversation.state.ConversationState
import com.axxist.app.runtime.intent.IntentManager
import com.axxist.app.runtime.intent.state.IntentState
import com.axxist.app.runtime.action.ActionManager
import com.axxist.app.runtime.action.model.ActionState

/**
 * RuntimeManager - Main coordinator for the Axxist Runtime.
 * 
 * Responsible for:
 * - Starting the Runtime
 * - Stopping the Runtime
 * - Restarting the Runtime
 * - Querying state
 * - Registering modules
 * - Managing Audio subsystem
 * - Managing WakeWord subsystem
 * - Managing Conversation subsystem
 * - Managing Intent subsystem
 * - Managing Action subsystem
 * 
 * No other module should start or stop the service directly.
 */
class RuntimeManager private constructor(private val context: Context) {
    
    companion object {
        private const val TAG = "RuntimeManager"
        
        @Volatile
        private var instance: RuntimeManager? = null
        
        fun initialize(context: Context): RuntimeManager {
            return instance ?: synchronized(this) {
                instance ?: RuntimeManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): RuntimeManager {
            return instance ?: throw IllegalStateException("RuntimeManager not initialized")
        }
    }
    
    private val stateManager = RuntimeStateManager.getInstance()
    private val registeredModules = mutableMapOf<String, ModuleInfo>()
    private var serviceStarted = false
    private var audioManager: AudioManager? = null
    private var wakeWordManager: WakeWordManager? = null
    private var conversationManager: ConversationManager? = null
    private var intentManager: IntentManager? = null
    private var actionManager: ActionManager? = null
    
    data class ModuleInfo(
        val name: String,
        val version: String,
        val status: ModuleStatus = ModuleStatus.REGISTERED
    )
    
    enum class ModuleStatus {
        REGISTERED,
        INITIALIZED,
        RUNNING,
        ERROR,
        STOPPED
    }
    
    init {
        Logger.d(TAG, "RuntimeManager initialized")
    }
    
    // ========== Audio Subsystem ==========
    
    /**
     * Initialize Audio subsystem.
     */
    fun initializeAudio(): AudioManager {
        if (audioManager == null) {
            audioManager = AudioManager.initialize(context)
        }
        return audioManager!!
    }
    
    /**
     * Get AudioManager instance.
     */
    fun getAudioManager(): AudioManager {
        return audioManager ?: initializeAudio()
    }
    
    /**
     * Check if audio is active.
     */
    fun isAudioActive(): Boolean {
        return audioManager?.isActive() ?: false
    }
    
    /**
     * Get audio state.
     */
    fun getAudioState(): AudioState {
        return audioManager?.getState() ?: AudioState.IDLE
    }
    
    // ========== WakeWord Subsystem ==========
    
    /**
     * Initialize WakeWord subsystem.
     */
    fun initializeWakeWord(): WakeWordManager {
        if (wakeWordManager == null) {
            wakeWordManager = WakeWordManager.initialize(context)
        }
        return wakeWordManager!!
    }
    
    /**
     * Get WakeWordManager instance.
     */
    fun getWakeWordManager(): WakeWordManager {
        return wakeWordManager ?: initializeWakeWord()
    }
    
    /**
     * Check if wake word detection is active.
     */
    fun isWakeWordActive(): Boolean {
        return wakeWordManager?.isActive() ?: false
    }
    
    /**
     * Get wake word state.
     */
    fun getWakeWordState(): WakeWordState {
        return wakeWordManager?.getState() ?: WakeWordState.DISABLED
    }
    
    // ========== Conversation Subsystem ==========
    
    /**
     * Initialize Conversation subsystem.
     */
    fun initializeConversation(): ConversationManager {
        if (conversationManager == null) {
            conversationManager = ConversationManager.initialize(context)
        }
        return conversationManager!!
    }
    
    /**
     * Get ConversationManager instance.
     */
    fun getConversationManager(): ConversationManager {
        return conversationManager ?: initializeConversation()
    }
    
    /**
     * Check if conversation is active.
     */
    fun isConversationActive(): Boolean {
        return conversationManager?.isActive() ?: false
    }
    
    /**
     * Get conversation state.
     */
    fun getConversationState(): ConversationState {
        return conversationManager?.getState() ?: ConversationState.IDLE
    }
    
    // ========== Intent Subsystem ==========
    
    /**
     * Initialize Intent subsystem.
     */
    fun initializeIntent(): IntentManager {
        if (intentManager == null) {
            intentManager = IntentManager.initialize(context)
        }
        return intentManager!!
    }
    
    /**
     * Get IntentManager instance.
     */
    fun getIntentManager(): IntentManager {
        return intentManager ?: initializeIntent()
    }
    
    /**
     * Check if intent processing is ready.
     */
    fun isIntentReady(): Boolean {
        return intentManager?.isReady() ?: false
    }
    
    /**
     * Get intent state.
     */
    fun getIntentState(): IntentState {
        return intentManager?.getState() ?: IntentState.IDLE
    }
    
    // ========== Action Subsystem ==========
    
    /**
     * Initialize Action subsystem.
     */
    fun initializeAction(): ActionManager {
        if (actionManager == null) {
            actionManager = ActionManager.initialize(context)
        }
        return actionManager!!
    }
    
    /**
     * Get ActionManager instance.
     */
    fun getActionManager(): ActionManager {
        return actionManager ?: initializeAction()
    }
    
    /**
     * Check if action framework is ready.
     */
    fun isActionReady(): Boolean {
        return actionManager?.isReady() ?: false
    }
    
    /**
     * Start the Runtime.
     */
    fun start(): Boolean {
        Logger.d(TAG, "Starting Runtime...")
        
        if (stateManager.getCurrentState().isActive()) {
            Logger.w(TAG, "Runtime already running")
            return true
        }
        
        if (!stateManager.start()) {
            Logger.e(TAG, "Failed to start Runtime - invalid state transition")
            return false
        }
        
        try {
            // Initialize audio subsystem
            initializeAudio()
            
            AssistantService.startService(context)
            serviceStarted = true
            
            // Mark as running after service starts
            stateManager.running()
            
            EventBusManager.emitRuntimeEvent(RuntimeEvent.Started)
            Logger.d(TAG, "Runtime started successfully")
            return true
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to start service", e)
            stateManager.error("Service start failed: ${e.message}")
            return false
        }
    }
    
    /**
     * Stop the Runtime.
     */
    fun stop(): Boolean {
        Logger.d(TAG, "Stopping Runtime...")
        
        if (!stateManager.getCurrentState().isActive() && 
            stateManager.getCurrentState() != RuntimeState.STARTING) {
            Logger.w(TAG, "Runtime not running")
            return true
        }
        
        if (!stateManager.stop()) {
            Logger.e(TAG, "Failed to stop Runtime - invalid state transition")
            return false
        }
        
        try {
            // Stop conversation if active
            conversationManager?.let { conv ->
                try {
                    conv.endConversation()
                } catch (e: Exception) {
                    Logger.e(TAG, "Error stopping conversation", e)
                }
            }
            
            // Stop wake word if active
            wakeWordManager?.let { ww ->
                try {
                    kotlinx.coroutines.runBlocking {
                        ww.stop()
                    }
                } catch (e: Exception) {
                    Logger.e(TAG, "Error stopping wake word", e)
                }
            }
            
            // Stop audio if active
            audioManager?.let { audio ->
                try {
                    kotlinx.coroutines.runBlocking {
                        audio.stop()
                    }
                } catch (e: Exception) {
                    Logger.e(TAG, "Error stopping audio", e)
                }
            }
            
            if (serviceStarted) {
                AssistantService.stopService(context)
                serviceStarted = false
            }
            
            stateManager.stopComplete()
            EventBusManager.emitRuntimeEvent(RuntimeEvent.Stopped)
            Logger.d(TAG, "Runtime stopped successfully")
            return true
        } catch (e: Exception) {
            Logger.e(TAG, "Failed to stop service", e)
            stateManager.error("Service stop failed: ${e.message}")
            return false
        }
    }
    
    /**
     * Restart the Runtime.
     */
    fun restart(): Boolean {
        Logger.d(TAG, "Restarting Runtime...")
        stop()
        Thread.sleep(500) // Brief pause
        return start()
    }
    
    /**
     * Get current runtime state.
     */
    fun getState(): RuntimeState = stateManager.getCurrentState()
    
    /**
     * Check if Runtime is active.
     */
    fun isActive(): Boolean = stateManager.getCurrentState().isActive()
    
    /**
     * Register a module with the Runtime.
     */
    fun registerModule(name: String, version: String): Boolean {
        if (registeredModules.containsKey(name)) {
            Logger.w(TAG, "Module already registered: $name")
            return false
        }
        
        registeredModules[name] = ModuleInfo(name, version)
        Logger.d(TAG, "Module registered: $name v$version")
        return true
    }
    
    /**
     * Update module status.
     */
    fun updateModuleStatus(name: String, status: ModuleStatus): Boolean {
        val module = registeredModules[name] ?: run {
            Logger.w(TAG, "Module not found: $name")
            return false
        }
        
        registeredModules[name] = module.copy(status = status)
        Logger.d(TAG, "Module $name status updated: $status")
        return true
    }
    
    /**
     * Get registered modules.
     */
    fun getRegisteredModules(): Map<String, ModuleInfo> = registeredModules.toMap()
    
    /**
     * Get module info.
     */
    fun getModuleInfo(name: String): ModuleInfo? = registeredModules[name]
    
    /**
     * Called when the service starts.
     */
    fun onServiceStarted() {
        Logger.d(TAG, "Service started callback")
    }
    
    /**
     * Called when the service stops.
     */
    fun onServiceStopped() {
        Logger.d(TAG, "Service stopped callback")
    }
    
    /**
     * Get runtime uptime in milliseconds.
     */
    fun getUptime(): Long = stateManager.getUptime()
    
    /**
     * Get runtime summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "state" to stateManager.getCurrentState().name,
        "isActive" to isActive(),
        "uptime" to getUptime(),
        "modulesCount" to registeredModules.size,
        "serviceStarted" to serviceStarted,
        "audioState" to getAudioState().name,
        "audioActive" to isAudioActive(),
        "wakeWordState" to getWakeWordState().name,
        "wakeWordActive" to isWakeWordActive(),
        "conversationState" to getConversationState().name,
        "conversationActive" to isConversationActive(),
        "intentState" to getIntentState().name,
        "intentReady" to isIntentReady(),
        "actionReady" to isActionReady()
    )
    
    /**
     * Reset the Runtime (for testing).
     */
    fun reset() {
        Logger.d(TAG, "Resetting Runtime...")
        stop()
        actionManager?.reset()
        intentManager?.reset()
        conversationManager?.reset()
        wakeWordManager?.reset()
        audioManager?.reset()
        stateManager.reset()
        registeredModules.clear()
        serviceStarted = false
    }
}
