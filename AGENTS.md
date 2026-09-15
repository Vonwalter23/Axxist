# AGENTS.md - Axxist

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: `0.0.9-action-framework` (`versionCode` 9 en `android/app/build.gradle`)
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions (pendiente)
- **Último commit de `main`**: `a414c65` (2026-08-20) — solo toca `AGENTS.md`
- **Última verificación de este archivo**: 2026-09-15

> STAGE_09 no tiene implementación. Existe la especificación `prompts/stages/STAGE_09_ANDROID_ACTIONS.md`, pero ni `src/` ni `android/app/src/main/java/` referencian `stage_09` ni `android.actions`.

## Módulos Implementados

| Stage | Nombre | Estado |
|-------|--------|--------|
| FASE_00 | Auditoría Técnica | ✅ |
| STAGE_00 | Foundation | ✅ |
| STAGE_01 | Android Core | ✅ |
| STAGE_01.5 | Production Foundation | ✅ |
| STAGE_02 | Runtime | ✅ |
| STAGE_03 | Audio Core | ✅ |
| STAGE_04 | Wake Word | ✅ |
| STAGE_05 | Conversation Engine | ✅ |
| STAGE_06 | AI Router | ✅ |
| STAGE_07 | Intent Engine | ✅ |
| STAGE_08 | Action Framework | ✅ |
| STAGE_09 | Android Actions | ⏳ Pendiente |

Detalle por módulo: `docs/PROJECT_STATE.md`. Reportes: `docs/reports/` (FASE_00 a STAGE_08).

## Arquitectura de Calidad

Dual Quality Gate Architecture:
- **Nivel 1**: Build Quality Gate (`.github/workflows/android-quality-gate.yml`) — trigger `push` a `main`/`develop` y `pull_request` a `main`
- **Nivel 2**: Runtime Validation (`.github/workflows/android-runtime-validation.yml`) — trigger `workflow_dispatch`

El **Required Status Check** de branch protection sigue pendiente de configuración manual (ver `docs/GITHUB_BRANCH_PROTECTION.md`); sin él, el resultado del gate no bloquea un merge.

### Bucle de retroalimentación del automation de monitoreo

> **⚠️ Leer antes de crear o modificar automatizaciones sobre este repositorio.**

El automation de GitHub configurado para este repo dispara con `on: [push, pull_request.opened]`. El flujo que produce el bucle es:

1. Un evento abre el run del automation.
2. El run abre un PR nuevo contra `main`.
3. Ese PR emite `pull_request.opened`.
4. El evento dispara el run siguiente → volver al paso 1.

El 2026-09-15 esto generó decenas de ejecuciones del Quality Gate en menos de una hora (contra 9 de `push` en toda la historia del repo), los PRs duplicados #5, #6, #7 y #8, y múltiples runs del automation en estado `FAILED` por timeout. El bucle además se autoalimenta con ruido: cada run documenta en `AGENTS.md` el conteo de PRs y de ejecuciones de CI, cifras que su propio push invalida en segundos.

**Recomendaciones** (la automatización fue deshabilitada manualmente el 2026-09-15):

- Acotar el trigger a `push` con filtro de rama, o a `pull_request.opened` con filtro que excluya ramas del propio automation (`filter` JMESPath sobre `pull_request.head.ref`).
- No commitear cifras volátiles (conteos de PRs, ramas o ejecuciones de CI) en `AGENTS.md`; describir el estado cualitativamente o anclar cada cifra a un SHA/fecha.
- Antes de mergear un PR generado por el automation, verificar que no exista otro equivalente abierto.

## Skills Disponibles

### Investigación Científica
- **Archivo**: `.agents/skills/investigacion-cientifica.md`
- **Descripción**: Guía sobre el proceso de investigación científica basada en FUNIBER
- **Uso**: Orientación metodológica para proyectos de investigación, tesis, tesinas

### GitHub Integration
- Disponible a través de la skill `github` del sistema
- Workflows de CI/CD configurados en `.github/workflows/`

## Comandos de Desarrollo

```bash
# Instalar dependencias
npm install

# Iniciar Metro bundler
npm start

# Ejecutar en Android
npm run android

# Compilar APK debug
npm run build:android

# Ejecutar lint
npm run lint

# Formatear código
npm run format
```

## Requisitos

- Node.js >=18.x
- Java JDK 17
- Android SDK API 34
- React Native 0.76.6
- Kotlin 2.1.0

## Estructura del Proyecto

```
axxist/
├── src/
│   ├── App.tsx
│   └── core/            # logger, eventbus, config, capability,
│                        # nativebridge, lifecycle, build, types
├── android/             # rootProject.name = 'Axxist'
│   └── app/src/main/java/com/axxist/app/
│       ├── core/        # espejo Kotlin de src/core + permission
│       └── runtime/     # manager, service, state, health, receiver,
│                        # audio, wakeword, conversation, ai, intent, action
├── docs/
│   ├── reports/         # FASE_00 a STAGE_08
│   └── *.md             # PROJECT_STATE, DECISIONS, politicas
├── prompts/
│   ├── stages/          # especificaciones STAGE_00 a STAGE_20
│   └── master/
├── knowledge/           # PROJECT_BOOK.md
└── .agents/
    └── skills/
        └── investigacion-cientifica.md
```

## Convenciones de Trabajo

De `docs/DECISIONS.md` (políticas que rigen cada Stage):

- Desarrollo incremental por Stages; los Stages aprobados no se modifican, las funcionalidades nuevas entran como Stages nuevos.
- Cada Stage genera APK, reporte, actualización del CHANGELOG, commits descriptivos, push a GitHub y GitHub Release.
- TypeScript obligatorio; JDK 17 obligatorio; Android 10+ (minSdk 29).
- El AI Router es la única puerta de acceso a cualquier IA.
- Todos los cambios deben documentarse.

## Inconsistencias conocidas

Verificado contra el árbol real y la API de GitHub el 2026-09-15:

- `docs/PROJECT_STATE.md` declara una rama `develop` que **no existe** en el repositorio.
- `package.json` reporta `"version": "0.0.1-foundation"`, desalineado con `versionCode` 9.
- El roadmap de `README.md` y el de `PROJECT_STATE.md` no siempre coinciden con lo verificado.
- `architecture/ADR/` no contiene ADRs.
- No existe directorio `tests/`.
- Los tags `v0.0.3` a `v0.0.8` no existen aunque figuren en el CHANGELOG y en `PROJECT_STATE.md`.
- Ramas `docs/*` sin PR asociado: `docs/resumen-chiavenato`, `docs/resumen-commitment-workplace`.
- Los "issues" que reporta la API son en realidad pull requests: GitHub comparte numeración. No hay issues reales abiertos.

## Notas de Mantenimiento

- `docs/PROJECT_STATE.md` es la fuente de verdad del stage actual.
- Antes de afirmar que un stage está completo, verificar código en `src/` o `android/`, no solo la existencia del prompt del stage.
- No mezclar material de investigación (PDFs FUNIBER, PRs #1, #2 y #4) con trabajo de roadmap.

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Issues conocidos: `docs/KNOWN_ISSUES.md`
- Políticas: `docs/DEVELOPMENT_POLICY.md`, `docs/RELEASE_PROCESS.md`, `docs/RELEASE_VALIDATION_POLICY.md`, `docs/APK_VALIDATION_POLICY.md`, `docs/GITHUB_BRANCH_PROTECTION.md`
