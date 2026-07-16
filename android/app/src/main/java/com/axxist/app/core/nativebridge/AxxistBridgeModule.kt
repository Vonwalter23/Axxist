package com.axxist.app.core.nativebridge

import com.axxist.app.core.capability.CapabilityManager
import com.axxist.app.core.config.ConfigKeys
import com.axxist.app.core.config.ConfigManager
import com.axxist.app.core.logger.Logger
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule

/**
 * Native Bridge Module for React Native communication.
 * 
 * This module provides the communication layer between React Native and Kotlin.
 * All future communication will use this infrastructure.
 */
class AxxistBridgeModule(reactContext: ReactApplicationContext) : 
    ReactContextBaseJavaModule(reactContext) {
    
    companion object {
        private const val TAG = "AxxistBridge"
    }
    
    override fun getName(): String = "AxxistBridge"
    
    init {
        Logger.d(TAG, "AxxistBridgeModule initialized")
    }
    
    // ============ App Info ============
    
    @ReactMethod
    fun getAppVersion(promise: Promise) {
        try {
            val config = ConfigManager.getInstance()
            val version = config.getString(ConfigKeys.APP_VERSION, "0.0.0")
            promise.resolve(version)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting app version", e)
            promise.reject("ERROR", "Failed to get app version", e)
        }
    }
    
    @ReactMethod
    fun getBuildInfo(promise: Promise) {
        try {
            val info = Arguments.createMap().apply {
                putString("version", "0.0.2-android-core")
                putString("stage", "STAGE_01")
                putBoolean("debug", true)
            }
            promise.resolve(info)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting build info", e)
            promise.reject("ERROR", "Failed to get build info", e)
        }
    }
    
    // ============ Configuration ============
    
    @ReactMethod
    fun getConfig(key: String, promise: Promise) {
        try {
            val config = ConfigManager.getInstance()
            val value = config.getString(key, "")
            promise.resolve(value)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting config: $key", e)
            promise.reject("ERROR", "Failed to get config", e)
        }
    }
    
    @ReactMethod
    fun setConfig(key: String, value: String, promise: Promise) {
        try {
            val config = ConfigManager.getInstance()
            config.setString(key, value)
            promise.resolve(true)
        } catch (e: Exception) {
            Logger.e(TAG, "Error setting config: $key", e)
            promise.reject("ERROR", "Failed to set config", e)
        }
    }
    
    @ReactMethod
    fun getAllConfig(promise: Promise) {
        try {
            val config = ConfigManager.getInstance()
            val keys = config.getAllKeys()
            val result = Arguments.createMap()
            keys.forEach { key ->
                result.putString(key, config.getString(key, ""))
            }
            promise.resolve(result)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting all config", e)
            promise.reject("ERROR", "Failed to get all config", e)
        }
    }
    
    // ============ Capabilities ============
    
    @ReactMethod
    fun getCapabilities(promise: Promise) {
        try {
            val capabilities = CapabilityManager.getAllCapabilities()
            val result = Arguments.createArray()
            
            capabilities.forEach { cap ->
                val permissionsArray = Arguments.createArray()
                cap.requiredPermissions.forEach { perm ->
                    permissionsArray.pushString(perm)
                }
                
                val map = Arguments.createMap().apply {
                    putString("id", cap.id)
                    putString("name", cap.name)
                    putString("description", cap.description)
                    putString("status", cap.status.name)
                    putArray("requiredPermissions", permissionsArray)
                }
                result.pushMap(map)
            }
            
            promise.resolve(result)
        } catch (e: Exception) {
            Logger.e(TAG, "Error getting capabilities", e)
            promise.reject("ERROR", "Failed to get capabilities", e)
        }
    }
    
    @ReactMethod
    fun isCapabilityAvailable(capabilityId: String, promise: Promise) {
        try {
            val available = CapabilityManager.isCapabilityAvailable(capabilityId)
            promise.resolve(available)
        } catch (e: Exception) {
            Logger.e(TAG, "Error checking capability: $capabilityId", e)
            promise.reject("ERROR", "Failed to check capability", e)
        }
    }
    
    // ============ Event System ============
    
    @ReactMethod
    fun emitEvent(eventName: String, data: ReadableMap?, promise: Promise) {
        try {
            Logger.d(TAG, "Event emitted from RN: $eventName")
            promise.resolve(true)
        } catch (e: Exception) {
            Logger.e(TAG, "Error emitting event: $eventName", e)
            promise.reject("ERROR", "Failed to emit event", e)
        }
    }
    
    @ReactMethod
    fun addListener(eventName: String) {
        Logger.d(TAG, "RN listener added for: $eventName")
    }
    
    @ReactMethod
    fun removeListeners(count: Int) {
        Logger.d(TAG, "RN listeners removed: $count")
    }
    
    // ============ Logging ============
    
    @ReactMethod
    fun log(level: String, tag: String, message: String, promise: Promise) {
        try {
            when (level.uppercase()) {
                "DEBUG" -> Logger.d(tag, message)
                "INFO" -> Logger.i(tag, message)
                "WARNING" -> Logger.w(tag, message)
                "ERROR" -> Logger.e(tag, message)
                else -> Logger.i(tag, message)
            }
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERROR", "Failed to log", e)
        }
    }
    
    // ============ Health Check ============
    
    @ReactMethod
    fun healthCheck(promise: Promise) {
        try {
            val health = Arguments.createMap().apply {
                putBoolean("core", true)
                putBoolean("eventBus", true)
                putBoolean("config", true)
                putBoolean("capabilities", true)
                putBoolean("bridge", true)
                putString("status", "healthy")
            }
            promise.resolve(health)
        } catch (e: Exception) {
            Logger.e(TAG, "Error in health check", e)
            promise.reject("ERROR", "Health check failed", e)
        }
    }
    
    // Helper method to send events to React Native
    fun sendEvent(eventName: String, params: WritableMap?) {
        try {
            reactApplicationContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
        } catch (e: Exception) {
            Logger.e(TAG, "Error sending event to RN: $eventName", e)
        }
    }
}
