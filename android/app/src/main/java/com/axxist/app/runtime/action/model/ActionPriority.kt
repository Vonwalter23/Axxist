package com.axxist.app.runtime.action.model

/**
 * Priority levels for actions.
 */
enum class ActionPriority(val value: Int) {
    LOW(0),
    NORMAL(1),
    HIGH(2),
    CRITICAL(3);
    
    companion object {
        fun fromValue(value: Int): ActionPriority {
            return entries.find { it.value == value } ?: NORMAL
        }
    }
}
