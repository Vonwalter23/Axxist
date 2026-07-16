package com.axxist.app.runtime.action.provider

/**
 * Interface for permission checking providers.
 */
interface PermissionProvider {
    
    /**
     * Check if a permission is granted.
     */
    fun isPermissionGranted(permission: String): Boolean
    
    /**
     * Check if multiple permissions are granted.
     */
    fun arePermissionsGranted(permissions: List<String>): Map<String, Boolean>
    
    /**
     * Get the list of missing permissions.
     */
    fun getMissingPermissions(requiredPermissions: List<String>): List<String>
    
    /**
     * Check if we can request a permission.
     */
    fun canRequestPermission(permission: String): Boolean
    
    /**
     * Get the provider name.
     */
    fun getName(): String
}
