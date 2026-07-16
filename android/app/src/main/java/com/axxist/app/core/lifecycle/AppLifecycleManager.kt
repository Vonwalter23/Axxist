package com.axxist.app.core.lifecycle

import com.axxist.app.core.eventbus.AppEvent
import com.axxist.app.core.eventbus.EventBusManager
import com.axxist.app.core.logger.Logger
import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * App Lifecycle Manager - Handles application lifecycle events.
 */
class AppLifecycleManager : Application.ActivityLifecycleCallbacks {
    
    companion object {
        private const val TAG = "AppLifecycle"
    }
    
    private var isAppInForeground = false
    private var currentActivity: Activity? = null
    
    /**
     * Initialize the lifecycle manager.
     */
    fun initialize(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
        Logger.d(TAG, "AppLifecycleManager initialized")
    }
    
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Logger.d(TAG, "Activity created: ${activity.javaClass.simpleName}")
        currentActivity = activity
    }
    
    override fun onActivityStarted(activity: Activity) {
        Logger.d(TAG, "Activity started: ${activity.javaClass.simpleName}")
    }
    
    override fun onActivityResumed(activity: Activity) {
        Logger.d(TAG, "Activity resumed: ${activity.javaClass.simpleName}")
        if (!isAppInForeground) {
            isAppInForeground = true
            EventBusManager.emitAppEvent(AppEvent.Resumed)
            Logger.d(TAG, "App entered foreground")
        }
    }
    
    override fun onActivityPaused(activity: Activity) {
        Logger.d(TAG, "Activity paused: ${activity.javaClass.simpleName}")
    }
    
    override fun onActivityStopped(activity: Activity) {
        Logger.d(TAG, "Activity stopped: ${activity.javaClass.simpleName}")
    }
    
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Logger.d(TAG, "Activity save instance state: ${activity.javaClass.simpleName}")
    }
    
    override fun onActivityDestroyed(activity: Activity) {
        Logger.d(TAG, "Activity destroyed: ${activity.javaClass.simpleName}")
        if (activity == currentActivity) {
            currentActivity = null
        }
    }
    
    /**
     * Check if app is in foreground.
     */
    fun isInForeground(): Boolean = isAppInForeground
    
    /**
     * Get current activity.
     */
    fun getCurrentActivity(): Activity? = currentActivity
}
