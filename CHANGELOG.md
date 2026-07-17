# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/).

## [1.0.0-quality-gate] - 2024-07-17

### Agregado

**Infrastructure - Quality Gate CI/CD:**

**GitHub Actions Workflow:**
- `.github/workflows/android-quality-gate.yml` - Workflow completo de validación

### Validado

**Build Validation:**
- ✅ assembleDebug - PASS (app-debug.apk: 29.4 MB)
- ✅ assembleRelease - PASS (app-release.apk: 14.3 MB)
- ✅ lintDebug - PASS

**Artifacts Generados:**
- ✅ app-debug-apk (29.4 MB)
- ✅ app-release-apk (14.3 MB)
- ✅ lint-report (1.0 KB)
- ✅ build-summary (415 bytes)
- ✅ build-debug-log (1.4 KB)
- ✅ build-release-log (1.4 KB)

**Workflow Run:**
- ✅ Run ID: 29588855650
- ✅ Conclusion: SUCCESS
- ✅ Commit: f073ca7

**Runtime Validation:**
- ⏭️ Opcional (habilitable via workflow_dispatch)
- Emulator Boot (Android 14, API 34)
- APK Installation via adb
- App Launch via monkey
- Runtime Duration (180 segundos)
- Crash Detection (FATAL EXCEPTION, AndroidRuntime)

**Documentación:**
- `docs/DEVELOPMENT_POLICY.md` - Políticas de desarrollo con Quality Gate
- `docs/GITHUB_BRANCH_PROTECTION.md` - Configuración de Branch Protection

**Required Status Check:**
- Preparado para funcionar como check obligatorio
- Documentación de configuración manual incluida

### Actualizado

- `README.md` - Agregada sección de Quality Gate
- `docs/PROJECT_STATE.md` - Actualizado con infraestructura

### Características Técnicas

- **Arquitectura**: Jobs separados para build, runtime opcional y report
- **Cache**: Gradle cache
- **Build-only mode**: Por defecto para CI/CD confiable
- **Runtime optional**: Habilitable via workflow_dispatch

### Restricciones Cumplidas

- ✅ No modifica arquitectura existente
- ✅ No modifica lógica de negocio
- ✅ No elimina código existente
- ✅ Compatible con STAGE_00 a STAGE_08

---

## [0.0.9-action-framework] - 2024-07-16

### Agregado

**Action Framework (STAGE_08):**
- ActionManager - Coordinator principal
- ActionRegistry - Registro de 18 acciones base
- ActionRouter - Selección de mecanismo (Android/Local/External/Hybrid)
- ActionValidator - Validación de requests
- ActionExecutor - Infraestructura (sin ejecución real)
- PermissionChecker - Infraestructura de permisos
- RetryManager - Política de reintentos
- ActionDiagnosticsCollector - Diagnósticos

**Models:**
- Action, ActionDefinition, ActionRequest, ActionResult
- ActionError, ActionMetadata, RetryPolicy

**Enums:**
- ActionState (IDLE, QUEUED, VALIDATING, EXECUTING, COMPLETED, FAILED, CANCELLED, ERROR)
- ActionPriority (LOW, NORMAL, HIGH, CRITICAL)
- ActionCategory (SYSTEM, MEDIA, COMMUNICATION, DEVICE, SEARCH, PRODUCTIVITY, AUTOMATION, AI, CUSTOM)
- ActionStatus, ActionType

**Events:**
- ActionEvent sealed class con 8 tipos de eventos
- Event types: ACTION_QUEUED, ACTION_STARTED, ACTION_COMPLETED, etc.

**Actions registradas:**
- OPEN_APP, CLOSE_APP
- MAKE_CALL, SEND_SMS, SEND_WHATSAPP, SEND_EMAIL
- PLAY_MUSIC, STOP_MUSIC, SET_VOLUME
- NAVIGATE_TO, SEARCH_NEARBY
- CREATE_REMINDER, CREATE_ALARM, CREATE_NOTE
- TOGGLE_WIFI, TOGGLE_BLUETOOTH, TOGGLE_FLASHLIGHT
- TAKE_PHOTO
- SEARCH_WEB, SEARCH_CONTACTS

### Características Técnicas
- **Arquitectura desacoplada**: Interfaces para providers
- **State Machine**: 8 estados con transiciones validadas
- **Permission infrastructure**: Sin requests reales
- **Retry policies**: NONE, IMMEDIATE, DEFERRED, EXPONENTIAL
- **Integration**: RuntimeManager actualizado con Action subsystem
- **Capability**: ACTION_FRAMEWORK disponible

### Restricciones Cumplidas
- ✅ Sin ejecución de acciones Android
- ✅ Sin apertura de aplicaciones reales
- ✅ Sin uso de APIs reales
- ✅ Infraestructura lista para expansión

---

## [0.0.8-intent-framework] - 2024-07-16

### Agregado

