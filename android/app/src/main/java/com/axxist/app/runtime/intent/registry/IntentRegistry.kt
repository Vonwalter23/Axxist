package com.axxist.app.runtime.intent.registry

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.IntentCategory
import com.axxist.app.runtime.intent.model.IntentDefinition
import com.axxist.app.runtime.intent.model.IntentProcessingMethod

/**
 * Registry for all available intents.
 */
class IntentRegistry {
    
    companion object {
        private const val TAG = "IntentRegistry"
        
        @Volatile
        private var instance: IntentRegistry? = null
        
        fun getInstance(): IntentRegistry {
            return instance ?: synchronized(this) {
                instance ?: IntentRegistry().also { instance = it }
            }
        }
    }
    
    private val registeredIntents = mutableMapOf<String, IntentDefinition>()
    private val intentsByCategory = mutableMapOf<IntentCategory, MutableList<IntentDefinition>>()
    private val intentsByProvider = mutableMapOf<IntentProcessingMethod, MutableList<IntentDefinition>>()
    
    init {
        initializeDefaultIntents()
    }
    
    /**
     * Register a new intent.
     */
    fun register(intent: IntentDefinition): Boolean {
        if (registeredIntents.containsKey(intent.id)) {
            Logger.w(TAG, "Intent already registered: ${intent.id}")
            return false
        }
        
        registeredIntents[intent.id] = intent
        
        intentsByCategory.getOrPut(intent.category) { mutableListOf() }.add(intent)
        intentsByProvider.getOrPut(intent.provider) { mutableListOf() }.add(intent)
        
        Logger.d(TAG, "Registered intent: ${intent.id}")
        return true
    }
    
    /**
     * Unregister an intent.
     */
    fun unregister(intentId: String): Boolean {
        val intent = registeredIntents.remove(intentId) ?: return false
        
        intentsByCategory[intent.category]?.remove(intent)
        intentsByProvider[intent.provider]?.remove(intent)
        
        Logger.d(TAG, "Unregistered intent: $intentId")
        return true
    }
    
    /**
     * Get intent by ID.
     */
    fun getIntent(intentId: String): IntentDefinition? {
        return registeredIntents[intentId]
    }
    
    /**
     * Get all registered intents.
     */
    fun getAllIntents(): List<IntentDefinition> {
        return registeredIntents.values.toList()
    }
    
    /**
     * Get intents by category.
     */
    fun getIntentsByCategory(category: IntentCategory): List<IntentDefinition> {
        return intentsByCategory[category]?.toList() ?: emptyList()
    }
    
    /**
     * Get intents by provider.
     */
    fun getIntentsByProvider(provider: IntentProcessingMethod): List<IntentDefinition> {
        return intentsByProvider[provider]?.toList() ?: emptyList()
    }
    
    /**
     * Get active intents.
     */
    fun getActiveIntents(): List<IntentDefinition> {
        return registeredIntents.values.filter {
            it.status == IntentDefinition.IntentStatus.ACTIVE
        }
    }
    
    /**
     * Get intents count.
     */
    fun getIntentCount(): Int = registeredIntents.size
    
    /**
     * Get summary statistics.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "totalIntents" to registeredIntents.size,
        "byCategory" to intentsByCategory.mapValues { it.value.size },
        "byProvider" to intentsByProvider.mapValues { it.value.size },
        "activeIntents" to getActiveIntents().size
    )
    
    /**
     * Initialize default intents.
     */
    private fun initializeDefaultIntents() {
        // Communication intents
        register(IntentDefinition(
            id = "CALL_CONTACT",
            name = "Call Contact",
            description = "Make a phone call to a contact",
            category = IntentCategory.COMMUNICATION,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.CONTACT,
                com.axxist.app.runtime.intent.model.EntityType.PHONE_NUMBER
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Call Juan",
                "Phone to Maria",
                "Llamar a Carlos"
            ),
            patterns = listOf(
                "(call|phone|llamar|dial)\\s+(\\w+)",
                "(marcar|contactar)\\s+(\\w+)"
            )
        ))
        
