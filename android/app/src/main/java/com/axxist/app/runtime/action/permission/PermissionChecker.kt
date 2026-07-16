package com.axxist.app.runtime.action.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.action.provider.PermissionProvider

/**
 * Infrastructure for checking Android permissions.
 * No actual permission requests are made.
 */
class PermissionChecker(private val context: Context) : PermissionProvider {
    
    companion object {
        private const val TAG = "PermissionChecker"
    }
    
    /**
     * Check if a permission is granted.
     */
    override fun isPermissionGranted(permission: String): Boolean {
        return try {
            val result = ContextCompat.checkSelfPermission(context, permission)
            val granted = result == PackageManager.PERMISSION_GRANTED
            Logger.d(TAG, "Permission $permission: ${if (granted) "GRANTED" else "DENIED"}")
            granted
        } catch (e: Exception) {
            Logger.e(TAG, "Error checking permission $permission", e)
            false
        }
    }
    
    /**
     * Check if multiple permissions are granted.
     */
    override fun arePermissionsGranted(permissions: List<String>): Map<String, Boolean> {
        return permissions.associateWith { isPermissionGranted(it) }
    }
    
    /**
     * Get the list of missing permissions.
     */
    override fun getMissingPermissions(requiredPermissions: List<String>): List<String> {
        return requiredPermissions.filter { !isPermissionGranted(it) }
    }
    
    /**
     * Check if we can request a permission.
     */
    override fun canRequestPermission(permission: String): Boolean {
        // Infrastructure only - permission request not implemented
        return true
    }
    
    /**
     * Get the provider name.
     */
    override fun getName(): String = "PermissionChecker"
    
    /**
     * Get all dangerous permissions that need runtime requests.
     */
    fun getDangerousPermissions(): List<String> {
        return listOf(
            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_CONTACTS",
            "android.permission.READ_SMS",
            "android.permission.SEND_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.CALL_PHONE",
            "android.permission.READ_CALL_LOG",
            "android.permission.READ_PHONE_STATE",
            "android.permission.CAMERA",
            "android.permission.RECORD_AUDIO",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.BLUETOOTH_CONNECT",
            "android.permission.BLUETOOTH_SCAN",
            "android.permission.NEARBY_WIFI_DEVICES"
        )
    }
    
    /**
     * Get permission group for a permission.
     */
    fun getPermissionGroup(permission: String): String? {
        return when {
            permission.contains("CONTACTS") -> "CONTACTS"
            permission.contains("SMS") -> "SMS"
            permission.contains("CALL") -> "PHONE"
            permission.contains("CAMERA") -> "CAMERA"
            permission.contains("LOCATION") -> "LOCATION"
            permission.contains("STORAGE") -> "STORAGE"
            permission.contains("BLUETOOTH") -> "BLUETOOTH"
            permission.contains("MICROPHONE") || permission.contains("RECORD_AUDIO") -> "MICROPHONE"
            else -> null
        }
    }
}
