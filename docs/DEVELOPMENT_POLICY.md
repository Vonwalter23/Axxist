# Axxist Development Policy

## Versión
1.1

## Fecha
2024-07-17

## Autor
OpenHands

---

# AXXIST QUALITY GATE POLICY

## 1. Propósito

Esta política establece los requisitos obligatorios de validación de calidad para todo el código que se integre en la rama principal (`main`) del proyecto Axxist.

**Ningún STAGE podrá considerarse COMPLETADO hasta aprobar exitosamente el Quality Gate automático.**

## 2. Definición

El **Axxist Quality Gate** es un sistema automatizado de validación que se ejecuta en cada push y pull request mediante GitHub Actions. Implementa una arquitectura dual para máxima cobertura de validación.

## 3. Arquitectura Dual Quality Gate

Axxist implementa un sistema de validación de dos niveles:

### NIVEL 1: Build Quality Gate (Obligatorio)

**Trigger**: push, pull_request

**Workflow**: `android-quality-gate.yml`

| Validación | Descripción | Criterio de Éxito |
|------------|-------------|-------------------|
| assembleDebug | Compilación Debug | APK generado correctamente |
| assembleRelease | Compilación Release | APK generado correctamente |
| lintDebug | Análisis estático | Sin errores fatales |
| APK SHA256 | Hash de integridad | Generado automáticamente |
| APK Report | Metadata del APK | HTML con detalles completos |

### NIVEL 2: Runtime Validation (Recomendado para Releases)

**Trigger**: workflow_dispatch

**Workflow**: `android-runtime-validation.yml`

| Validación | Descripción | Criterio de Éxito |
|------------|-------------|-------------------|
| Emulator Boot | Arranque de emulador | sys.boot_completed=1 |
| APK Install | Instalación de APK | Instalación exitosa |
| App Launch | Lanzamiento de app | App iniciada sin errores |
| Runtime Duration | Tiempo de ejecución | 180 segundos |
| Crash Detection | Análisis de logcat | Sin FATAL EXCEPTION ni AndroidRuntime |

## 4. APK Delivery

### 4.1 Artifact Policy

Todo build exitoso genera:

```
Axxist-debug.apk                # APK Debug
Axxist-release.apk              # APK Release
apk-validation-report.html      # Reporte de validación
build-summary.txt               # Resumen de build
lint-report/                     # Reporte de lint
```

### 4.2 APK Metadata

Cada APK incluye:
- **SHA256**: Hash para verificación de integridad
- **Size**: Tamaño del archivo
- **Commit**: SHA del commit de origen
- **Build Date**: Fecha de generación

## 5. Crash Detection

El sistema analiza automáticamente el logcat buscando:
- `FATAL EXCEPTION`
- `AndroidRuntime`
- `Process:`
- `Caused by:`
- `ANR`
- `SIGSEGV`
- `SecurityException`
- `IllegalStateException`

Si se detecta cualquiera de estos errores, el Quality Gate falla.

## 6. Funcionamiento

### 6.1 Pipeline de Ejecución - Build Quality Gate

```
1. Checkout (fetch LFS)
   ↓
2. Setup Java JDK 17
   ↓
3. Cache Gradle
   ↓
4. Build Debug APK
   └── Generate SHA256
   ↓
5. Upload Axxist-debug.apk
   ↓
6. Build Release APK
   └── Generate SHA256
   ↓
7. Upload Axxist-release.apk
   ↓
8. Run Lint
   ↓
9. Generate APK Validation Report
   ↓
10. Final Status
```

### 6.2 Pipeline de Ejecución - Runtime Validation

```
1. Environment Setup
   └── Build or Download APK
   ↓
2. Emulator Setup
   └── Wait for sys.boot_completed=1
   ↓
3. APK Installation
   └── adb install -r
   ↓
4. App Launch
   └── adb shell am start
   ↓
5. Runtime Monitoring
   └── Capture logcat (180s)
   ↓
6. Crash Analysis
   └── Generate crash-analysis.txt
   ↓
7. Generate Runtime Report
   ↓
8. Final Status
```

### 6.3 Triggers

| Workflow | Triggers |
|----------|----------|
| Android Quality Gate | push, pull_request, workflow_dispatch |
| Android Runtime Validation | workflow_dispatch |

## 7. Artifacts Generados

### 7.1 Build Quality Gate Artifacts

| Artifact | Descripción | Retention |
|----------|-------------|-----------|
| Axxist-debug.apk | APK Debug firmable | 30 días |
| Axxist-release.apk | APK Release | 30 días |
| apk-validation-report.html | Reporte HTML de validación | 30 días |
| build-summary.txt | Resumen de build | 30 días |
| lint-report/ | Reporte de lint | 30 días |

### 7.2 Runtime Validation Artifacts

| Artifact | Descripción | Retention |
|----------|-------------|-----------|
| runtime-logcat.txt | Logcat completo | 30 días |
| crash-analysis.txt | Análisis de crashes | 30 días |
| emulator-info.txt | Info del emulador | 30 días |
| runtime-report.html | Reporte HTML runtime | 30 días |

