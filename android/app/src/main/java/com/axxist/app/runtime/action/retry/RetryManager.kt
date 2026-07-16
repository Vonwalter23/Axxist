package com.axxist.app.runtime.action.retry

import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.model.RetryPolicy
import com.axxist.app.runtime.action.provider.RetryProvider

/**
 * Manager for retry policies.
 */
class RetryManager : RetryProvider {
    
    companion object {
        private const val TAG = "RetryManager"
    }
    
    /**
     * Calculate delay for retry attempt.
     */
    override fun calculateDelay(policy: RetryPolicy, attemptNumber: Int): Long {
        val delay = (policy.initialDelayMs * Math.pow(
            policy.backoffMultiplier.toDouble(),
            (attemptNumber - 1).toDouble()
        )).toLong()
        
        return delay.coerceAtMost(policy.maxDelayMs)
    }
    
    /**
     * Check if an error is retryable.
     */
    override fun isRetryable(errorCode: String, policy: RetryPolicy): Boolean {
        if (policy.retryableErrors.isEmpty()) {
            // Default retryable errors
            return errorCode in listOf(
                "TIMEOUT",
                "NETWORK_ERROR",
                "SERVICE_UNAVAILABLE",
                "EXECUTION_FAILED"
            )
        }
        return errorCode in policy.retryableErrors
    }
    
    /**
     * Check if retries should be attempted.
     */
    override fun shouldRetry(policy: RetryPolicy, currentAttempt: Int): Boolean {
        return currentAttempt < policy.maxRetries
    }
    
    /**
     * Get next delay with jitter.
     */
    fun getNextDelayWithJitter(policy: RetryPolicy, attemptNumber: Int): Long {
        val baseDelay = calculateDelay(policy, attemptNumber)
        val jitter = (baseDelay * 0.1 * Math.random()).toLong()
        return baseDelay + jitter
    }
    
    /**
     * Log retry attempt.
     */
    fun logRetryAttempt(actionId: String, attempt: Int, policy: RetryPolicy, delay: Long) {
        Logger.d(TAG, "Retry attempt $attempt for $actionId, waiting ${delay}ms")
    }
}
