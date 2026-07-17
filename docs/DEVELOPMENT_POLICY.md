# Axxist Development Policy

## Versión
1.0

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

El **Axxist Quality Gate** es un sistema automatizado de validación que se ejecuta en cada push y pull request mediante GitHub Actions.

## 3. Validaciones Obligatorias

### 3.1 Build Validation

| Validación | Descripción | Criterio de Éxito |
|------------|-------------|-------------------|
| assembleDebug | Compilación Debug | APK generado correctamente |
| assembleRelease | Compilación Release | APK generado correctamente |
| lintDebug | Análisis estático | Sin errores fatales |

### 3.2 Runtime Validation

| Validación | Descripción | Criterio de Éxito |
|------------|-------------|-------------------|
| Emulator Boot | Arranque de emulador | sys.boot_completed=1 |
| APK Install | Instalación de APK | Instalación exitosa |
| App Launch | Lanzamiento de app | App iniciada sin errores |
| Runtime Duration | Tiempo de ejecución | Mínimo 30 segundos |
| Crash Detection | Análisis de logcat | Sin FATAL EXCEPTION ni AndroidRuntime |

### 3.3 Crash Detection

El sistema analiza automáticamente el logcat buscando:
- `FATAL EXCEPTION`
- `AndroidRuntime`
- `Process:`
- `Caused by:`

Si se detecta cualquiera de estos errores, el Quality Gate falla.

## 4. Funcionamiento

### 4.1 Pipeline de Ejecución

```
1. Checkout
   ↓
2. Configuración del entorno (JDK 17, Android SDK, Build Tools)
   ↓
3. Cache (Gradle, Android dependencies)
   ↓
4. Build Validation
   ├── assembleDebug
   ├── assembleRelease
   └── lintDebug
   ↓
5. Runtime Validation
   ├── Crear emulador (Android 14, API 34)
   ├── Esperar boot completo
   ├── Instalar app-debug.apk
   ├── Lanzar con adb shell monkey
   ├── Capturar logcat (180 segundos)
   └── Analizar crashes
   ↓
6. Generar Reportes
   ├── report.html
   ├── build-summary.txt
   └── Publicar artifacts
```

### 4.2 Triggers

El Quality Gate se ejecuta automáticamente en:
- Push a `main` o `develop`
- Pull Request a `main`
- Ejecución manual (`workflow_dispatch`)

## 5. Artifacts Generados

### 5.1 Obligatorios

| Artifact | Descripción | Retention |
|----------|-------------|-----------|
| app-debug.apk | APK Debug | 14 días |
| app-release.apk | APK Release | 14 días |
| logcat.txt | Logcat completo | 14 días |
| report.html | Reporte HTML | 30 días |
| build-summary.txt | Resumen de build | 30 días |
| lint-report/ | Reporte de lint | 14 días |

### 5.2 Opcionales

| Artifact | Descripción |
|----------|-------------|
| emulator-video.mp4 | Video del emulador |

## 6. Reporte HTML

El reporte HTML incluye:

| Campo | Descripción |
|-------|-------------|
| Repository | Nombre del repositorio |
| Commit SHA | Hash del commit |
| Fecha | Fecha de ejecución |
| Branch | Rama evaluada |
| Stage | Stage actual del proyecto |
| Build Debug | Estado de compilación Debug |
| Build Release | Estado de compilación Release |
| APK Size | Tamaño de los APKs |
| SHA256 Debug | Hash SHA256 del APK Debug |
| SHA256 Release | Hash SHA256 del APK Release |
| Android Lint | Resultados del análisis estático |
| Emulator Boot | Estado del boot del emulador |
| Install Success | Estado de instalación |
| Launch Success | Estado de lanzamiento |
| Runtime Duration | Duración de la validación |
| Crash Detection | Resultado del análisis de crashes |
| Warnings | Advertencias encontradas |
| Resultado Final | PASS o FAIL |

## 7. Obligatoriedad

### 7.1 Para Completar un Stage

Para que un Stage se considere **COMPLETADO**, deben cumplirse TODAS las condiciones:

1. ✅ Código implementado
2. ✅ Documentación actualizada
3. ✅ Reporte del Stage generado
4. ✅ **Quality Gate aprobado en GitHub Actions**

### 7.2 Para Merge a main

Ningún código puede fusionarse a `main` sin:
- Pull Request creada
- **Android Quality Gate** aprobado
- Revisión aprobada (si está configurado)

## 8. Required Status Check

El workflow está preparado para funcionar como **Required Status Check** en GitHub.

### 8.1 Configuración Manual Requerida

Para activar la protección de rama:

1. Ir a GitHub → Repository → Settings → Branches
2. Crear regla para `main`
3. Activar:
   - ✅ "Require pull request before merging"
   - ✅ "Require status checks to pass before merging"
4. Agregar status check: `Android Quality Gate`

**Nota**: Esta configuración debe realizarse manualmente con permisos de administrador del repositorio.

## 9. Manejo de Limitaciones Técnicas

Si alguna validación no puede ejecutarse por limitaciones del entorno:

| Limitación | Acción |
|------------|--------|
| Hardware insuficiente | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Restricciones del runner | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Problemas del emulador | `NOT EXECUTED - TECHNICAL LIMITATION` |
| Límites de tiempo | `NOT EXECUTED - TECHNICAL LIMITATION` |

**Importante**: No inventar resultados. Documentar la limitación.

## 10. Mejora Continua

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
