package com.axxist.app.runtime.intent.provider

import com.axxist.app.runtime.intent.model.Entity
import com.axxist.app.runtime.intent.model.EntityType

/**
 * Interface for entity extraction providers.
 */
interface EntityProvider {
    
    /**
     * Extract entities from input text.
     */
    suspend fun extract(text: String): List<Entity>
    
    /**
     * Extract entities of specific types.
     */
    suspend fun extract(text: String, types: List<EntityType>): List<Entity>
    
    /**
     * Check if this provider supports a specific entity type.
     */
    fun supportsType(type: EntityType): Boolean
    
    /**
     * Get all supported entity types.
     */
    fun getSupportedTypes(): List<EntityType>
    
    /**
     * Get the provider name.
     */
    fun getName(): String = "EntityProvider"
}
