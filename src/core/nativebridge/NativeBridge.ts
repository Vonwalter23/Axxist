/**
 * NativeBridge - React Native wrapper for Android native module.
 * 
 * This module provides the communication layer between React Native and Kotlin.
 */

import { NativeModules, NativeEventEmitter, Platform } from 'react-native';
import type {
  AxxistBridgeInterface,
  BuildInfo,
  Config,
  Capability,
  HealthStatus,
  LogLevel,
} from '../types';

const { AxxistBridge } = NativeModules;

class NativeBridgeClass implements AxxistBridgeInterface {
  private eventEmitter: NativeEventEmitter | null = null;
  private listeners: Map<string, Set<Function>> = new Map();

  constructor() {
    if (Platform.OS === 'android' && AxxistBridge) {
      this.eventEmitter = new NativeEventEmitter(AxxistBridge);
    }
  }

  /**
   * Get the app version.
   */
  async getAppVersion(): Promise<string> {
    this.checkBridge();
    return AxxistBridge.getAppVersion();
  }

  /**
   * Get build information.
   */
  async getBuildInfo(): Promise<BuildInfo> {
    this.checkBridge();
    return AxxistBridge.getBuildInfo();
  }

  /**
   * Get a configuration value.
   */
  async getConfig(key: string): Promise<string> {
    this.checkBridge();
    return AxxistBridge.getConfig(key);
  }

  /**
   * Set a configuration value.
   */
  async setConfig(key: string, value: string): Promise<boolean> {
    this.checkBridge();
    return AxxistBridge.setConfig(key, value);
  }

  /**
   * Get all configuration values.
   */
  async getAllConfig(): Promise<Config> {
    this.checkBridge();
    return AxxistBridge.getAllConfig();
  }

  /**
   * Get all capabilities.
   */
  async getCapabilities(): Promise<Capability[]> {
    this.checkBridge();
    return AxxistBridge.getCapabilities();
  }

  /**
   * Check if a capability is available.
   */
  async isCapabilityAvailable(capabilityId: string): Promise<boolean> {
    this.checkBridge();
    return AxxistBridge.isCapabilityAvailable(capabilityId);
  }

  /**
   * Emit an event to native.
   */
  async emitEvent(
    eventName: string,
    data?: Record<string, unknown>,
  ): Promise<boolean> {
    this.checkBridge();
    return AxxistBridge.emitEvent(eventName, data || null);
  }

  /**
   * Log a message to native.
   */
  async log(level: LogLevel, tag: string, message: string): Promise<boolean> {
    this.checkBridge();
    return AxxistBridge.log(level, tag, message);
  }

  /**
   * Perform health check.
   */
  async healthCheck(): Promise<HealthStatus> {
    this.checkBridge();
    return AxxistBridge.healthCheck();
  }

  /**
   * Add an event listener for native events.
   */
  addEventListener(eventName: string, callback: Function): () => void {
    if (!this.eventEmitter) {
      console.warn('NativeBridge: Event emitter not available');
      return () => {};
    }

    if (!this.listeners.has(eventName)) {
      this.listeners.set(eventName, new Set());
    }
    this.listeners.get(eventName)?.add(callback);

    const subscription = this.eventEmitter.addListener(eventName, callback);

    return () => {
      subscription.remove();
      this.listeners.get(eventName)?.delete(callback);
    };
  }

  /**
   * Remove all listeners for an event.
   */
  removeAllListeners(eventName: string): void {
    if (!this.eventEmitter) {
      return;
    }

    this.eventEmitter.removeAllListeners(eventName);
    this.listeners.get(eventName)?.clear();
  }

  /**
   * Check if the native bridge is available.
   */
  isAvailable(): boolean {
    return Platform.OS === 'android' && AxxistBridge != null;
  }

  /**
   * Check if the bridge is initialized and throw if not.
   */
  private checkBridge(): void {
    if (!this.isAvailable()) {
      throw new Error(
        'NativeBridge: AxxistBridge module is not available. ' +
          'Make sure you are running on Android.',
      );
    }
  }
}

export const NativeBridge = new NativeBridgeClass();
export default NativeBridge;
