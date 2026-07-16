/**
 * Logger - TypeScript logging module.
 * 
 * Provides a single logging system that mirrors the Android Logger.
 * Logs both to console and to the native bridge when available.
 */

import { NativeBridge } from '../nativebridge/NativeBridge';
import type { LogLevel } from '../types';

const PREFIX = 'Axxist';

class LoggerClass {
  private minimumLevel: LogLevel = 'DEBUG';
  private isEnabled: boolean = true;

  /**
   * Set the minimum log level.
   */
  setMinimumLevel(level: LogLevel): void {
    this.minimumLevel = level;
  }

  /**
   * Enable or disable the logger.
   */
  setEnabled(enabled: boolean): void {
    this.isEnabled = enabled;
  }

  /**
   * Check if the logger is enabled.
   */
  isLoggerEnabled(): boolean {
    return this.isEnabled;
  }

  /**
   * Get the minimum log level.
   */
  getMinimumLevel(): LogLevel {
    return this.minimumLevel;
  }

  /**
   * Log a debug message.
   */
  d(tag: string, message: string, ...args: unknown[]): void {
    this.log('DEBUG', tag, message, args);
  }

  /**
   * Log an info message.
   */
  i(tag: string, message: string, ...args: unknown[]): void {
    this.log('INFO', tag, message, args);
  }

  /**
   * Log a warning message.
   */
  w(tag: string, message: string, ...args: unknown[]): void {
    this.log('WARNING', tag, message, args);
  }

  /**
   * Log an error message.
   */
  e(tag: string, message: string, ...args: unknown[]): void {
    this.log('ERROR', tag, message, args);
  }

  /**
   * Internal log function.
   */
  private log(level: LogLevel, tag: string, message: string, args: unknown[]): void {
    if (!this.isEnabled || !this.shouldLog(level)) {
      return;
    }

    const fullTag = `${PREFIX}:${tag}`;
    const formattedMessage = args.length > 0 
      ? `${message} ${args.map(a => JSON.stringify(a)).join(' ')}`
      : message;

    // Console output
    const logFn = this.getConsoleLogFunction(level);
    logFn(`[${level}] [${fullTag}] ${formattedMessage}`);

    // Native bridge output
    if (NativeBridge.isAvailable()) {
      NativeBridge.log(level, tag, formattedMessage).catch(() => {
        // Ignore errors in logging
      });
    }
  }

  /**
   * Check if a level should be logged based on minimum level.
   */
  private shouldLog(level: LogLevel): boolean {
    const levels: LogLevel[] = ['DEBUG', 'INFO', 'WARNING', 'ERROR'];
    const currentIndex = levels.indexOf(this.minimumLevel);
    const levelIndex = levels.indexOf(level);
    return levelIndex >= currentIndex;
  }

  /**
   * Get the console log function for a level.
   */
  private getConsoleLogFunction(level: LogLevel): (message: string) => void {
    switch (level) {
      case 'DEBUG':
        return console.debug.bind(console);
      case 'INFO':
        return console.info.bind(console);
      case 'WARNING':
        return console.warn.bind(console);
      case 'ERROR':
        return console.error.bind(console);
      default:
        return console.log.bind(console);
    }
  }
}

export const Logger = new LoggerClass();
export default Logger;
