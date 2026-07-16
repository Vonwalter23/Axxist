# Axxist

Intelligent Assistant for Android - Plataforma de asistencia inteligente con capacidades de voz y IA.

## 🚀 Estado del Proyecto

**STAGE_00 Foundation** - Completado ✅ (En revisión)

El proyecto se encuentra en fase de fundación, con la estructura básica establecida y lista para comenzar el desarrollo de funcionalidades.

## ⚠️ Estado de Verificación

| Verificación | Estado |
|-------------|--------|
| Sin integración IA local | ✅ Verificado |
| Sin integración Groq | ✅ Verificado |
| Foreground Service preparado | ✅ Verificado |
| Sin permisos en primer inicio | ✅ Verificado |
| Dependencias mínimas | ✅ Verificado |

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
