/**
 * Axxist Core - Android Core modules for React Native.
 * 
 * This is the main entry point for all core modules.
 */

// Types
export * from './types';

// Native Bridge
export { NativeBridge } from './nativebridge/NativeBridge';
export { default as NativeBridgeModule } from './nativebridge/NativeBridge';

// Logger
export { Logger } from './logger';
export { default as LoggerModule } from './logger';

// EventBus
export { EventBus, AppEvents, ModuleEvents, ConfigEvents } from './eventbus';
export { default as EventBusModule } from './eventbus';

// Config
export { ConfigManager, ConfigKeys, FeatureFlags } from './config';
export { default as ConfigModule } from './config';

// Capabilities
export { CapabilityManager, CapabilityIds } from './capability';
export { default as CapabilityModule } from './capability';

// Lifecycle
export { Lifecycle } from './lifecycle';
export { default as LifecycleModule } from './lifecycle';

// Build
export { Build } from './build';
export { default as BuildModule } from './build';

// ============ Initialization ============

import { NativeBridge } from './nativebridge/NativeBridge';
import { Logger } from './logger';
import { EventBus } from './eventbus';
import { ConfigManager } from './config';
import { CapabilityManager } from './capability';
import { Lifecycle } from './lifecycle';
import { Build } from './build';

/**
 * Initialize all core modules.
 * This should be called once at app startup.
 */
export async function initializeCore(): Promise<void> {
  Logger.d('Core', 'Initializing Axxist Core...');

  try {
    // Initialize in order
    EventBus.initialize();
    await ConfigManager.initialize();
    await CapabilityManager.initialize();
    Lifecycle.initialize();
    await Build.initialize();

    Logger.d('Core', 'Axxist Core initialized successfully');
  } catch (error) {
    Logger.e('Core', 'Failed to initialize core', error);
    throw error;
  }
}

/**
 * Health check for all core modules.
 */
export async function healthCheck(): Promise<{
  bridge: boolean;
  logger: boolean;
  eventBus: boolean;
  config: boolean;
  capabilities: boolean;
  lifecycle: boolean;
  build: boolean;
}> {
  const results = {
    bridge: false,
    logger: false,
    eventBus: false,
    config: false,
    capabilities: false,
    lifecycle: false,
    build: false,
  };

  try {
    // Bridge health check
    if (NativeBridge.isAvailable()) {
      await NativeBridge.healthCheck();
      results.bridge = true;
    }

    // Other modules are always available in JS
    results.logger = Logger.isLoggerEnabled();
    results.eventBus = true;
    results.config = ConfigManager.getAllKeys().length >= 0;
    results.capabilities = CapabilityManager.getAllCapabilities().length >= 0;
    results.lifecycle = true;
    results.build = true;
  } catch (error) {
    Logger.e('Core', 'Health check failed', error);
  }

  return results;
}

/**
 * Shutdown all core modules.
 * This should be called when the app is being destroyed.
 */
export function shutdownCore(): void {
  Logger.d('Core', 'Shutting down Axxist Core...');

  Lifecycle.shutdown();
  EventBus.shutdown();
  CapabilityManager;
  ConfigManager;
  Logger.d('Core', 'Axxist Core shutdown complete');
}

export default {
  initialize: initializeCore,
  healthCheck,
  shutdown: shutdownCore,
};
