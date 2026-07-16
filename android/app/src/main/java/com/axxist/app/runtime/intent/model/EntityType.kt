package com.axxist.app.runtime.intent.model

/**
 * Entity types that can be extracted from user input.
 */
enum class EntityType {
    PERSON,         // Names of people
    DATE,           // Dates (absolute or relative)
    TIME,           // Time expressions
    DURATION,       // Duration expressions
    LOCATION,       // Places and addresses
    APPLICATION,     // App names
    PHONE_NUMBER,   // Phone numbers
    EMAIL,          // Email addresses
    URL,            // Web URLs
    NUMBER,         // Numeric values
    TEXT,           // Free text
    COMMAND,        // Command keywords
    CURRENCY,       // Money expressions
    PERCENTAGE,     // Percentage values
    HASHTAG,        // Social media hashtags
    MENTION,        // Social media mentions
    MUSIC_ARTIST,   // Music artists
    MUSIC_SONG,     // Music tracks
    PLAYLIST,       // Music playlists
    CONTACT,        // Contact names
    CALENDAR_EVENT,  // Calendar event names
    REMINDER,       // Reminder content
    ALARM_TIME      // Alarm time settings
}
