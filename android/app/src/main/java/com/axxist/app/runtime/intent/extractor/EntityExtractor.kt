package com.axxist.app.runtime.intent.extractor

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.Entity
import com.axxist.app.runtime.intent.model.EntityType
import com.axxist.app.runtime.intent.provider.EntityProvider
import kotlinx.coroutines.runBlocking

/**
 * Entity extractor for extracting entities from text.
 */
class EntityExtractor(
    private val providers: List<EntityProvider> = emptyList()
) {
    
    companion object {
        private const val TAG = "EntityExtractor"
        
        @Volatile
        private var instance: EntityExtractor? = null
        
        fun getInstance(): EntityExtractor {
            return instance ?: synchronized(this) {
                instance ?: EntityExtractor().also { instance = it }
            }
        }
    }
    
    private val defaultProvider = DefaultEntityProvider()
    
    /**
     * Extract all entities from text.
     */
    suspend fun extract(text: String): List<Entity> {
        Logger.d(TAG, "Extracting entities from: ${text.take(50)}...")
        
        val allEntities = mutableListOf<Entity>()
        
        // Use default provider for basic extraction
        allEntities.addAll(defaultProvider.extract(text))
        
        // Use additional providers if available
        for (provider in providers) {
            try {
                allEntities.addAll(provider.extract(text))
            } catch (e: Exception) {
                Logger.e(TAG, "Provider ${provider.getName()} failed", e)
            }
        }
        
        // Deduplicate by type and value
        val deduplicated = allEntities
            .groupBy { it.type to it.value }
            .map { (_, entities) -> entities.maxByOrNull { it.confidence }!! }
        
        Logger.d(TAG, "Extracted ${deduplicated.size} entities")
        return deduplicated
    }
    
    /**
     * Extract entities of specific types.
     */
    suspend fun extract(text: String, types: List<EntityType>): List<Entity> {
        val allEntities = extract(text)
        return allEntities.filter { it.type in types }
    }
    
    /**
     * Get count of extracted entities.
     */
    fun getExtractedCount(): Int = defaultProvider.getExtractedCount()
    
    /**
     * Default entity provider using regex patterns.
     */
    private class DefaultEntityProvider : EntityProvider {
        
        private var extractedCount = 0
        
        override suspend fun extract(text: String): List<Entity> {
            val entities = mutableListOf<Entity>()
            
            // Extract phone numbers
            extractRegex(text, PHONE_PATTERN, EntityType.PHONE_NUMBER)?.let {
                entities.add(it)
            }
            
            // Extract emails
            extractRegex(text, EMAIL_PATTERN, EntityType.EMAIL)?.let {
                entities.add(it)
            }
            
            // Extract URLs
            extractRegex(text, URL_PATTERN, EntityType.URL)?.let {
                entities.add(it)
            }
            
            // Extract times
            extractRegex(text, TIME_PATTERN, EntityType.TIME)?.let {
                entities.add(it)
            }
            
            // Extract dates
            extractRegex(text, DATE_PATTERN, EntityType.DATE)?.let {
                entities.add(it)
            }
            
            // Extract currency
            extractRegex(text, CURRENCY_PATTERN, EntityType.CURRENCY)?.let {
                entities.add(it)
            }
            
            // Extract percentages
            extractRegex(text, PERCENTAGE_PATTERN, EntityType.PERCENTAGE)?.let {
                entities.add(it)
            }
            
            extractedCount += entities.size
            return entities
        }
        
        override suspend fun extract(text: String, types: List<EntityType>): List<Entity> {
            return extract(text).filter { it.type in types }
        }
        
        override fun supportsType(type: EntityType): Boolean = true
        
        override fun getSupportedTypes(): List<EntityType> = EntityType.entries
        
        override fun getName(): String = "DefaultEntityProvider"
        
        fun getExtractedCount(): Int = extractedCount
        
        private fun extractRegex(text: String, pattern: Regex, type: EntityType): Entity? {
            val match = pattern.find(text)
            return match?.let {
                Entity(
                    type = type,
                    value = it.value,
                    confidence = 1.0f,
                    startIndex = it.range.first,
                    endIndex = it.range.last,
                    metadata = mapOf("method" to "regex")
                )
            }
        }
        
        companion object {
            private val PHONE_PATTERN = Regex("""[\+]?[(]?[0-9]{1,3}[)]?[-\s\.]?[0-9]{1,4}[-\s\.]?[0-9]{1,4}[-\s\.]?[0-9]{1,9}""")
            private val EMAIL_PATTERN = Regex("""[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}""")
            private val URL_PATTERN = Regex("""https?://[^\s]+""")
            private val TIME_PATTERN = Regex("""\b([0-1]?[0-9]|2[0-3]):[0-5][0-9]\b""")
            private val DATE_PATTERN = Regex("""\b\d{1,2}[/-]\d{1,2}[/-]\d{2,4}\b""")
            private val CURRENCY_PATTERN = Regex("""\$[\d,]+\.?\d*|€[\d,]+\.?\d*|[\d,]+\.?\d*\s?(dollars?|euros?|pesos?)\b""", RegexOption.IGNORE_CASE)
            private val PERCENTAGE_PATTERN = Regex("""\d+\.?\d*\s?%""")
        }
    }
}
