package com.axxist.app.runtime.action.provider

import com.axxist.app.runtime.action.model.RetryPolicy

/**
 * Interface for retry policy providers.
 */
interface RetryProvider {
    
    /**
     * Calculate the delay for a retry attempt.
     */
    fun calculateDelay(policy: RetryPolicy, attemptNumber: Int): Long
    
    /**
     * Check if an error is retryable.
     */
    fun isRetryable(errorCode: String, policy: RetryPolicy): Boolean
    
    /**
     * Check if retries should be attempted.
     */
    fun shouldRetry(policy: RetryPolicy, currentAttempt: Int): Boolean
    
    /**
     * Get the provider name.
     */
    fun getName(): String = "RetryProvider"
}
