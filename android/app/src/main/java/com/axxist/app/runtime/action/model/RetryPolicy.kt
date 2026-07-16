package com.axxist.app.runtime.action.model

/**
 * Retry policy for action execution.
 */
data class RetryPolicy(
    val maxRetries: Int = 0,
    val initialDelayMs: Long = 1000L,
    val maxDelayMs: Long = 30000L,
    val backoffMultiplier: Float = 2.0f,
    val retryableErrors: List<String> = emptyList()
) {
    companion object {
        val NONE = RetryPolicy(maxRetries = 0)
        val IMMEDIATE = RetryPolicy(maxRetries = 1)
        val DEFERRED = RetryPolicy(maxRetries = 3, initialDelayMs = 5000L)
        val EXPONENTIAL = RetryPolicy(maxRetries = 5, initialDelayMs = 1000L, backoffMultiplier = 2.0f)
    }
}