**Intent Engine (STAGE_07):**
- IntentManager - Coordinator principal del Intent Framework
- IntentRouter - Selección de método (Rule/AI/Hybrid)
- IntentRegistry - Registro de intents disponibles (10 intents base)
- IntentValidator - Validación de intents y entidades
- EntityExtractor - Extracción de entidades (regex-based)
- ConfidenceEvaluator - Evaluación de niveles de confianza
- IntentDiagnosticsCollector - Diagnósticos internos
- IntentStateManager - State machine con 7 estados
- IntentProcessor interfaces (Rule, AI, Hybrid)

**Models:**
- IntentCategory (10 categorías: SYSTEM, DEVICE, MEDIA, etc.)
- EntityType (21 tipos de entidades)
- ConfidenceLevel (VERY_LOW a VERY_HIGH)
- Intent, Entity, IntentResult, ValidationResult, IntentDiagnostics

**Events:**
- IntentEvent sealed class con 9 tipos de eventos
- Event types: INTENT_ANALYSIS_STARTED, INTENT_MATCHED, etc.

**Intents base registrados:**
- CALL_CONTACT
- SEND_WHATSAPP
- SEND_EMAIL
- PLAY_SPOTIFY
- NAVIGATE_TO
- CREATE_REMINDER
- CREATE_ALARM
- OPEN_APP
- SEARCH_WEB
- READ_NOTIFICATIONS

### Características Técnicas
- **Arquitectura desacoplada**: Interfaces para providers
- **State Machine**: Transiciones validadas
- **Entity extraction**: Regex patterns para Phone, Email, URL, Time, Date
- **Confidence evaluation**: Multi-factor calculation
- **Integration**: RuntimeManager actualizado con Intent subsystem
- **Capability**: INTENT_ENGINE disponible

### Restricciones Cumplidas
- ✅ Sin ejecución de acciones Android
- ✅ Sin automaciones reales
- ✅ Sin apertura de aplicaciones
- ✅ Sin IA real
- ✅ Arquitectura preparada para expansión

---

## [0.0.7-ai-router] - 2024-07-16

### Agregado

**AI Router (STAGE_06):**
- AIManager - Coordinator principal de AI
- AIRouter - Router de providers con selección y fallback
- AIConfiguration - Configuración de providers
- AIStateManager - State machine con 5 estados
- AIState - Enum de estados
- AIProvider placeholders:
  - GroqProvider - Groq API
  - GeminiProvider - Google Gemini
  - OpenAIProvider - OpenAI
  - LocalAIProvider - LLM local
- AI Events: AI_REQUEST_STARTED, AI_PROVIDER_SELECTED, AI_RESPONSE_RECEIVED, AI_ERROR
- Capability AI_ROUTER

### Flujo
```
User Message → Conversation Engine → AI Router → Provider → Response
```

### Providers Preparados
- Groq (llama-3.3-70b-versatile)
- Gemini (gemini-1.5-flash)
- OpenAI (gpt-4o-mini)
- Local (llama3.2, mistral)

### Características Técnicas
- **Fallback automático**: Si falla el provider preferido, usa fallback
- **Provider availability**: Verifica disponibilidad antes de enviar
- **State tracking**: Historial de uso y errores por provider
- **Placeholder implementations**: Sin llamadas reales a APIs

### Restricciones Cumplidas
- ✅ Sin API keys
- ✅ Sin llamadas reales
- ✅ Sin comandos reales
- ✅ Arquitectura desacoplada

---

## [0.0.6-conversation-engine] - 2024-07-16

### Agregado

**Conversation Engine (STAGE_05):**
- ConversationManager - Coordinator central del sistema de conversación
- ConversationContextManager - Gestor de contexto temporal
- ConversationStateManager - State machine con 6 estados
- ConversationSession - Modelo de sesión de conversación
- Message - Modelo de mensaje con roles (SYSTEM, USER, ASSISTANT)
- IntentProcessor - Interfaz para procesamiento de intenciones
- AIProvider - Interfaz para proveedores de IA
- ResponseGenerator - Interfaz para generación de respuestas
- Conversation Events: CONVERSATION_STARTED, USER_MESSAGE_RECEIVED, PROCESSING_STARTED, ASSISTANT_RESPONSE_READY, CONVERSATION_ENDED
- Capability CONVERSATION_ENGINE
- Integración con RuntimeManager

### Flujo Preparado
```
Wake Word Detected → Conversation Started → Audio Input → Processing → Response
```

### Providers Preparados (Futuros)
- AI: Groq, OpenAI, Gemini, Anthropic, Local
- Intent: Rule-based, AI-powered, Keyword matching

### Características Técnicas
- **Arquitectura desacoplada**: Interfaces para providers
- **State Machine**: Transiciones validadas
- **Context Management**: Máximo 50 mensajes, 32000 caracteres
- **Integración Runtime**: ConversationManager integrado

### Restricciones Cumplidas
- ✅ Sin IA real
- ✅ Sin Groq
- ✅ Sin OpenAI
- ✅ Sin Gemini
- ✅ Sin procesamiento inteligente

---

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
