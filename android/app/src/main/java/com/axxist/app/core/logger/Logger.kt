package com.axxist.app.core.logger

import android.util.Log

/**
 * Axxist Logger - Centralized logging system.
 * 
 * Provides a single logging system with configurable levels.
 * Can be disabled in Release builds.
 */
object Logger {
    private var minimumLevel: LogLevel = LogLevel.DEBUG
    private var isEnabled: Boolean = true
    private val tagPrefix: String = "Axxist"

    /**
     * Initialize the logger with the specified minimum log level.
     */
    fun initialize(level: LogLevel = LogLevel.DEBUG) {
        minimumLevel = level
    }

    /**
     * Enable or disable the logger.
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
    }

    /**
     * Set the minimum log level.
     */
    fun setMinimumLevel(level: LogLevel) {
        minimumLevel = level
    }

    /**
     * Log a debug message.
     */
    fun d(tag: String, message: String) {
        log(LogLevel.DEBUG, tag, message)
    }

    /**
     * Log an info message.
     */
    fun i(tag: String, message: String) {
        log(LogLevel.INFO, tag, message)
    }

    /**
     * Log a warning message.
     */
    fun w(tag: String, message: String) {
        log(LogLevel.WARNING, tag, message)
    }

    /**
     * Log an error message.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(LogLevel.ERROR, tag, message, throwable)
    }

    /**
     * Internal log function.
     */
    private fun log(level: LogLevel, tag: String, message: String, throwable: Throwable? = null) {
        if (!isEnabled || level.ordinal < minimumLevel.ordinal) {
            return
        }

        val fullTag = "$tagPrefix:$tag"
        val logMessage = if (throwable != null) {
            "$message\n${throwable.stackTraceToString()}"
        } else {
            message
        }

        when (level) {
            LogLevel.DEBUG -> Log.d(fullTag, logMessage)
            LogLevel.INFO -> Log.i(fullTag, logMessage)
            LogLevel.WARNING -> Log.w(fullTag, logMessage)
            LogLevel.ERROR -> Log.e(fullTag, logMessage)
        }
    }

    /**
     * Get the current minimum log level.
     */
    fun getMinimumLevel(): LogLevel = minimumLevel

    /**
     * Check if the logger is enabled.
     */
    fun isEnabled(): Boolean = isEnabled
}
