/**
 * Build - Build information module.
 */

export interface BuildInfo {
  versionName: string;
  versionCode: number;
  isDebug: boolean;
  buildTime: string;
}

class BuildModule {
  private static instance: BuildModule;
  
  private buildInfo: BuildInfo = {
    versionName: '0.0.9-action-framework',
    versionCode: 9,
    isDebug: true,
    buildTime: new Date().toISOString(),
  };

  private constructor() {}

  static getInstance(): BuildModule {
    if (!BuildModule.instance) {
      BuildModule.instance = new BuildModule();
    }
    return BuildModule.instance;
  }

  async initialize(): Promise<void> {
    // In a real app, this would get info from native module
    return Promise.resolve();
  }

  getVersionName(): string {
    return this.buildInfo.versionName;
  }

  getVersionCode(): number {
    return this.buildInfo.versionCode;
  }

  isDebugBuild(): boolean {
    return this.buildInfo.isDebug;
  }

  getSummary(): string {
    return `v${this.buildInfo.versionName} (${this.buildInfo.versionCode})`;
  }

  getInfo(): BuildInfo {
    return { ...this.buildInfo };
  }
}

export const Build = BuildModule.getInstance();

export default Build;
