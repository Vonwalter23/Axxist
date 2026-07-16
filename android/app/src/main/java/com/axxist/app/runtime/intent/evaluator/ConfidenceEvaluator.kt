package com.axxist.app.runtime.intent.evaluator

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.intent.model.ConfidenceLevel
import com.axxist.app.runtime.intent.model.Intent
import com.axxist.app.runtime.intent.provider.ConfidenceProvider

/**
 * Evaluator for calculating intent confidence levels.
 */
class ConfidenceEvaluator : ConfidenceProvider {
    
    companion object {
        private const val TAG = "ConfidenceEvaluator"
        
        @Volatile
        private var instance: ConfidenceEvaluator? = null
        
        fun getInstance(): ConfidenceEvaluator {
            return instance ?: synchronized(this) {
                instance ?: ConfidenceEvaluator().also { instance = it }
            }
        }
    }
    
    override fun evaluate(intent: Intent): ConfidenceResult {
        return evaluate(intent, emptyMap())
    }
    
    override fun evaluate(intent: Intent, context: Map<String, Any>): ConfidenceResult {
        val factors = mutableMapOf<String, Float>()
        
        // Base confidence from intent
        var confidence = intent.confidence
        factors["baseConfidence"] = intent.confidence
        
        // Adjust based on entity count
        val entityCount = intent.entities.size
        if (entityCount > 0) {
            val entityBonus = minOf(entityCount * 0.05f, 0.2f)
            confidence += entityBonus
            factors["entityBonus"] = entityBonus
        }
        
        // Adjust based on entity confidence
        if (intent.entities.isNotEmpty()) {
            val avgEntityConfidence = intent.entities.map { it.confidence }.average().toFloat()
            val entityConfidenceWeight = 0.1f
            confidence = confidence * (1 - entityConfidenceWeight) + avgEntityConfidence * entityConfidenceWeight
            factors["avgEntityConfidence"] = avgEntityConfidence
        }
        
        // Context-based adjustments
        context["previousIntent"]?.let {
            confidence += 0.1f
            factors["previousIntentBonus"] = 0.1f
        }
        
        context["conversationActive"]?.let { active ->
            if (active == true) {
                confidence += 0.05f
                factors["conversationBonus"] = 0.05f
            }
        }
        
        // Clamp to [0, 1]
        confidence = confidence.coerceIn(0f, 1f)
        
        val level = ConfidenceLevel.fromValue(confidence)
        val passedThreshold = confidence >= getMinimumThreshold()
        
        Logger.d(TAG, "Evaluated confidence: $confidence (level: $level, passed: $passedThreshold)")
        
        return ConfidenceResult(
            confidence = confidence,
            level = level,
            factors = factors,
            passedThreshold = passedThreshold
        )
    }
    
    override fun getMinimumThreshold(): Float = 0.5f
    
    override fun getName(): String = "ConfidenceEvaluator"
    
    /**
     * Evaluate multiple intents and rank them.
     */
    fun rankIntents(intents: List<Intent>): List<Pair<Intent, Float>> {
        return intents.map { intent ->
            intent to evaluate(intent).confidence
        }.sortedByDescending { it.second }
    }
}
