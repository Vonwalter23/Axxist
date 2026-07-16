/**
 * Lifecycle - TypeScript lifecycle management.
 * 
 * Provides lifecycle events and state management.
 */

import { AppState, AppStateStatus } from 'react-native';
import { EventBus, AppEvents } from '../eventbus';
import { Logger } from '../logger';

class LifecycleClass {
  private currentState: AppStateStatus = 'unknown';
  private listeners: Set<(state: AppStateStatus) => void> = new Set();

  /**
   * Initialize the Lifecycle manager.
   */
  initialize(): void {
    this.currentState = AppState.currentState;

    // Subscribe to app state changes
    AppState.addEventListener('change', this.handleAppStateChange);

    // Subscribe to native events
    EventBus.subscribe(AppEvents.RESUMED, () => {
      this.notifyListeners('active');
    });

    EventBus.subscribe(AppEvents.PAUSED, () => {
      this.notifyListeners('inactive');
    });

    EventBus.subscribe(AppEvents.STOPPED, () => {
      this.notifyListeners('background');
    });

    Logger.d('Lifecycle', 'Lifecycle initialized');
  }

  /**
   * Handle app state change from React Native.
   */
  private handleAppStateChange = (nextState: AppStateStatus): void => {
    if (nextState === this.currentState) {
      return;
    }

    Logger.d('Lifecycle', `App state changed: ${this.currentState} -> ${nextState}`);
    this.currentState = nextState;

    // Emit to native
    EventBus.emit(nextState === 'active' ? 'app_resumed' : 'app_paused');

    // Notify local listeners
    this.notifyListeners(nextState);
  };

  /**
   * Get current app state.
   */
  getCurrentState(): AppStateStatus {
    return this.currentState;
  }

  /**
   * Check if app is in foreground.
   */
  isInForeground(): boolean {
    return this.currentState === 'active';
  }

  /**
   * Check if app is in background.
   */
  isInBackground(): boolean {
    return this.currentState === 'background';
  }

  /**
   * Subscribe to lifecycle changes.
   */
  subscribe(callback: (state: AppStateStatus) => void): () => void {
    this.listeners.add(callback);
    return () => {
      this.listeners.delete(callback);
    };
  }

  /**
   * Notify listeners of state change.
   */
  private notifyListeners(state: AppStateStatus): void {
    this.listeners.forEach(callback => {
      try {
        callback(state);
      } catch (error) {
        Logger.e('Lifecycle', 'Error in lifecycle listener', error);
      }
    });
  }

  /**
   * Shutdown the Lifecycle manager.
   */
  shutdown(): void {
    this.listeners.clear();
    Logger.d('Lifecycle', 'Lifecycle shutdown');
  }
}

export const Lifecycle = new LifecycleClass();
export default Lifecycle;
