# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/).

## [0.0.5-wakeword-framework] - 2024-07-16

### Agregado

**Wake Word Framework (STAGE_04):**
- WakeWordManager - Coordinator central del sistema de wake word
- WakeWordStateManager - State machine con 5 estados (DISABLED, INITIALIZING, LISTENING, DETECTED, ERROR)
- WakeWordState - Enum de estados de wake word
- WakeWordProvider - Interfaz para motores de detección de wake word
- Wake Word Events: WAKE_WORD_INITIALIZING, WAKE_WORD_READY, WAKE_WORD_LISTENING, WAKE_WORD_DETECTED, WAKE_WORD_STOPPED, WAKE_WORD_ERROR
- Capability WAKE_WORD_FRAMEWORK
- Integración con RuntimeManager

### Proveedores Preparados (Futuros)
- Porcupine (Picovoice)
- Snowboy
- TensorFlow Lite
- Whisper Keyword Detection
- Custom models

### Características Técnicas
- **Arquitectura desacoplada**: WakeWordProvider permite diferentes implementaciones
- **State Machine**: Transiciones validadas
- **Consume Audio Core**: No duplica captura de audio
- **Provider opcional**: Funciona sin proveedor para testing

### Restricciones Cumplidas
- ✅ Sin Porcupine
- ✅ Sin Snowboy
- ✅ Sin TFLite models
- ✅ Sin reconocimiento de comandos
- ✅ Sin IA

---

## [0.0.4-audio-core] - 2024-07-16

### Agregado

**Audio Core Foundation (STAGE_03):**
- AudioManager - Coordinator central del sistema de audio
- AudioStateManager - State machine con 6 estados (IDLE, INITIALIZING, LISTENING, PROCESSING, STOPPING, ERROR)
- AudioState - Enum de estados de audio
- AudioInputProvider - Interfaz para entrada de audio
- SpeechRecognizerProvider - Interfaz para reconocimiento de voz
- VoiceActivityDetector - Interfaz para detección de actividad de voz
- AudioException - Exception classes para errores de audio
- Audio Events: AUDIO_INITIALIZING, AUDIO_STARTED, AUDIO_STOPPED, AUDIO_ERROR, VOICE_ACTIVITY_DETECTED
- Integración con RuntimeManager

### Permisos Agregados
- RECORD_AUDIO

### Características Técnicas
- **Arquitectura desacoplada**: Interfaces permiten diferentes implementaciones
- **State Machine**: Transiciones validadas
- **Permisos preparados**: RECORD_AUDIO en AndroidManifest
- **Integración Runtime**: Audio se detiene con Runtime

### Restricciones Cumplidas
- ✅ Sin Wake Word
- ✅ Sin Speech Recognition funcional
- ✅ Sin Text To Speech
- ✅ Sin IA
- ✅ Sin proveedores externos

---

## [0.0.3-runtime] - 2024-07-16

### Agregado

**Runtime Foundation (STAGE_02):**
- AssistantService - Foreground Service para operación permanente
- RuntimeManager - Coordinator central del Runtime
- RuntimeStateManager - State machine con 6 estados
- HealthMonitor - Monitor de salud del Runtime
- BootReceiver - BroadcastReceiver para auto-inicio
- Notification Channel "axxist_runtime_channel"
- Notificación permanente "Axxist está preparado"
- Nuevos eventos Runtime (RUNTIME_STARTED, RUNTIME_STOPPED, etc.)
- Nuevos eventos Service (SERVICE_CREATED, SERVICE_DESTROYED, etc.)
- Interfaces preparadas: VoiceManager, AIManager, MemoryManager

### Permisos Agregados
- FOREGROUND_SERVICE
- FOREGROUND_SERVICE_MICROPHONE
- FOREGROUND_SERVICE_MEDIA_PLAYBACK
- FOREGROUND_SERVICE_SPECIAL_USE
- RECEIVE_BOOT_COMPLETED
- WAKE_LOCK
- POST_NOTIFICATIONS

### Características Técnicas
- **Foreground Service**: START_STICKY para recuperación
- **Android 14+**: Compatible con foregroundServiceType
- **State Machine**: STOPPED → STARTING → RUNNING → PAUSED → STOPPING → STOPPED
- **Health Monitoring**: Verificación cada 60 segundos
- **Auto-inicio**: Preparado, no automático sin consentimiento

