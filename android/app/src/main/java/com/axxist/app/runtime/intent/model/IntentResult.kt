package com.axxist.app.runtime.intent.model

/**
 * Complete result of intent processing.
 */
data class IntentResult(
    val intent: Intent?,
    val validationResult: ValidationResult,
    val confidence: Float,
    val confidenceLevel: ConfidenceLevel,
    val diagnostics: IntentDiagnostics,
    val timestamp: Long = System.currentTimeMillis()
) {
    val isSuccessful: Boolean
        get() = intent != null && validationResult.isValid
    
    val isReadyForExecution: Boolean
        get() = isSuccessful && confidence >= 0.5f
    
    companion object {
        fun success(
            intent: Intent,
            validation: ValidationResult,
            diagnostics: IntentDiagnostics
        ): IntentResult = IntentResult(
            intent = intent,
            validationResult = validation,
            confidence = intent.confidence,
            confidenceLevel = intent.getConfidenceLevel(),
            diagnostics = diagnostics
        )
        
        fun failure(
            intent: Intent?,
            validation: ValidationResult,
            diagnostics: IntentDiagnostics
        ): IntentResult = IntentResult(
            intent = intent,
            validationResult = validation,
            confidence = intent?.confidence ?: 0f,
            confidenceLevel = ConfidenceLevel.VERY_LOW,
            diagnostics = diagnostics
        )
        
        fun empty(diagnostics: IntentDiagnostics): IntentResult = IntentResult(
            intent = null,
            validationResult = ValidationResult.invalid(errors = listOf("No intent detected")),
            confidence = 0f,
            confidenceLevel = ConfidenceLevel.VERY_LOW,
            diagnostics = diagnostics
        )
    }
}
