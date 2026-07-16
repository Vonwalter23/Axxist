package com.axxist.app.runtime.intent.router

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.Intent
import com.axxist.app.runtime.intent.model.IntentCategory
import com.axxist.app.runtime.intent.model.IntentProcessingMethod
import com.axxist.app.runtime.intent.provider.AIIntentProcessor
import com.axxist.app.runtime.intent.provider.HybridIntentProcessor
import com.axxist.app.runtime.intent.provider.IntentProcessor
import com.axxist.app.runtime.intent.provider.RuleIntentProcessor

/**
 * Router for selecting the best intent processing method.
 */
class IntentRouter {
    
    companion object {
        private const val TAG = "IntentRouter"
        
        @Volatile
        private var instance: IntentRouter? = null
        
        fun getInstance(): IntentRouter {
            return instance ?: synchronized(this) {
                instance ?: IntentRouter().also { instance = it }
            }
        }
    }
    
    private var ruleProcessor: RuleIntentProcessor? = null
    private var aiProcessor: AIIntentProcessor? = null
    private var hybridProcessor: HybridIntentProcessor? = null
    
    private var defaultMethod: IntentProcessingMethod = IntentProcessingMethod.RULE_BASED
    
    /**
     * Set the rule-based processor.
     */
    fun setRuleProcessor(processor: RuleIntentProcessor) {
        ruleProcessor = processor
        Logger.d(TAG, "Rule processor set: ${processor.getName()}")
    }
    
    /**
     * Set the AI-based processor.
     */
    fun setAIProcessor(processor: AIIntentProcessor) {
        aiProcessor = processor
        Logger.d(TAG, "AI processor set: ${processor.getName()}")
    }
    
    /**
     * Set the hybrid processor.
     */
    fun setHybridProcessor(processor: HybridIntentProcessor) {
        hybridProcessor = processor
        Logger.d(TAG, "Hybrid processor set: ${processor.getName()}")
    }
    
    /**
     * Set the default processing method.
     */
    fun setDefaultMethod(method: IntentProcessingMethod) {
        defaultMethod = method
        Logger.d(TAG, "Default method set to: $method")
    }
    
    /**
     * Select the best processor for the given input.
     */
    fun selectProcessor(input: String): SelectedProcessor {
        Logger.d(TAG, "Selecting processor for: ${input.take(50)}...")
        
        // Try rule processor first if available
        ruleProcessor?.let { processor ->
            if (processor.canProcess(input)) {
                Logger.d(TAG, "Selected rule processor")
                return SelectedProcessor(
                    processor = null, // Will be used via IntentProcessor
                    method = IntentProcessingMethod.RULE_BASED,
                    processorName = processor.getName()
                )
            }
        }
        
        // Try AI processor if available
        aiProcessor?.let { processor ->
            if (processor.isAvailable()) {
                Logger.d(TAG, "Selected AI processor")
                return SelectedProcessor(
                    processor = null,
                    method = IntentProcessingMethod.AI_BASED,
                    processorName = processor.getName()
                )
            }
        }
        
        // Try hybrid processor if available
        hybridProcessor?.let { processor ->
            if (processor.isAvailable()) {
                Logger.d(TAG, "Selected hybrid processor")
                return SelectedProcessor(
                    processor = null,
                    method = IntentProcessingMethod.HYBRID,
                    processorName = processor.getName()
                )
            }
        }
        
        // Fallback to rule processor
        ruleProcessor?.let { processor ->
            Logger.d(TAG, "Falling back to rule processor")
            return SelectedProcessor(
                processor = null,
                method = IntentProcessingMethod.RULE_BASED,
                processorName = processor.getName()
            )
        }
        
        Logger.w(TAG, "No processor available")
        return SelectedProcessor(
            processor = null,
            method = IntentProcessingMethod.RULE_BASED,
            processorName = "none"
        )
    }
    
    /**
     * Auto-select the best method based on input.
     */
    fun autoSelectMethod(input: String): IntentProcessingMethod {
        val lowercase = input.lowercase()
        
        // Check for common patterns
        return when {
            lowercase.contains("call") || lowercase.contains("llamar") -> IntentProcessingMethod.RULE_BASED
            lowercase.contains("send") || lowercase.contains("enviar") -> IntentProcessingMethod.RULE_BASED
            lowercase.contains("play") || lowercase.contains("reproducir") -> IntentProcessingMethod.RULE_BASED
            lowercase.contains("search") || lowercase.contains("buscar") -> IntentProcessingMethod.RULE_BASED
            lowercase.contains("remind") || lowercase.contains("recordar") -> IntentProcessingMethod.RULE_BASED
            lowercase.contains("navigate") || lowercase.contains("ir a") -> IntentProcessingMethod.RULE_BASED
            lowercase.length > 100 -> IntentProcessingMethod.HYBRID
            else -> defaultMethod
        }
    }
    
    /**
     * Get available methods.
     */
    fun getAvailableMethods(): List<IntentProcessingMethod> {
        val methods = mutableListOf<IntentProcessingMethod>()
        
        ruleProcessor?.let { methods.add(IntentProcessingMethod.RULE_BASED) }
        aiProcessor?.let { methods.add(IntentProcessingMethod.AI_BASED) }
        hybridProcessor?.let { methods.add(IntentProcessingMethod.HYBRID) }
        
        return methods.ifEmpty { listOf(IntentProcessingMethod.RULE_BASED) }
    }
    
    /**
     * Check if any processor is available.
     */
    fun hasAvailableProcessor(): Boolean {
        return ruleProcessor != null || aiProcessor != null || hybridProcessor != null
    }
    
    /**
     * Get summary.
     */
    fun getSummary(): Map<String, Any> = mapOf(
        "hasRuleProcessor" to (ruleProcessor != null),
        "hasAIProcessor" to (aiProcessor != null),
        "hasHybridProcessor" to (hybridProcessor != null),
        "defaultMethod" to defaultMethod.name,
        "availableMethods" to getAvailableMethods().map { it.name }
    )
    
    data class SelectedProcessor(
        val processor: IntentProcessor?,
        val method: IntentProcessingMethod,
        val processorName: String
    )
}
