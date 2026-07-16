package com.axxist.app.runtime.intent.router

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.IntentDiagnostics
import com.axxist.app.runtime.intent.model.IntentProcessingMethod

/**
 * Collector for intent processing diagnostics.
 */
class IntentDiagnosticsCollector {
    
    companion object {
        private const val TAG = "IntentDiagnosticsCollector"
        
        @Volatile
        private var instance: IntentDiagnosticsCollector? = null
        
        fun getInstance(): IntentDiagnosticsCollector {
            return instance ?: synchronized(this) {
                instance ?: IntentDiagnosticsCollector().also { instance = it }
            }
        }
    }
    
    private var analysisStartTime: Long = 0
    private var validationStartTime: Long = 0
    private var methodUsed: IntentProcessingMethod? = null
    private var providerSelected: String? = null
    private var entitiesExtractedCount: Int = 0
    private val errors = mutableListOf<String>()
    private val warnings = mutableListOf<String>()
    private val metadata = mutableMapOf<String, Any>()
    
    /**
     * Start timing analysis.
     */
    fun startAnalysis() {
        analysisStartTime = System.currentTimeMillis()
        errors.clear()
        warnings.clear()
        metadata.clear()
    }
    
    /**
     * Start timing validation.
     */
    fun startValidation() {
        validationStartTime = System.currentTimeMillis()
    }
    
    /**
     * Set the processing method used.
     */
    fun setMethod(method: IntentProcessingMethod) {
        methodUsed = method
    }
    
    /**
     * Set the provider selected.
     */
    fun setProvider(provider: String) {
        providerSelected = provider
    }
    
    /**
     * Set the count of extracted entities.
     */
    fun setEntitiesExtracted(count: Int) {
        entitiesExtractedCount = count
    }
    
    /**
     * Record an error.
     */
    fun recordError(error: String) {
        errors.add(error)
        Logger.e(TAG, "Intent error: $error")
    }
    
    /**
     * Record a warning.
     */
    fun recordWarning(warning: String) {
        warnings.add(warning)
        Logger.w(TAG, "Intent warning: $warning")
    }
    
    /**
     * Add metadata.
     */
    fun addMetadata(key: String, value: Any) {
        metadata[key] = value
    }
    
    /**
     * Build and return the diagnostics.
     */
    fun build(): IntentDiagnostics {
        val analysisDuration = System.currentTimeMillis() - analysisStartTime
        val validationDuration = if (validationStartTime > 0) {
            System.currentTimeMillis() - validationStartTime
        } else 0
        val totalDuration = analysisDuration + validationDuration
        
        return IntentDiagnostics(
            analysisDurationMs = analysisDuration,
            methodUsed = methodUsed,
            providerSelected = providerSelected,
            entitiesExtractedCount = entitiesExtractedCount,
            validationDurationMs = validationDuration,
            totalDurationMs = totalDuration,
            errors = errors.toList(),
            warnings = warnings.toList(),
            metadata = metadata.toMap()
        )
    }
    
    /**
     * Reset the collector for next use.
     */
    fun reset() {
        analysisStartTime = 0
        validationStartTime = 0
        methodUsed = null
        providerSelected = null
        entitiesExtractedCount = 0
        errors.clear()
        warnings.clear()
        metadata.clear()
    }
}