## 8. Reportes HTML

El reporte HTML incluye:

| Campo | Descripción |
|-------|-------------|
| Repository | Nombre del repositorio |
| Commit SHA | Hash del commit |
| Fecha | Fecha de ejecución |
| Branch | Rama evaluada |
| APK Size | Tamaño de los APKs |
| SHA256 Debug | Hash SHA256 del APK Debug |
| SHA256 Release | Hash SHA256 del APK Release |
| Android Lint | Resultados del análisis estático |
| Emulator Boot | Estado del boot del emulador |
| Install Success | Estado de instalación |
| Launch Success | Estado de lanzamiento |
| Runtime Duration | Duración de la validación |
| Crash Detection | Resultado del análisis de crashes |
| Resultado Final | PASS o FAIL |

## 9. Obligatoriedad

### 9.1 Para Completar un Stage

Para que un Stage se considere **COMPLETADO**, deben cumplirse TODAS las condiciones:

1. ✅ Código implementado
2. ✅ Documentación actualizada
3. ✅ Reporte del Stage generado
4. ✅ **Quality Gate aprobado en GitHub Actions**

### 9.2 Para Merge a main

Ningún código puede fusionarse a `main` sin:
- Pull Request creada
- **Android Quality Gate** aprobado
- Revisión aprobada (si está configurado)

## 10. Required Status Check

El workflow está preparado para funcionar como **Required Status Check** en GitHub.

### 10.1 Configuración Manual Requerida

Para activar la protección de rama:

1. Ir a GitHub → Repository → Settings → Branches
2. Crear regla para `main`
3. Activar:
   - ✅ "Require pull request before merging"
   - ✅ "Require status checks to pass before merging"
4. Agregar status check: `Android Quality Gate`

**Nota**: Esta configuración debe realizarse manualmente con permisos de administrador del repositorio.

## 11. Manejo de Limitaciones Técnicas

Si alguna validación no puede ejecutarse por limitaciones del entorno:

| Limitación | Acción |
|------------|--------|
| Hardware insuficiente | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Restricciones del runner | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Problemas del emulador | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Límites de tiempo | `NOT EXECUTED - TECHNICAL LIMITATION` |

**Importante**: No inventar resultados. Documentar la limitación.

## 12. Mejora Continua

Esta política es **permanente** y se revisará en cada Stage mayor para:
- Agregar validaciones adicionales si es necesario
- Optimizar tiempos de ejecución
- Mejorar detección de errores
- Mantener actualizada la documentación

---

# POLÍTICAS GENERALES DE DESARROLLO

## Reglas Arquitectónicas

1. **Arquitectura primero**: Nunca sacrificar arquitectura por velocidad.
2. **Infraestructura antes que funcionalidades**: Placeholders primero.
3. **Módulos desacoplados**: Interfaces, EventBus, Providers.
4. **Interfaces antes que implementaciones**: Contrato primero.
5. **Integraciones al final**: Groq, Spotify, etc. en stages posteriores.
6. **No deuda técnica**: Sin atajos, código limpio.
7. **No secretos en Git**: API keys fuera del repositorio.
8. **Compatibilidad hacia atrás**: Stages aditivos.
9. **Escalabilidad preparada**: Plugin system, multi-plataforma.
10. **Documentación sincronizada**: Código + Docs.

## Estrategia de Desarrollo

> **"Calidad sobre velocidad. Arquitectura sobre features. Un stage a la vez."**

### Reglas del Pipeline

- Nunca avanzar sin validar
- Nunca romper backwards compatibility
- Nunca escribir código sin tests (cuando estén implementados)
- Nunca commit sin actualizar docs
- Nunca deploy sin APK verificada

### Definición de Done

Cada Stage debe cumplir:

1. Código implementado
2. Compilación exitosa (Debug + Release)
3. Quality Gate aprobado
4. Documentación actualizada
5. CHANGELOG actualizado
6. Reporte del Stage generado
7. Commit descriptivo
8. Push realizado

## Control de Versiones

### Formato de Versiones

```
major.minor-stage
```

Ejemplos:
- 0.0.1-foundation
- 0.0.9-action-framework

### Commits

Formato conventional commits:
```
feat(STAGE_XX): descripción
fix(STAGE_XX): descripción
docs: descripción
chore: descripción
```

## Gestión de Ramas

| Rama | Propósito |
|------|-----------|
| main | Producción, código aprobado |
| develop | Integración de features |
| stage-XX-* | Desarrollo de stages específicos |

## Permisos y Credenciales

- API keys en variables de entorno de GitHub
- Secrets en GitHub Secrets
- keystore.properties fuera de Git
- No hardcodear credenciales

---

# HISTORIAL DE CAMBIOS

| Versión | Fecha | Descripción |
|---------|-------|-------------|
| 1.0 | 2024-07-17 | Versión inicial con Quality Gate Policy |

---

**Documento actualizado**: 2024-07-17
**Próxima revisión**: Con cada Stage mayor
