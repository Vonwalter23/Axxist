/**
 * CapabilityManager - TypeScript capability management.
 * 
 * Mirrors the Android CapabilityManager for capability checking.
 */

import { NativeBridge } from '../nativebridge/NativeBridge';
import { Logger } from '../logger';
import type { Capability, CapabilityStatus, CapabilityIds as CapabilityIdsType } from '../types';
import { CapabilityIds } from '../types';

class CapabilityManagerClass {
  private capabilities: Map<string, Capability> = new Map();
  private listeners: Set<(capabilityId: string, status: CapabilityStatus) => void> = new Set();

  /**
   * Initialize the CapabilityManager.
   */
  async initialize(): Promise<void> {
    await this.loadCapabilities();
    Logger.d('CapabilityManager', 'CapabilityManager initialized');
  }

  /**
   * Load all capabilities from native.
   */
  private async loadCapabilities(): Promise<void> {
    try {
      const caps = await NativeBridge.getCapabilities();
      this.capabilities.clear();
      caps.forEach((cap: Capability) => {
        this.capabilities.set(cap.id, cap);
      });
      Logger.d('CapabilityManager', `Loaded ${this.capabilities.size} capabilities`);
    } catch (error) {
      Logger.e('CapabilityManager', 'Failed to load capabilities', error);
    }
  }

  /**
   * Get all capabilities.
   */
  getAllCapabilities(): Capability[] {
    return Array.from(this.capabilities.values());
  }

  /**
   * Get a capability by ID.
   */
  getCapability(id: string): Capability | undefined {
    return this.capabilities.get(id);
  }

  /**
   * Check if a capability is available.
   */
  async isAvailable(id: string): Promise<boolean> {
    const cap = this.capabilities.get(id);
    if (cap) {
      return cap.status === 'AVAILABLE';
    }

    // If not in cache, check native
    try {
      return await NativeBridge.isCapabilityAvailable(id);
    } catch (error) {
      Logger.e('CapabilityManager', `Failed to check capability: ${id}`, error);
      return false;
    }
  }

  /**
   * Get capabilities by status.
   */
  getByStatus(status: CapabilityStatus): Capability[] {
    return this.getAllCapabilities().filter(cap => cap.status === status);
  }

  /**
   * Get available capabilities.
   */
  getAvailable(): Capability[] {
    return this.getByStatus('AVAILABLE');
  }

  /**
   * Get not implemented capabilities.
   */
  getNotImplemented(): Capability[] {
    return this.getByStatus('NOT_IMPLEMENTED');
  }

  /**
   * Subscribe to capability changes.
   */
  subscribe(callback: (capabilityId: string, status: CapabilityStatus) => void): () => void {
    this.listeners.add(callback);
    return () => {
      this.listeners.delete(callback);
    };
  }

  /**
   * Get summary of capabilities by status.
   */
  getSummary(): Record<CapabilityStatus, number> {
    const summary: Record<CapabilityStatus, number> = {
      AVAILABLE: 0,
      UNAVAILABLE: 0,
      DISABLED: 0,
      NOT_IMPLEMENTED: 0,
    };

    this.getAllCapabilities().forEach(cap => {
      summary[cap.status]++;
    });

    return summary;
  }

  /**
   * Log capabilities status.
   */
  logStatus(): void {
    const summary = this.getSummary();
    Logger.d('CapabilityManager', 'Capabilities Status:');
    Object.entries(summary).forEach(([status, count]) => {
      Logger.d('CapabilityManager', `  ${status}: ${count}`);
    });
  }

  /**
   * Get capability IDs constants.
   */
  getIds(): CapabilityIdsType {
    return CapabilityIds;
  }
}

export const CapabilityManager = new CapabilityManagerClass();
export default CapabilityManager;

export { CapabilityIds };
