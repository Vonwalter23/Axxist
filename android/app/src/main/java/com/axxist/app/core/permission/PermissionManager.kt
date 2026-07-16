package com.axxist.app.core.permission

import com.axxist.app.core.logger.Logger
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Permission Manager - Infrastructure for permission management.
 * 
 * This module creates the infrastructure for permissions.
 * Permissions are NOT requested here - only the infrastructure is prepared.
 * 
 * Permissions will be requested when the user accesses specific features.
 */
object PermissionManager {
    
    private const val TAG = "PermissionManager"
    
    /**
     * Permission definitions for future use.
     */
    object Permissions {
        // Audio
        const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
        
        // Phone
        const val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
        const val CALL_PHONE = Manifest.permission.CALL_PHONE
        const val READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
        
        // Contacts
        const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
        const val WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS
        
        // Calendar
        const val READ_CALENDAR = Manifest.permission.READ_CALENDAR
        const val WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR
        
        // SMS
        const val SEND_SMS = Manifest.permission.SEND_SMS
        const val READ_SMS = Manifest.permission.READ_SMS
        
        // Storage
        const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        
        // Location
        const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
        const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
        
        // Camera
        const val CAMERA = Manifest.permission.CAMERA
        
        // Notifications
        const val POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
        
        // Foreground Service
        const val FOREGROUND_SERVICE = Manifest.permission.FOREGROUND_SERVICE
        const val FOREGROUND_SERVICE_MICROPHONE = Manifest.permission.FOREGROUND_SERVICE_MICROPHONE
    }
    
    /**
     * Permission groups for organized requests.
     */
    object PermissionGroups {
        val VOICE = listOf(Permissions.RECORD_AUDIO)
        val PHONE = listOf(Permissions.READ_PHONE_STATE, Permissions.CALL_PHONE)
        val CONTACTS = listOf(Permissions.READ_CONTACTS, Permissions.WRITE_CONTACTS)
        val CALENDAR = listOf(Permissions.READ_CALENDAR, Permissions.WRITE_CALENDAR)
        val SMS = listOf(Permissions.SEND_SMS, Permissions.READ_SMS)
        val LOCATION = listOf(Permissions.ACCESS_FINE_LOCATION, Permissions.ACCESS_COARSE_LOCATION)
        val CAMERA = listOf(Permissions.CAMERA)
        val STORAGE = listOf(Permissions.READ_EXTERNAL_STORAGE, Permissions.WRITE_EXTERNAL_STORAGE)
        val NOTIFICATIONS = listOf(Permissions.POST_NOTIFICATIONS)
        val FOREGROUND = listOf(Permissions.FOREGROUND_SERVICE, Permissions.FOREGROUND_SERVICE_MICROPHONE)
    }
    
    /**
     * Check if a permission is granted.
     */
    fun isGranted(context: Context, permission: String): Boolean {
        return try {
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            Logger.e(TAG, "Error checking permission: $permission", e)
            false
        }
    }
    
    /**
     * Check if all permissions in a list are granted.
     */
    fun areAllGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.all { isGranted(context, it) }
    }
    
    /**
     * Check if any permission in a list is granted.
     */
    fun isAnyGranted(context: Context, permissions: List<String>): Boolean {
        return permissions.any { isGranted(context, it) }
    }
    
    /**
     * Get list of permissions that are not granted.
     */
    fun getMissingPermissions(context: Context, permissions: List<String>): List<String> {
        return permissions.filter { !isGranted(context, it) }
    }
    
    /**
     * Get the permission status as a map.
     */
    fun getPermissionStatus(context: Context, permissions: List<String>): Map<String, Boolean> {
        return permissions.associateWith { isGranted(context, it) }
    }
    
    /**
     * Permission status enum.
     */
    enum class PermissionStatus {
        GRANTED,
        DENIED,
        DENIED_FOREVER,
        UNKNOWN
    }
    
    /**
     * Get detailed permission status.
     */
    fun getDetailedStatus(context: Context, permission: String): PermissionStatus {
        return when {
            isGranted(context, permission) -> PermissionStatus.GRANTED
            else -> PermissionStatus.DENIED
        }
    }
    
    /**
     * Log permission status for debugging.
     */
    fun logPermissionStatus(context: Context, permissions: List<String>) {
        Logger.d(TAG, "Permission Status:")
        permissions.forEach { permission ->
            val status = getDetailedStatus(context, permission)
            Logger.d(TAG, "  $permission: $status")
        }
    }
}
