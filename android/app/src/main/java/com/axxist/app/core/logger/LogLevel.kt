package com.axxist.app.core.logger

/**
 * Log levels for the Axxist logging system.
 */
enum class LogLevel {
    DEBUG,
    INFO,
    WARNING,
    ERROR;

    companion object {
        fun fromString(value: String): LogLevel {
            return entries.find { it.name.equals(value, ignoreCase = true) } ?: INFO
        }
    }
}