### Restricciones Cumplidas
- ✅ Sin Wake Word
- ✅ Sin Speech Recognition
- ✅ Sin Text To Speech
- ✅ Sin Groq o IA
- ✅ Sin integraciones externas

---

## [0.0.2-android-core] - 2024-07-16

### Agregado

**Production Foundation (STAGE_01.5):**
- Configuración release preparada en build.gradle
- Estructura signing segura con keystore.properties
- Template keystore.properties.example
- .gitignore actualizado para excluir secrets
- VersionCode y VersionName actualizados
- Separación debug/release en buildTypes
- Minify y shrinkResources habilitado para release
- BuildConfig habilitado
- Multidex support agregado
- gradle.properties con metadata de build
- docs/RELEASE_PROCESS.md - Guía completa de release
- BuildConfiguration actualizado con más metadata

### Características Técnicas
- **React Native**: 0.76.6
- **Kotlin**: 2.1.0
- **Min SDK**: Android 10 (API 29)
- **Target SDK**: Android 14 (API 34)
- **Package**: com.axxist.app
- **Arquitectura**: Modular por stages

### Seguridad
- ✅ Secrets fuera de Git (keystore.properties)
- ✅ Debug keystore separado
- ✅ Release signing configurable
- ✅ Fallback a debug signing si no hay keystore

---

## [0.0.2-android-core] - 2024-07-16

### Agregado

**Android Core Modules (Kotlin):**
- AndroidCore - Punto de entrada central
- Logger - Sistema de logging centralizado con niveles DEBUG/INFO/WARNING/ERROR
- EventBus - Sistema de eventos interno
- ConfigManager - Gestión centralizada de configuración con SharedPreferences
- PermissionManager - Infraestructura para permisos (preparado, no activo)
- CapabilityManager - Registro de 15 capacidades futuras
- NativeBridge - Comunicación React Native ↔ Kotlin
- AppLifecycle - Gestión del ciclo de vida
- BuildConfiguration - Configuración de build y versión

**React Native Core Modules (TypeScript):**
- src/core/types - Definiciones de tipos TypeScript
- src/core/logger - Logger para JavaScript
- src/core/eventbus - EventBus para JavaScript
- src/core/config - ConfigManager para JavaScript
- src/core/capability - CapabilityManager para JavaScript
- src/core/lifecycle - Lifecycle para JavaScript
- src/core/build - Build info para JavaScript
- src/core/nativebridge - Wrapper para Native Module
- src/core/index - Exportaciones principales

**Dependencias:**
- kotlinx-coroutines-android: 1.7.3
- kotlinx-coroutines-core: 1.7.3

### Restricciones Cumplidas

- ✅ Sin Foreground Service
- ✅ Sin Wake Word
- ✅ Sin Speech To Text / Text To Speech
- ✅ Sin Groq o IA
- ✅ Sin integraciones de terceros
- ✅ Sin permisos activos

---

## [0.0.1-foundation] - 2024-07-16

### Agregado

- Estructura básica del proyecto React Native
- Configuración de TypeScript
- Proyecto Android nativo configurado
- JDK 17 configurado
- Android SDK 34 configurado
- Gradle 8.5 configurado
- Kotlin 2.1.0 configurado
- Hermes Engine habilitado
- Componente principal App.tsx básico
- APK debug compilado exitosamente
- README.md con documentación del proyecto
- Configuración de linting con ESLint
- Configuración de formateo con Prettier
- Configuración de Android (build.gradle, AndroidManifest.xml)
- Recursos de Android (colors, strings, themes)
- Icono de launcher configurado
- FASE_00_REPORT.md con auditoría técnica

### Verificaciones Completadas

- ✅ Sin integración funcional con IA local
- ✅ Sin integración funcional con Groq
- ✅ Foreground Service solo como infraestructura
- ✅ Sin solicitud de permisos en primer inicio
- ✅ Dependencias no utilizadas eliminadas
- ✅ Código mínimo y limpio

---

## Notas de Versión Futuras

Las versiones futuras seguirán el formato:

```
## [X.Y.Z] - YYYY-MM-DD

### Agregado
- Nuevas características

### Cambiado
- Cambios en funcionalidades existentes

### Deprecated
- Funcionalidades que serán removidas

### Removido
- Funcionalidades removidas

### Fixed
- Correcciones de bugs

### Security
- Cambios de seguridad
```
