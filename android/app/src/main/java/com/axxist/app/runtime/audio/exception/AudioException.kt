package com.axxist.app.runtime.audio.exception

/**
 * AudioException - Base exception for audio-related errors.
 */
open class AudioException(message: String, cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception thrown when audio permission is denied.
 */
class AudioPermissionException(message: String = "Audio permission not granted") : AudioException(message)

/**
 * Exception thrown when audio initialization fails.
 */
class AudioInitializationException(message: String = "Failed to initialize audio system") : AudioException(message)

/**
 * Exception thrown when audio capture fails.
 */
class AudioCaptureException(message: String = "Failed to capture audio") : AudioException(message)

/**
 * Exception thrown when audio provider is not available.
 */
class AudioProviderUnavailableException(message: String = "Audio provider not available") : AudioException(message)
