package com.axxist.app.runtime.ai.state

import com.axxist.app.runtime.conversation.provider.AIProvider

/**
 * AI State - Represents the possible states of the AI system.
 */
enum class AIState {
    DISABLED,
    IDLE,
    REQUESTING,
    RESPONDING,
    ERROR;
    
    fun isActive(): Boolean = this == REQUESTING || this == RESPONDING
    
    fun canTransitionTo(target: AIState): Boolean {
        return when (this) {
            DISABLED -> target == IDLE
            IDLE -> target == REQUESTING
            REQUESTING -> target == RESPONDING || target == ERROR || target == IDLE
            RESPONDING -> target == IDLE || target == ERROR
            ERROR -> target == IDLE || target == DISABLED
        }
    }
}

/**
 * AI Request status.
 */
enum class AIRequestStatus {
    PENDING,
    IN_PROGRESS,
    SUCCESS,
    FAILED,
    TIMEOUT,
    CANCELLED
}

/**
 * AI Provider status.
 */
data class AIProviderStatus(
    val type: AIProvider.ProviderType,
    val isAvailable: Boolean,
    val isEnabled: Boolean,
    val lastError: String? = null,
    val lastUsed: Long? = null,
    val requestCount: Int = 0,
    val errorCount: Int = 0
)
