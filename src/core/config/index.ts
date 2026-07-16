/**
 * ConfigManager - TypeScript configuration management.
 * 
 * Mirrors the Android ConfigManager for cross-platform configuration.
 */

import { NativeBridge } from '../nativebridge/NativeBridge';
import { Logger } from '../logger';
import { EventBus, ConfigEvents } from '../eventbus';
import { ConfigKeys, FeatureFlags } from '../types';
import type { Config, FeatureFlags as FeatureFlagsType } from '../types';

class ConfigManagerClass {
  private cache: Map<string, string> = new Map();
  private listeners: Map<string, Set<(value: string) => void>> = new Map();

  /**
   * Initialize the ConfigManager.
   */
  async initialize(): Promise<void> {
    await this.loadAll();
    
    // Listen for config updates from native
    EventBus.subscribe(ConfigEvents.KEY_UPDATED, (event) => {
      const key = event.data?.key as string;
      if (key && this.cache.has(key)) {
        // Refresh the value
        this.get(key).then(value => {
          this.notifyListeners(key, value);
        });
      }
    });

    Logger.d('ConfigManager', 'ConfigManager initialized');
  }

  /**
   * Load all configuration from native.
   */
  private async loadAll(): Promise<void> {
    try {
      const config = await NativeBridge.getAllConfig();
      this.cache.clear();
      Object.entries(config).forEach(([key, value]) => {
        this.cache.set(key, value);
      });
      Logger.d('ConfigManager', `Loaded ${this.cache.size} config values`);
    } catch (error) {
      Logger.e('ConfigManager', 'Failed to load config', error);
    }
  }

  /**
   * Get a string value.
   */
  async get(key: string): Promise<string> {
    if (this.cache.has(key)) {
      return this.cache.get(key)!;
    }

    try {
      const value = await NativeBridge.getConfig(key);
      this.cache.set(key, value);
      return value;
    } catch (error) {
      Logger.e('ConfigManager', `Failed to get config: ${key}`, error);
      return '';
    }
  }

  /**
   * Set a string value.
   */
  async set(key: string, value: string): Promise<void> {
    try {
      await NativeBridge.setConfig(key, value);
      this.cache.set(key, value);
      this.notifyListeners(key, value);
      Logger.d('ConfigManager', `Config set: ${key}`);
    } catch (error) {
      Logger.e('ConfigManager', `Failed to set config: ${key}`, error);
      throw error;
    }
  }

  /**
   * Get a boolean value.
   */
  async getBoolean(key: string, defaultValue: boolean = false): Promise<boolean> {
    const value = await this.get(key);
    if (value === '') {
      return defaultValue;
    }
    return value === 'true' || value === '1';
  }

  /**
   * Set a boolean value.
   */
  async setBoolean(key: string, value: boolean): Promise<void> {
    await this.set(key, value ? 'true' : 'false');
  }

  /**
   * Get an integer value.
   */
  async getInt(key: string, defaultValue: number = 0): Promise<number> {
    const value = await this.get(key);
    if (value === '') {
      return defaultValue;
    }
    const parsed = parseInt(value, 10);
    return isNaN(parsed) ? defaultValue : parsed;
  }

  /**
   * Set an integer value.
   */
  async setInt(key: string, value: number): Promise<void> {
    await this.set(key, value.toString());
  }

  /**
   * Check if a key exists.
   */
  has(key: string): boolean {
    return this.cache.has(key);
  }

  /**
   * Remove a key.
   */
  async remove(key: string): Promise<void> {
    await this.set(key, '');
    this.cache.delete(key);
  }

  /**
   * Get all keys.
   */
  getAllKeys(): string[] {
    return Array.from(this.cache.keys());
  }

  /**
   * Get all configuration as an object.
   */
  getAll(): Config {
    return Object.fromEntries(this.cache);
  }

  /**
   * Subscribe to config changes.
   */
  subscribe(key: string, callback: (value: string) => void): () => void {
    if (!this.listeners.has(key)) {
      this.listeners.set(key, new Set());
    }
    this.listeners.get(key)?.add(callback);

    return () => {
      this.listeners.get(key)?.delete(callback);
    };
  }

  /**
   * Notify listeners of a change.
   */
  private notifyListeners(key: string, value: string): void {
    const callbacks = this.listeners.get(key);
    if (callbacks) {
      callbacks.forEach(callback => {
        try {
          callback(value);
        } catch (error) {
          Logger.e('ConfigManager', `Error in config listener for ${key}`, error);
        }
      });
    }
  }

  /**
   * Check if a feature is enabled.
   */
  async isFeatureEnabled(feature: string): Promise<boolean> {
    const key = this.getFeatureKey(feature);
    if (!key) {
      return false;
    }
    return this.getBoolean(key, false);
  }

  /**
   * Enable or disable a feature.
   */
  async setFeatureEnabled(feature: string, enabled: boolean): Promise<void> {
    const key = this.getFeatureKey(feature);
    if (!key) {
      throw new Error(`Unknown feature: ${feature}`);
    }
    await this.setBoolean(key, enabled);
  }

  /**
   * Get the config key for a feature.
   */
  private getFeatureKey(feature: string): string | null {
    const featureFlags = FeatureFlags as FeatureFlagsType;
    const keys = Object.entries(featureFlags);
    const entry = keys.find(([_, value]) => value === feature);
    if (!entry) {
      return null;
    }
    // Convert snake_case to feature_snake_case
    return `feature_${feature}`;
  }
}

export const ConfigManager = new ConfigManagerClass();
export default ConfigManager;

export { ConfigKeys, FeatureFlags };
