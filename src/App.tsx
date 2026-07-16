/**
 * Axxist - Intelligent Assistant for Android
 * Android Core Stage - Version 0.0.2
 */

import React, { useEffect, useState } from 'react';
import {
  StatusBar,
  Text,
  View,
  StyleSheet,
  ScrollView,
  ActivityIndicator,
} from 'react-native';
import {
  initializeCore,
  healthCheck,
  Build,
  CapabilityManager,
  Logger,
} from './core';

const App: React.FC = () => {
  const [isReady, setIsReady] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [health, setHealth] = useState<Record<string, boolean>>({});
  const [capabilitiesCount, setCapabilitiesCount] = useState(0);

  useEffect(() => {
    const init = async () => {
      try {
        Logger.d('App', 'Starting initialization...');
        await initializeCore();
        
        const healthStatus = await healthCheck();
        setHealth(healthStatus);
        
        const caps = CapabilityManager.getAllCapabilities();
        setCapabilitiesCount(caps.length);
        
        setIsReady(true);
        Logger.d('App', 'Initialization complete');
      } catch (err) {
        const message = err instanceof Error ? err.message : 'Unknown error';
        setError(message);
        Logger.e('App', 'Initialization failed', err);
      }
    };

    init();
  }, []);

  if (error) {
    return (
      <View style={styles.container}>
        <StatusBar barStyle="dark-content" backgroundColor="#FFEBEE" />
        <View style={styles.content}>
          <Text style={styles.title}>Axxist</Text>
          <Text style={styles.errorTitle}>Error de Inicialización</Text>
          <Text style={styles.errorText}>{error}</Text>
        </View>
      </View>
    );
  }

  if (!isReady) {
    return (
      <View style={styles.container}>
        <StatusBar barStyle="dark-content" backgroundColor="#FFFFFF" />
        <View style={styles.content}>
          <Text style={styles.title}>Axxist</Text>
          <ActivityIndicator size="large" color="#6200EE" style={styles.loader} />
          <Text style={styles.loadingText}>Inicializando...</Text>
        </View>
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <StatusBar barStyle="dark-content" backgroundColor="#FFFFFF" />
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <View style={styles.header}>
          <Text style={styles.title}>Axxist</Text>
          <Text style={styles.subtitle}>Android Core</Text>
          <Text style={styles.version}>{Build.getSummary()}</Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Estado del Sistema</Text>
          <View style={styles.statusGrid}>
            <StatusItem label="Bridge" status={health.bridge} />
            <StatusItem label="Logger" status={health.logger} />
            <StatusItem label="EventBus" status={health.eventBus} />
            <StatusItem label="Config" status={health.config} />
            <StatusItem label="Capabilities" status={health.capabilities} />
            <StatusItem label="Lifecycle" status={health.lifecycle} />
            <StatusItem label="Build" status={health.build} />
          </View>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Capabilities</Text>
          <Text style={styles.capabilitiesText}>
            {capabilitiesCount} capabilities registradas
          </Text>
        </View>

        <View style={styles.section}>
          <Text style={styles.sectionTitle}>Modules</Text>
          <ModuleItem name="AndroidCore" status="Ready" />
          <ModuleItem name="Logger" status="Active" />
          <ModuleItem name="EventBus" status="Active" />
          <ModuleItem name="ConfigManager" status="Active" />
          <ModuleItem name="CapabilityManager" status="Active" />
          <ModuleItem name="LifecycleManager" status="Active" />
          <ModuleItem name="NativeBridge" status="Connected" />
        </View>

        <View style={styles.section}>
          <Text style={styles.readyText}>✅ Sistema Listo</Text>
          <Text style={styles.readySubtext}>
            Android Core STAGE_01 instalado correctamente
          </Text>
        </View>
      </ScrollView>
    </View>
  );
};

const StatusItem: React.FC<{ label: string; status: boolean }> = ({
  label,
  status,
}) => (
  <View style={styles.statusItem}>
    <View
      style={[
        styles.statusDot,
        { backgroundColor: status ? '#4CAF50' : '#F44336' },
      ]}
    />
    <Text style={styles.statusLabel}>{label}</Text>
  </View>
);

const ModuleItem: React.FC<{ name: string; status: string }> = ({
  name,
  status,
}) => (
  <View style={styles.moduleItem}>
    <Text style={styles.moduleName}>{name}</Text>
    <Text style={styles.moduleStatus}>{status}</Text>
  </View>
);

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F5F5F5',
  },
  scrollContent: {
    padding: 20,
  },
  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  header: {
    alignItems: 'center',
    marginBottom: 30,
    paddingVertical: 20,
  },
  title: {
    fontSize: 48,
    fontWeight: 'bold',
    color: '#6200EE',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 20,
    color: '#333333',
    marginBottom: 8,
  },
  version: {
    fontSize: 14,
    color: '#666666',
  },
  loader: {
    marginTop: 20,
  },
  loadingText: {
    marginTop: 10,
    fontSize: 16,
    color: '#666666',
  },
  section: {
    backgroundColor: '#FFFFFF',
    borderRadius: 12,
    padding: 16,
    marginBottom: 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: '600',
    color: '#333333',
    marginBottom: 12,
  },
  statusGrid: {
    flexDirection: 'row',
    flexWrap: 'wrap',
  },
  statusItem: {
    flexDirection: 'row',
    alignItems: 'center',
    width: '50%',
    paddingVertical: 8,
  },
  statusDot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    marginRight: 8,
  },
  statusLabel: {
    fontSize: 14,
    color: '#666666',
  },
  capabilitiesText: {
    fontSize: 14,
    color: '#666666',
  },
  moduleItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 8,
    borderBottomWidth: 1,
    borderBottomColor: '#EEEEEE',
  },
  moduleName: {
    fontSize: 14,
    color: '#333333',
  },
  moduleStatus: {
    fontSize: 14,
    color: '#4CAF50',
  },
  readyText: {
    fontSize: 18,
    fontWeight: '600',
    color: '#4CAF50',
    textAlign: 'center',
  },
  readySubtext: {
    fontSize: 14,
    color: '#666666',
    textAlign: 'center',
    marginTop: 8,
  },
  errorTitle: {
    fontSize: 18,
    fontWeight: '600',
    color: '#D32F2F',
    marginTop: 10,
    marginBottom: 8,
  },
  errorText: {
    fontSize: 14,
    color: '#D32F2F',
    textAlign: 'center',
  },
});

export default App;
