# Axxist

Intelligent Assistant for Android - Plataforma de asistencia inteligente con capacidades de voz y IA.

## 🚀 Estado del Proyecto

**STAGE_08 Action Framework** - Completado ✅

El proyecto cuenta con infraestructura completa de frameworks para Voice AI Assistant.

## 🛡️ Axxist Quality Gate

Este proyecto implementa un **Dual Quality Gate Architecture** para validación completa.

### Arquitectura de Validación

```
┌─────────────────────────────────────────────────────────────┐
│              AXXIST DUAL QUALITY GATE                       │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  ┌─────────────────────────────────────────────────────┐  │
│  │  NIVEL 1: BUILD QUALITY GATE                        │  │
│  │  android-quality-gate.yml (Auto)                     │  │
│  │  ─────────────────────────────────────────────────   │  │
│  │  ✓ assembleDebug                                     │  │
│  │  ✓ assembleRelease                                   │  │
│  │  ✓ lintDebug                                         │  │
│  │  ✓ APK Generation (SHA256)                          │  │
│  │  ✓ APK Validation Report                             │  │
│  │  Trigger: push, pull_request                         │  │
│  └─────────────────────────────────────────────────────┘  │
│                          │                                 │
│                          ▼                                 │
│  ┌─────────────────────────────────────────────────────┐  │
│  │  NIVEL 2: RUNTIME VALIDATION                         │  │
│  │  android-runtime-validation.yml (On-Demand)          │  │
│  │  ─────────────────────────────────────────────────   │  │
│  │  ✓ Emulator Boot (API 34)                           │  │
│  │  ✓ APK Installation                                  │  │
│  │  ✓ Application Launch                               │  │
│  │  ✓ Runtime Monitoring (180s)                         │  │
│  │  ✓ Crash Detection                                   │  │
│  │  Trigger: workflow_dispatch                          │  │
│  └─────────────────────────────────────────────────────┘  │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Workflows Disponibles

| Workflow | Trigger | Propósito |
|----------|---------|-----------|
| `Android Quality Gate` | push, pull_request | Validación automática de builds |
| `Android Runtime Validation` | workflow_dispatch | Validación runtime bajo demanda |

### Artifacts Generados

**Build Quality Gate:**
- 📦 `Axxist-debug.apk` - APK Debug (29.4 MB)
- 📦 `Axxist-release.apk` - APK Release (14.3 MB)
- 📄 `apk-validation-report.html` - Reporte de validación
- 📝 `build-summary.txt` - Resumen de build
- 📊 `lint-report/` - Reporte de lint

**Runtime Validation (On-Demand):**
- 📄 `runtime-logcat.txt` - Logcat runtime
- 📄 `crash-analysis.txt` - Análisis de crashes
- 📄 `emulator-info.txt` - Info del emulador
- 📊 `runtime-report.html` - Reporte runtime

### APK Metadata

Cada APK incluye:
- **SHA256**: Hash para verificación de integridad
- **Size**: Tamaño del archivo
- **Commit**: SHA del commit de origen
- **Date**: Fecha de generación

### Required Status Check

El **Build Quality Gate** está preparado como **Required Status Check**.

> ⚠️ **Nota**: La configuración de Branch Protection debe realizarse manualmente. Ver [docs/GITHUB_BRANCH_PROTECTION.md](docs/GITHUB_BRANCH_PROTECTION.md)

### Documentación de Calidad

- [docs/DEVELOPMENT_POLICY.md](docs/DEVELOPMENT_POLICY.md) - Políticas de desarrollo
- [docs/GITHUB_BRANCH_PROTECTION.md](docs/GITHUB_BRANCH_PROTECTION.md) - Configuración de protección
- [docs/APK_VALIDATION_POLICY.md](docs/APK_VALIDATION_POLICY.md) - Política de validación APK
- [docs/RELEASE_VALIDATION_POLICY.md](docs/RELEASE_VALIDATION_POLICY.md) - Política de validación de releases

## ⚠️ Estado de Verificación

| Verificación | Estado |
|-------------|--------|
| Sin integración IA local | ✅ Verificado |
| Sin integración Groq | ✅ Verificado |
| Foreground Service preparado | ✅ Verificado |
| Sin permisos en primer inicio | ✅ Verificado |
| Dependencias mínimas | ✅ Verificado |
| Quality Gate implementado | ✅ Verificado | |

## 📋 Requisitos

- **Node.js**: >=18.x
- **Java JDK**: 17 (obligatorio)
- **Android SDK**: API 34 (Android 14)
- **React Native**: 0.76.6
- **Kotlin**: 2.1.0

## 🛠️ Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/Vonwalter23/Axxist.git
cd Axxist
```

### 2. Instalar dependencias

```bash
npm install
```

### 3. Configurar Android

Asegúrate de tener configurado el Android SDK:

```bash
export ANDROID_HOME=~/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
```

### 4. Compilar APK

```bash
npm run build:android
```

El APK estará en `android/app/build/outputs/apk/debug/app-debug.apk`

## 📦 Scripts Disponibles

| Script | Descripción |
|--------|-------------|
| `npm start` | Inicia Metro bundler |
| `npm run android` | Ejecuta en Android |
| `npm run build:android` | Compila APK debug |
| `npm run lint` | Ejecuta ESLint |
| `npm run format` | Formatea código |

## 📁 Estructura del Proyecto

```
axxist/
├── src/
│   └── App.tsx           # Componente principal (básico)
├── android/              # Proyecto Android nativo
│   └── app/
│       └── src/main/java/com/axxist/app/
│           ├── MainActivity.kt
│           └── MainApplication.kt
├── docs/                 # Documentación
│   ├── reports/
│   │   └── FASE_00_REPORT.md
│   └── PROJECT_STATE.md
├── prompts/              # Prompts de desarrollo
└── knowledge/            # Conocimiento del proyecto
```

## 🎯 Roadmap de Stages

| Stage | Nombre | Estado |
|-------|--------|--------|
| FASE_00 | Auditoría Técnica | ✅ Completada |
| STAGE_00 | Foundation | ✅ Completada (En revisión) |
| STAGE_01 | Android Core | ⏳ Pendiente |
| STAGE_02 | Runtime | ⏳ Pendiente |
| STAGE_03 | Audio Core | ⏳ Pendiente |
| STAGE_04 | Wake Word | ⏳ Pendiente |
| STAGE_05 | Conversation Engine | ⏳ Pendiente |
| STAGE_06 | AI Router | ⏳ Pendiente |
| STAGE_07-20 | Funcionalidades | ⏳ Pendiente |

## 🔧 APK Debug

**Ubicación:** `android/app/build/outputs/apk/debug/app-debug.apk`

**Hash SHA256:** `bc0de051e4103351abef35b2ffc16ac486070e73397337aeb84951ec87273ed1`

## 📄 Licencia

MIT License - Ver archivo LICENSE para más detalles.

## 👤 Autor

**Vonwalter23**

- GitHub: [@Vonwalter23](https://github.com/Vonwalter23)
