package com.axxist.app.core.capability

import com.axxist.app.core.logger.Logger

/**
 * Capability Manager - Structure to register future capabilities.
 * 
 * This module creates the infrastructure for capability management.
 * No capabilities are implemented yet - only the structure is prepared.
 * 
 * Capabilities will be registered when their respective modules are implemented.
 */
object CapabilityManager {
    
    private const val TAG = "CapabilityManager"
    
    private val registeredCapabilities = mutableMapOf<String, Capability>()
    private val capabilityListeners = mutableListOf<(String, CapabilityStatus) -> Unit>()
    
    /**
     * Capability status.
     */
    enum class CapabilityStatus {
        AVAILABLE,
        UNAVAILABLE,
        DISABLED,
        NOT_IMPLEMENTED
    }
    
    /**
     * Capability definition.
     */
    data class Capability(
        val id: String,
        val name: String,
        val description: String,
        val requiredPermissions: List<String> = emptyList(),
        var status: CapabilityStatus = CapabilityStatus.NOT_IMPLEMENTED
    )
    
    /**
     * Initialize the capability manager.
     */
    fun initialize() {
        Logger.d(TAG, "CapabilityManager initialized")
        registerBaseCapabilities()
    }
    
    /**
     * Register base capabilities (not implemented, just prepared).
     */
    private fun registerBaseCapabilities() {
        // Voice capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.VOICE_RECOGNITION,
                name = "Voice Recognition",
                description = "Speech-to-text functionality",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.TEXT_TO_SPEECH,
                name = "Text-to-Speech",
                description = "Speech synthesis functionality",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.WAKE_WORD,
                name = "Wake Word Detection",
                description = "Always-listening wake word detection",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Phone capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.PHONE_CALL,
                name = "Phone Calls",
                description = "Make and manage phone calls",
                requiredPermissions = listOf("android.permission.CALL_PHONE"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.CONTACTS,
                name = "Contacts",
                description = "Read and manage contacts",
                requiredPermissions = listOf("android.permission.READ_CONTACTS"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Messaging capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.SMS,
                name = "SMS",
                description = "Send and read SMS messages",
                requiredPermissions = listOf("android.permission.SEND_SMS", "android.permission.READ_SMS"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.WHATSAPP,
                name = "WhatsApp Integration",
                description = "Read and send WhatsApp messages",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.EMAIL,
                name = "Email",
                description = "Send emails via email client",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Media capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.SPOTIFY,
                name = "Spotify Integration",
                description = "Control Spotify playback",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.MEDIA_CONTROL,
                name = "Media Control",
                description = "Control device media playback",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Calendar capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.CALENDAR,
                name = "Calendar",
                description = "Read and manage calendar events",
                requiredPermissions = listOf("android.permission.READ_CALENDAR"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // AI capabilities
        registerCapability(
            Capability(
                id = CapabilityIds.AI_GROQ,
                name = "AI (Groq)",
                description = "AI processing via Groq API",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        registerCapability(
            Capability(
                id = CapabilityIds.AI_LOCAL,
                name = "AI (Local)",
                description = "Local AI processing",
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Location
        registerCapability(
            Capability(
                id = CapabilityIds.LOCATION,
                name = "Location",
                description = "Get device location",
                requiredPermissions = listOf("android.permission.ACCESS_FINE_LOCATION"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        // Camera
        registerCapability(
            Capability(
                id = CapabilityIds.CAMERA,
                name = "Camera",
                description = "Access camera for photos and video",
                requiredPermissions = listOf("android.permission.CAMERA"),
                status = CapabilityStatus.NOT_IMPLEMENTED
            )
        )
        
        Logger.d(TAG, "Registered ${registeredCapabilities.size} base capabilities")
    }
    
    /**
     * Register a new capability.
     */
    fun registerCapability(capability: Capability) {
        registeredCapabilities[capability.id] = capability
        Logger.d(TAG, "Registered capability: ${capability.id}")
        notifyListeners(capability.id, capability.status)
    }
    
    /**
     * Unregister a capability.
     */
    fun unregisterCapability(capabilityId: String) {
        registeredCapabilities.remove(capabilityId)
        Logger.d(TAG, "Unregistered capability: $capabilityId")
    }
    
    /**
     * Get a capability by ID.
     */
    fun getCapability(capabilityId: String): Capability? {
        return registeredCapabilities[capabilityId]
    }
    
    /**
     * Get all registered capabilities.
     */
    fun getAllCapabilities(): List<Capability> {
        return registeredCapabilities.values.toList()
    }
    
    /**
     * Get capabilities by status.
     */
    fun getCapabilitiesByStatus(status: CapabilityStatus): List<Capability> {
        return registeredCapabilities.values.filter { it.status == status }
    }
    
    /**
     * Get available capabilities.
     */
    fun getAvailableCapabilities(): List<Capability> {
        return getCapabilitiesByStatus(CapabilityStatus.AVAILABLE)
    }
    
    /**
     * Update capability status.
     */
    fun updateCapabilityStatus(capabilityId: String, status: CapabilityStatus) {
        registeredCapabilities[capabilityId]?.let { capability ->
            registeredCapabilities[capabilityId] = capability.copy(status = status)
            Logger.d(TAG, "Updated capability $capabilityId status to $status")
            notifyListeners(capabilityId, status)
        }
    }
    
    /**
     * Check if a capability is available.
     */
    fun isCapabilityAvailable(capabilityId: String): Boolean {
        return registeredCapabilities[capabilityId]?.status == CapabilityStatus.AVAILABLE
    }
    
    /**
     * Add a listener for capability changes.
     */
    fun addCapabilityListener(listener: (String, CapabilityStatus) -> Unit) {
        capabilityListeners.add(listener)
    }
    
    /**
     * Remove a capability listener.
     */
    fun removeCapabilityListener(listener: (String, CapabilityStatus) -> Unit) {
        capabilityListeners.remove(listener)
    }
    
    private fun notifyListeners(capabilityId: String, status: CapabilityStatus) {
        capabilityListeners.forEach { listener ->
            try {
                listener(capabilityId, status)
            } catch (e: Exception) {
                Logger.e(TAG, "Error in capability listener", e)
            }
        }
    }
    
    /**
     * Clear all capabilities.
     */
    fun clear() {
        registeredCapabilities.clear()
        capabilityListeners.clear()
    }
    
    /**
     * Get capabilities summary.
     */
    fun getCapabilitiesSummary(): Map<CapabilityStatus, Int> {
        return registeredCapabilities.values
            .groupBy { it.status }
            .mapValues { it.value.size }
    }
    
    /**
     * Log capabilities status.
     */
    fun logCapabilitiesStatus() {
        Logger.d(TAG, "Capabilities Status:")
        getCapabilitiesSummary().forEach { (status, count) ->
            Logger.d(TAG, "  $status: $count")
        }
    }
    
    /**
     * Capability ID constants.
     */
    object CapabilityIds {
        const val VOICE_RECOGNITION = "voice_recognition"
        const val TEXT_TO_SPEECH = "text_to_speech"
        const val WAKE_WORD = "wake_word"
        const val PHONE_CALL = "phone_call"
        const val CONTACTS = "contacts"
        const val SMS = "sms"
        const val WHATSAPP = "whatsapp"
        const val EMAIL = "email"
        const val SPOTIFY = "spotify"
        const val MEDIA_CONTROL = "media_control"
        const val CALENDAR = "calendar"
        const val AI_GROQ = "ai_groq"
        const val AI_LOCAL = "ai_local"
        const val LOCATION = "location"
        const val CAMERA = "camera"
    }
}
