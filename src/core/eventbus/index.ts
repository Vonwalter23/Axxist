/**
 * EventBus - TypeScript event system.
 * 
 * Mirrors the Android EventBus for cross-platform communication.
 */

import { NativeBridge } from '../nativebridge/NativeBridge';
import { Logger } from '../logger';
import type { AppEvent, EventType } from '../types';

type EventCallback<T = unknown> = (event: T) => void;

class EventBusClass {
  private listeners: Map<string, Set<EventCallback>> = new Map();
  private nativeListenerRemove: (() => void) | null = null;

  /**
   * Initialize the EventBus.
   */
  initialize(): void {
    if (this.nativeListenerRemove) {
      return;
    }

    // Listen for events from native
    this.nativeListenerRemove = NativeBridge.addEventListener(
      'AxxistEvent',
      (event: AppEvent) => {
        this.notifyListeners(event.type, event);
      },
    );

    Logger.d('EventBus', 'EventBus initialized');
  }

  /**
   * Subscribe to an event type.
   */
  subscribe<T = unknown>(eventType: EventType, callback: EventCallback<T>): () => void {
    if (!this.listeners.has(eventType)) {
      this.listeners.set(eventType, new Set());
    }
    this.listeners.get(eventType)?.add(callback as EventCallback);

    return () => {
      this.listeners.get(eventType)?.delete(callback as EventCallback);
    };
  }

  /**
   * Subscribe to all events.
   */
  subscribeAll(callback: EventCallback<AppEvent>): () => void {
    return this.subscribe('*', callback);
  }

  /**
   * Emit an event.
   */
  async emit(eventType: EventType, data?: Record<string, unknown>): Promise<void> {
    Logger.d('EventBus', `Emitting event: ${eventType}`);
    
    try {
      await NativeBridge.emitEvent(eventType, data);
    } catch (error) {
      Logger.e('EventBus', `Failed to emit event: ${eventType}`, error);
    }

    // Local notification
    this.notifyListeners(eventType, { type: eventType, data });
  }

  /**
   * Notify local listeners.
   */
  private notifyListeners(eventType: EventType, event: AppEvent): void {
    const callbacks = this.listeners.get(eventType);
    if (callbacks) {
      callbacks.forEach(callback => {
        try {
          callback(event);
        } catch (error) {
          Logger.e('EventBus', `Error in event callback for ${eventType}`, error);
        }
      });
    }

    // Also notify wildcard listeners
    const wildcardCallbacks = this.listeners.get('*');
    if (wildcardCallbacks) {
      wildcardCallbacks.forEach(callback => {
        try {
          callback(event);
        } catch (error) {
          Logger.e('EventBus', `Error in wildcard event callback`, error);
        }
      });
    }
  }

  /**
   * Remove all listeners.
   */
  removeAllListeners(): void {
    this.listeners.clear();
    Logger.d('EventBus', 'All listeners removed');
  }

  /**
   * Shutdown the EventBus.
   */
  shutdown(): void {
    this.removeAllListeners();
    if (this.nativeListenerRemove) {
      this.nativeListenerRemove();
      this.nativeListenerRemove = null;
    }
    Logger.d('EventBus', 'EventBus shutdown');
  }

  /**
   * Get listener count for an event type.
   */
  getListenerCount(eventType: string): number {
    return this.listeners.get(eventType)?.size || 0;
  }
}

export const EventBus = new EventBusClass();
export default EventBus;

// Standard event types for convenience
export const AppEvents = {
  STARTED: 'app_started' as EventType,
  STOPPED: 'app_stopped' as EventType,
  PAUSED: 'app_paused' as EventType,
  RESUMED: 'app_resumed' as EventType,
};

export const ModuleEvents = {
  LOADED: 'module_loaded' as EventType,
  ERROR: 'module_error' as EventType,
  UNLOADED: 'module_unloaded' as EventType,
};

export const ConfigEvents = {
  UPDATED: 'config_updated' as EventType,
  KEY_UPDATED: 'config_key_updated' as EventType,
};