        register(IntentDefinition(
            id = "SEND_WHATSAPP",
            name = "Send WhatsApp",
            description = "Send a WhatsApp message",
            category = IntentCategory.COMMUNICATION,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.CONTACT
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Send WhatsApp to Juan",
                "Message Maria on WhatsApp",
                "Enviar WhatsApp a Pedro"
            ),
            patterns = listOf(
                "(send|enviar)\\s+whatsapp\\s+to\\s+(\\w+)",
                "whatsapp\\s+(\\w+)"
            )
        ))
        
        register(IntentDefinition(
            id = "SEND_EMAIL",
            name = "Send Email",
            description = "Send an email",
            category = IntentCategory.COMMUNICATION,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.EMAIL
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Send email to john@example.com",
                "Email Maria",
                "Enviar correo a@example.com"
            ),
            patterns = listOf(
                "(send|enviar)\\s+(email|correo)\\s+to\\s+(\\w+)",
                "(email|correo)\\s+(\\w+)"
            )
        ))
        
        // Media intents
        register(IntentDefinition(
            id = "PLAY_SPOTIFY",
            name = "Play Spotify",
            description = "Play music on Spotify",
            category = IntentCategory.MEDIA,
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Play music",
                "Start Spotify",
                "Reproducir música"
            ),
            patterns = listOf(
                "(play|start|reproduce)\\s+(music|spotify|spotifí)",
                "(pon|activa)\\s+(música|spotify)"
            )
        ))
        
        // Navigation intents
        register(IntentDefinition(
            id = "NAVIGATE_TO",
            name = "Navigate To",
            description = "Navigate to a location",
            category = IntentCategory.NAVIGATION,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.LOCATION
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Navigate to downtown",
                "Go to the airport",
                "Ir a casa"
            ),
            patterns = listOf(
                "(navigate|go|ir)\\s+to\\s+(.+)",
                "(cómo\\s+llegar|direcciones)"
            )
        ))
        
        // Productivity intents
        register(IntentDefinition(
            id = "CREATE_REMINDER",
            name = "Create Reminder",
            description = "Create a reminder",
            category = IntentCategory.PRODUCTIVITY,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.TEXT
            ),
            optionalEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.DATE,
                com.axxist.app.runtime.intent.model.EntityType.TIME
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Remind me to buy milk",
                "Create reminder",
                "Recordarme comprar pan"
            ),
            patterns = listOf(
                "(remind|recordar)\\s+(me\\s+)?(.+)",
                "(crear| nuevo)\\s+recordatorio"
            )
        ))
        
        register(IntentDefinition(
            id = "CREATE_ALARM",
            name = "Create Alarm",
            description = "Set an alarm",
            category = IntentCategory.PRODUCTIVITY,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.TIME
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Set alarm for 7 AM",
                "Wake me up at 6",
                "Alarm for 8 o'clock"
            ),
            patterns = listOf(
                "(set|create)\\s+alarm\\s+(for|at)\\s+(.+)",
                "(alarma|despertador)\\s+(para|a)\\s+(.+)"
            )
        ))
        
        // System intents
        register(IntentDefinition(
            id = "OPEN_APP",
            name = "Open App",
            description = "Open an application",
            category = IntentCategory.SYSTEM,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.APPLICATION
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Open WhatsApp",
                "Launch camera",
                "Abrir calculadora"
            ),
            patterns = listOf(
                "(open|launch|abrir)\\s+(.+)",
                "(inicia|ejecuta)\\s+(.+)"
            )
        ))
        
        // Search intents
        register(IntentDefinition(
            id = "SEARCH_WEB",
            name = "Search Web",
            description = "Search the web",
            category = IntentCategory.SEARCH,
            requiredEntities = listOf(
                com.axxist.app.runtime.intent.model.EntityType.TEXT
            ),
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Search for restaurants",
                "Look up weather",
                "Buscar información"
            ),
            patterns = listOf(
                "(search|look\\s+up|buscar)\\s+(for\\s+)?(.+)",
                "(búsqueda|google)\\s+(.+)"
            )
        ))
        
        // Memory intents
        register(IntentDefinition(
            id = "READ_NOTIFICATIONS",
            name = "Read Notifications",
            description = "Read recent notifications",
            category = IntentCategory.MEMORY,
            provider = IntentProcessingMethod.RULE_BASED,
            examples = listOf(
                "Read my notifications",
                "What do I have",
                "Leer notificaciones"
            ),
            patterns = listOf(
                "(read|check)\\s+(my\\s+)?(notifications|messages)",
                "(qué|tengo)\\s+(nuevos|pendiente)"
            )
        ))
        
        // Unknown intent (fallback)
        register(IntentDefinition(
            id = "UNKNOWN",
            name = "Unknown",
            description = "Unknown or unrecognized intent",
            category = IntentCategory.CUSTOM,
            provider = IntentProcessingMethod.AI_BASED,
            status = IntentDefinition.IntentStatus.ACTIVE
        ))
        
        Logger.d(TAG, "Initialized ${registeredIntents.size} default intents")
    }
}
