/**
 * Axxist Core Types - TypeScript definitions for the Android Core.
 */

// ============ Build Info ============

export interface BuildInfo {
  version: string;
  stage: string;
  debug: boolean;
}

// ============ Configuration ============

export interface Config {
  [key: string]: string;
}

export interface ConfigKeys {
  LOG_LEVEL: string;
  DEBUG_MODE: string;
  FIRST_LAUNCH: string;
  APP_VERSION: string;
  FEATURE_WAKE_WORD: string;
  FEATURE_VOICE: string;
  FEATURE_AI: string;
  FEATURE_SPOTIFY: string;
  FEATURE_WHATSAPP: string;
  FEATURE_EMAIL: string;
  FEATURE_CALENDAR: string;
  FEATURE_CONTACTS: string;
  FEATURE_PHONE: string;
}

export const ConfigKeys: ConfigKeys = {
  LOG_LEVEL: 'log_level',
  DEBUG_MODE: 'debug_mode',
  FIRST_LAUNCH: 'first_launch',
  APP_VERSION: 'app_version',
  FEATURE_WAKE_WORD: 'feature_wake_word',
  FEATURE_VOICE: 'feature_voice',
  FEATURE_AI: 'feature_ai',
  FEATURE_SPOTIFY: 'feature_spotify',
  FEATURE_WHATSAPP: 'feature_whatsapp',
  FEATURE_EMAIL: 'feature_email',
  FEATURE_CALENDAR: 'feature_calendar',
  FEATURE_CONTACTS: 'feature_contacts',
  FEATURE_PHONE: 'feature_phone',
};

// ============ Capabilities ============

export type CapabilityStatus =
  | 'AVAILABLE'
  | 'UNAVAILABLE'
  | 'DISABLED'
  | 'NOT_IMPLEMENTED';

export interface Capability {
  id: string;
  name: string;
  description: string;
  status: CapabilityStatus;
  requiredPermissions: string[];
}

export interface CapabilityIds {
  VOICE_RECOGNITION: string;
  TEXT_TO_SPEECH: string;
  WAKE_WORD: string;
  PHONE_CALL: string;
  CONTACTS: string;
  SMS: string;
  WHATSAPP: string;
  EMAIL: string;
  SPOTIFY: string;
  MEDIA_CONTROL: string;
  CALENDAR: string;
  AI_GROQ: string;
  AI_LOCAL: string;
  LOCATION: string;
  CAMERA: string;
}

export const CapabilityIds: CapabilityIds = {
  VOICE_RECOGNITION: 'voice_recognition',
  TEXT_TO_SPEECH: 'text_to_speech',
  WAKE_WORD: 'wake_word',
  PHONE_CALL: 'phone_call',
  CONTACTS: 'contacts',
  SMS: 'sms',
  WHATSAPP: 'whatsapp',
  EMAIL: 'email',
  SPOTIFY: 'spotify',
  MEDIA_CONTROL: 'media_control',
  CALENDAR: 'calendar',
  AI_GROQ: 'ai_groq',
  AI_LOCAL: 'ai_local',
  LOCATION: 'location',
  CAMERA: 'camera',
};

// ============ Feature Flags ============

export interface FeatureFlags {
  WAKE_WORD: string;
  VOICE: string;
  AI: string;
  SPOTIFY: string;
  WHATSAPP: string;
  EMAIL: string;
  CALENDAR: string;
  CONTACTS: string;
  PHONE: string;
}

export const FeatureFlags: FeatureFlags = {
  WAKE_WORD: 'wake_word',
  VOICE: 'voice',
  AI: 'ai',
  SPOTIFY: 'spotify',
  WHATSAPP: 'whatsapp',
  EMAIL: 'email',
  CALENDAR: 'calendar',
  CONTACTS: 'contacts',
  PHONE: 'phone',
};

// ============ Event Types ============

export type EventType =
  | 'app_started'
  | 'app_stopped'
  | 'app_paused'
  | 'app_resumed'
  | 'module_loaded'
  | 'module_error'
  | 'module_unloaded'
  | 'config_updated'
  | 'config_key_updated';

export interface AppEvent {
  type: EventType;
  data?: Record<string, unknown>;
}

// ============ Health Check ============

export interface HealthStatus {
  core: boolean;
  eventBus: boolean;
  config: boolean;
  capabilities: boolean;
  bridge: boolean;
  status: 'healthy' | 'unhealthy';
}

// ============ Log Levels ============

export type LogLevel = 'DEBUG' | 'INFO' | 'WARNING' | 'ERROR';

// ============ Native Bridge Interface ============

export interface AxxistBridgeInterface {
  getAppVersion(): Promise<string>;
  getBuildInfo(): Promise<BuildInfo>;
  getConfig(key: string): Promise<string>;
  setConfig(key: string, value: string): Promise<boolean>;
  getAllConfig(): Promise<Config>;
  getCapabilities(): Promise<Capability[]>;
  isCapabilityAvailable(capabilityId: string): Promise<boolean>;
  emitEvent(eventName: string, data?: Record<string, unknown>): Promise<boolean>;
  log(level: LogLevel, tag: string, message: string): Promise<boolean>;
  healthCheck(): Promise<HealthStatus>;
}
