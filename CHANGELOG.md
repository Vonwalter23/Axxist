# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/).

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
