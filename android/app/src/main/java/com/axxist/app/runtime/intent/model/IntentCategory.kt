package com.axxist.app.runtime.intent.model

/**
 * Intent categories for organizing intents by domain.
 */
enum class IntentCategory {
    SYSTEM,        // System operations (reboot, settings, etc.)
    DEVICE,        // Device control (wifi, bluetooth, etc.)
    MEDIA,         // Media playback (music, video, etc.)
    COMMUNICATION, // Communication (calls, messages, etc.)
    NAVIGATION,    // Navigation (maps, directions, etc.)
    PRODUCTIVITY,  // Productivity (calendar, notes, etc.)
    MEMORY,        // Memory (reminders, memos, etc.)
    SEARCH,        // Search operations (web, files, etc.)
    AUTOMATION,    // Automation triggers (routines, etc.)
    CUSTOM         // Custom user-defined intents
}
