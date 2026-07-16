package com.axxist.app.core.config

import com.axxist.app.core.eventbus.ConfigEvent
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.logger.Logger
import android.content.Context
import android.content.SharedPreferences

/**
 * Configuration key definitions.
 */
object ConfigKeys {
    const val LOG_LEVEL = "log_level"
    const val DEBUG_MODE = "debug_mode"
    const val FIRST_LAUNCH = "first_launch"
    const val APP_VERSION = "app_version"
    const val FEATURE_WAKE_WORD = "feature_wake_word"
    const val FEATURE_VOICE = "feature_voice"
    const val FEATURE_AI = "feature_ai"
    const val FEATURE_SPOTIFY = "feature_spotify"
    const val FEATURE_WHATSAPP = "feature_whatsapp"
    const val FEATURE_EMAIL = "feature_email"
    const val FEATURE_CALENDAR = "feature_calendar"
    const val FEATURE_CONTACTS = "feature_contacts"
    const val FEATURE_PHONE = "feature_phone"
}

/**
 * Feature flag keys.
 */
object FeatureFlags {
    const val WAKE_WORD = "wake_word"
    const val VOICE = "voice"
    const val AI = "ai"
    const val SPOTIFY = "spotify"
    const val WHATSAPP = "whatsapp"
    const val EMAIL = "email"
    const val CALENDAR = "calendar"
    const val CONTACTS = "contacts"
    const val PHONE = "phone"
}

/**
 * ConfigManager - Centralized configuration management.
 * 
 * All configuration must be centralized here.
 * No hardcoded values allowed.
 */
class ConfigManager private constructor(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    private val defaults: Map<String, Any> = mapOf(
        ConfigKeys.LOG_LEVEL to "INFO",
        ConfigKeys.DEBUG_MODE to false,
        ConfigKeys.FIRST_LAUNCH to true,
        ConfigKeys.APP_VERSION to "0.0.0",
        ConfigKeys.FEATURE_WAKE_WORD to false,
        ConfigKeys.FEATURE_VOICE to false,
        ConfigKeys.FEATURE_AI to false,
        ConfigKeys.FEATURE_SPOTIFY to false,
        ConfigKeys.FEATURE_WHATSAPP to false,
        ConfigKeys.FEATURE_EMAIL to false,
        ConfigKeys.FEATURE_CALENDAR to false,
        ConfigKeys.FEATURE_CONTACTS to false,
        ConfigKeys.FEATURE_PHONE to false
    )
    
    companion object {
        private const val TAG = "ConfigManager"
        private const val PREFS_NAME = "axxist_config"
        
        @Volatile
        private var instance: ConfigManager? = null
        
        fun initialize(context: Context): ConfigManager {
            return instance ?: synchronized(this) {
                instance ?: ConfigManager(context.applicationContext).also { instance = it }
            }
        }
        
        fun getInstance(): ConfigManager {
            return instance ?: throw IllegalStateException("ConfigManager not initialized. Call initialize() first.")
        }
    }
    
    init {
        Logger.d(TAG, "ConfigManager initialized")
        initializeDefaults()
    }
    
    private fun initializeDefaults() {
        defaults.forEach { (key, value) ->
            if (!prefs.contains(key)) {
                when (value) {
                    is Boolean -> prefs.edit().putBoolean(key, value).apply()
                    is Int -> prefs.edit().putInt(key, value).apply()
                    is Long -> prefs.edit().putLong(key, value).apply()
                    is Float -> prefs.edit().putFloat(key, value).apply()
                    is String -> prefs.edit().putString(key, value).apply()
                }
            }
        }
    }
    
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return try {
            prefs.getBoolean(key, defaultValue)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting boolean for key: $key", e)
            defaultValue
        }
    }
    
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return try {
            prefs.getInt(key, defaultValue)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting int for key: $key", e)
            defaultValue
        }
    }
    
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return try {
            prefs.getLong(key, defaultValue)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting long for key: $key", e)
            defaultValue
        }
    }
    
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return try {
            prefs.getFloat(key, defaultValue)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting float for key: $key", e)
            defaultValue
        }
    }
    
    fun getString(key: String, defaultValue: String = ""): String {
        return try {
            prefs.getString(key, defaultValue) ?: defaultValue
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting string for key: $key", e)
            defaultValue
        }
    }
    
    fun setBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
        emitConfigChange(key)
    }
    
    fun setInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
        emitConfigChange(key)
    }
    
    fun setLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
        emitConfigChange(key)
    }
    
    fun setFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
        emitConfigChange(key)
    }
    
    fun setString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
        emitConfigChange(key)
    }
    
    fun contains(key: String): Boolean = prefs.contains(key)
    
    fun remove(key: String) {
        prefs.edit().remove(key).apply()
        emitConfigChange(key)
    }
    
    fun clear() {
        prefs.edit().clear().apply()
        initializeDefaults()
        EventBusManager.emitConfigEvent(ConfigEvent.Updated)
    }
    
    fun getAllKeys(): Set<String> = prefs.all.keys
    
    fun isFeatureEnabled(feature: String): Boolean {
        val key = when (feature) {
            FeatureFlags.WAKE_WORD -> ConfigKeys.FEATURE_WAKE_WORD
            FeatureFlags.VOICE -> ConfigKeys.FEATURE_VOICE
            FeatureFlags.AI -> ConfigKeys.FEATURE_AI
            FeatureFlags.SPOTIFY -> ConfigKeys.FEATURE_SPOTIFY
            FeatureFlags.WHATSAPP -> ConfigKeys.FEATURE_WHATSAPP
            FeatureFlags.EMAIL -> ConfigKeys.FEATURE_EMAIL
            FeatureFlags.CALENDAR -> ConfigKeys.FEATURE_CALENDAR
            FeatureFlags.CONTACTS -> ConfigKeys.FEATURE_CONTACTS
            FeatureFlags.PHONE -> ConfigKeys.FEATURE_PHONE
            else -> return false
        }
        return getBoolean(key, false)
    }
    
    fun setFeatureEnabled(feature: String, enabled: Boolean) {
        val key = when (feature) {
            FeatureFlags.WAKE_WORD -> ConfigKeys.FEATURE_WAKE_WORD
            FeatureFlags.VOICE -> ConfigKeys.FEATURE_VOICE
            FeatureFlags.AI -> ConfigKeys.FEATURE_AI
            FeatureFlags.SPOTIFY -> ConfigKeys.FEATURE_SPOTIFY
            FeatureFlags.WHATSAPP -> ConfigKeys.FEATURE_WHATSAPP
            FeatureFlags.EMAIL -> ConfigKeys.FEATURE_EMAIL
            FeatureFlags.CALENDAR -> ConfigKeys.FEATURE_CALENDAR
            FeatureFlags.CONTACTS -> ConfigKeys.FEATURE_CONTACTS
            FeatureFlags.PHONE -> ConfigKeys.FEATURE_PHONE
            else -> return
        }
        setBoolean(key, enabled)
    }
    
    private fun emitConfigChange(key: String) {
        EventBusManager.emitConfigEvent(ConfigEvent.KeyUpdated(key))
        EventBusManager.emitConfigEvent(ConfigEvent.Updated)
    }
}
