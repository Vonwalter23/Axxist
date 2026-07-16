package com.axxist.app.runtime.health

import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.eventbus.RuntimeEvent
import com.axxist.app.core.logger.Logger
import com.axxist.app.runtime.manager.RuntimeManager
import com.axxist.app.runtime.state.RuntimeState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * HealthMonitor - Monitors the health of the Runtime and its components.
 * 
 * Periodically checks:
 * - Service state
 * - Runtime state
 * - Registered modules
 * 
 * In this stage, it only logs events. No automatic recovery is implemented.
 */
class HealthMonitor {
    
    companion object {
        private const val TAG = "HealthMonitor"
        private const val CHECK_INTERVAL_MS = 60_000L // 1 minute
    }
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var monitoringJob: Job? = null
    private var isMonitoring = false
    
    data class HealthStatus(
        val isHealthy: Boolean,
        val runtimeState: RuntimeState,
        val serviceRunning: Boolean,
        val modulesCount: Int,
        val issues: List<String> = emptyList()
    )
    
    /**
     * Start health monitoring.
     */
    fun start() {
        if (isMonitoring) {
            Logger.w(TAG, "HealthMonitor already running")
            return
        }
        
        Logger.d(TAG, "Starting HealthMonitor...")
        isMonitoring = true
        
        monitoringJob = scope.launch {
            while (isActive && isMonitoring) {
                performHealthCheck()
                delay(CHECK_INTERVAL_MS)
            }
        }
        
        Logger.d(TAG, "HealthMonitor started")
    }
    
    /**
     * Stop health monitoring.
     */
    fun stop() {
        Logger.d(TAG, "Stopping HealthMonitor...")
        isMonitoring = false
        monitoringJob?.cancel()
        monitoringJob = null
        Logger.d(TAG, "HealthMonitor stopped")
    }
    
    /**
     * Perform a single health check.
     */
    fun performHealthCheck(): HealthStatus {
        val issues = mutableListOf<String>()
        
        try {
            val runtimeManager = RuntimeManager.getInstance()
            val runtimeState = runtimeManager.getState()
            val modules = runtimeManager.getRegisteredModules()
            
            // Check runtime state
            when (runtimeState) {
                RuntimeState.ERROR -> {
                    issues.add("Runtime in ERROR state")
                    Logger.w(TAG, "Health check: Runtime in ERROR state")
                }
                RuntimeState.STOPPED -> {
                    issues.add("Runtime is stopped")
                    Logger.w(TAG, "Health check: Runtime is stopped")
                }
                RuntimeState.STOPPING -> {
                    issues.add("Runtime is stopping")
                }
                else -> {
                    // Normal states
                }
            }
            
            // Check for registered modules in error state
            modules.values.filter { it.status == RuntimeManager.ModuleStatus.ERROR }.forEach { module ->
                issues.add("Module ${module.name} in ERROR state")
                Logger.w(TAG, "Health check: Module ${module.name} in ERROR state")
            }
            
            val isHealthy = issues.isEmpty() && runtimeState.isActive()
            
            val status = HealthStatus(
                isHealthy = isHealthy,
                runtimeState = runtimeState,
                serviceRunning = runtimeState.isActive(),
                modulesCount = modules.size,
                issues = issues
            )
            
            // Emit health check event
            EventBusManager.emitRuntimeEvent(RuntimeEvent.HealthCheck)
            
            if (!isHealthy) {
                Logger.w(TAG, "Health check failed: ${issues.joinToString(", ")}")
            } else {
                Logger.d(TAG, "Health check passed - Runtime: $runtimeState, Modules: ${modules.size}")
            }
            
            return status
            
        } catch (e: Exception) {
            Logger.e(TAG, "Error performing health check", e)
            return HealthStatus(
                isHealthy = false,
                runtimeState = RuntimeState.ERROR,
                serviceRunning = false,
                modulesCount = 0,
                issues = listOf("Health check exception: ${e.message}")
            )
        }
    }
    
    /**
     * Check if monitoring is active.
     */
    fun isMonitoring(): Boolean = isMonitoring
    
    /**
     * Get current health status without waiting.
     */
    fun getCurrentStatus(): HealthStatus {
        return try {
            val runtimeManager = RuntimeManager.getInstance()
            HealthStatus(
                isHealthy = runtimeManager.isActive(),
                runtimeState = runtimeManager.getState(),
                serviceRunning = runtimeManager.isActive(),
                modulesCount = runtimeManager.getRegisteredModules().size
            )
        } catch (e: Exception) {
            HealthStatus(
                isHealthy = false,
                runtimeState = RuntimeState.ERROR,
                serviceRunning = false,
                modulesCount = 0,
                issues = listOf(e.message ?: "Unknown error")
            )
        }
    }
}
