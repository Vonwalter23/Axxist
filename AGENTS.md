# AGENTS.md - Axxist

## Documento principal

Antes de modificar cualquier cosa, leer `prompts/master/PROMPT_MASTER_V1.md`.
Las reglas de trabajo y el flujo obligatorio están en `.github/OPENHANDS.md`.

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: 0.0.9-action-framework (`versionCode` 9, definidos en `android/app/build.gradle`)
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions
- **Último commit en `main`**: `a414c65` (2026-08-20) — corrección de referencias en AGENTS.md
- **Última revisión de monitoreo**: 2026-09-15 (segunda pasada del mismo día, ~17:40 UTC)

> Este archivo es el punto de entrada para agentes. El detalle del estado vive en
> `docs/PROJECT_STATE.md`; si ambos difieren, prevalece `PROJECT_STATE.md`.

Existe la especificación `prompts/stages/STAGE_09_ANDROID_ACTIONS.md`, pero **no hay
código de producto de STAGE_09**: ninguna fuente en `src/` o `android/` referencia
`stage_09` ni `android.actions`. STAGE_09 sigue pendiente de implementación.

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

Restan STAGE_09 a STAGE_20 (todos ⏳ pendientes). **No existe ningún stage nuevo
completado desde STAGE_08**, verificado contra la API de GitHub el 2026-09-15.

## Actividad Reciente del Repositorio

Última revisión: 2026-09-15, segunda pasada del mismo día sobre la misma `main`.

El proyecto no registra actividad de producto desde 2026-07-17. Todo lo posterior es
documentación de investigación (PDFs de FUNIBER, validación OMAR 2026), ajena al
roadmap de stages.

La única actividad del 2026-09-15 son ejecuciones del propio monitoreo automático:
ramas `docs/agents-*` y `openhands/monitor-activity-2026-09-15`, sus PRs asociados (#5
a #8) y los workflows disparados por esos pushes. Ninguna toca `src/` ni `android/`.

Commits en `main` (más recientes primero):

| SHA | Fecha | Autor | Descripción |
|-----|-------|-------|-------------|
| `a414c65` | 2026-08-20 | Vonwalter23 | Corregir referencias de recursos en AGENTS.md (#3) |
| `fcecc5c` | 2026-08-05 | openhands | Agregar PDF de investigación científica y skills |
| `da1ddc0` | 2026-08-05 | Vonwalter23 | Add files via upload |
| `aa29406` | 2026-07-17 | openhands | Android runtime validation y certificación APK |
| `d7ce807`..`4d499b3` | 2026-07-17 | openhands | Implementación del Quality Gate CI/CD |
| `b9a1edc`..`0d544c2` | 2026-07-16 | openhands | STAGE_01 a STAGE_08 |

Ramas remotas:

| Rama | Estado |
|------|--------|
| `main` | Única rama de producto; HEAD `a414c65` |
| `omar-2026-validacion` | PR #4 abierto (documental) |
| `docs/colquitt-compact-pdf` | PR #1 abierto (documental) |
| `docs/meyer-allen-compact-pdf` | PR #2 abierto (documental) |
| `docs/resumen-chiavenato` | Sin PR asociado |
| `docs/resumen-commitment-workplace` | Sin PR asociado |
| `docs/agents-refresh-repo-status` | PR #7 abierto (este PR) |
| `docs/agents-md-monitor-2026-09-15` | PR #8 abierto (duplicado de monitoreo) |
| `openhands/monitor-activity-2026-09-15` | PR #6 abierto (duplicado de monitoreo) |
| `docs/agents-md-monitor-update` | PR #5 cerrado sin fusionar (duplicado de monitoreo) |

Todas las ramas de monitoreo parten del mismo `main` (`a414c65`) y tocan únicamente
`AGENTS.md`. La rama `openhands/monitor-activity-2026-09-15` (PR #6) recibió commits
nuevos durante esta misma ventana después de abrir el PR (`beafa96` → `15f0759`), así
que su contenido sigue cambiando mientras las demás ramas permanecen estáticas.

La rama `develop` mencionada en `docs/PROJECT_STATE.md` **no existe** en el remoto.

Pull requests abiertos (ninguno fusionado):

| PR | Título | Contenido | Tamaño | Draft |
|----|--------|-----------|--------|-------|
| #1 | Versión compacta del PDF: Jason A. Colquitt | 1 PDF en `docs/material/` | — | Sí |
| #2 | Versión compacta del PDF: Meyer y Allen | 1 PDF en `docs/material/` | — | Sí |
| #4 | OMAR 2026: respaldo documental | 23 archivos en `OMAR 2026/` | +16479 líneas, PDFs binarios | Sí |
| #6 | Actualizar AGENTS.md con el estado real | Solo `AGENTS.md` | +172 −13 | No |
| #7 | Refrescar AGENTS.md con el estado real | Solo `AGENTS.md` | +174 −14 | No |
| #8 | Sincronizar AGENTS.md (monitoreo 2026-09-15) | Solo `AGENTS.md` | +159 −16 | No |

Los PRs #1, #2 y #4 son documentales y ninguno toca `src/` ni `android/`. El PR #4
conviene revisarlo con criterio de peso del repositorio (~4 MB de PDFs).

Los PRs #6, #7 y #8 son **duplicados**: tres ejecuciones del mismo monitoreo automático
del 2026-09-15 escribieron `AGENTS.md` de forma independiente, y el PR #5 quedó cerrado
sin fusionar por el mismo motivo. Conviene fusionar uno solo y cerrar el resto.

Sobre cuál fusionar: los tres son `mergeable: true` y rebaseables, así que ninguno está
bloqueado. El PR #6 tiene Quality Gate en verde pero quedó en `unstable` (una ejecución
en curso sobre su rama); los PRs #7 y #8 están en `clean` con los cuatro checks en verde
(`Build Summary`, `APK Validation Report`, `Build Validation`, `Final Quality Gate
Status`). El PR #6 es además el único que sigue recibiendo commits, así que su contenido
todavía cambia y el de #7/#8 no. Elegir por contenido, no por color del gate: mientras los
tres sigan abiertos van a quedar en conflicto entre sí, porque todos editan el mismo
archivo desde el mismo `main`.

Issues: la API de GitHub reporta 6 abiertos (#1, #2, #4, #6, #7, #8), pero son los
mismos pull requests — GitHub comparte numeración entre issues y PRs. **No hay issues
reales abiertos.**

Tags / Releases:

| Tag | Commit | Fecha | Assets |
|-----|--------|-------|--------|
| `OMAR-2026` | `a414c65` | 2026-08-20 | `OMAR_2026.zip` (4.2 MB) |
| `v0.0.9` | `aa29406` | 2026-07-17 | `Axxist-debug-0.0.9.apk`, `Axxist-release-0.0.9.apk` (prerelease) |
| `v0.0.9-action-framework` | `fec5aec` | 2026-07-17 | `app-debug.apk`, `app-release.apk` |
| `v0.0.2-android-core` | `0d544c2` | 2026-07-16 | Sin assets |

`OMAR-2026` apunta al mismo commit que `main` y es material documental, no una release
de producto. `v0.0.9-action-framework` marca el primer commit del Quality Gate, no el
commit de STAGE_08 (`b9a1edc`).

**Quality Gate**: `Android Quality Gate` está activo en `push` y `pull_request`, y en
verde en todos los commits de `main`. `Android Runtime Validation` sigue siendo manual
(`workflow_dispatch`).

## Arquitectura de Calidad

Dual Quality Gate Architecture:
- **Nivel 1**: Build Quality Gate (android-quality-gate.yml)
- **Nivel 2**: Runtime Validation (android-runtime-validation.yml)

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
- Java JDK 17 (obligatorio según `docs/DECISIONS.md`)
- Android SDK API 34
- Gradle 8.5 (wrapper en `android/gradle/wrapper/`)
- React Native 0.76.6
- Kotlin 2.1.0
- TypeScript 5.3

## Estructura del Proyecto

```text
axxist/
|-- src/                              # Cliente React Native (TypeScript)
|   |-- App.tsx
|   `-- core/                         # nativebridge, eventbus, types, lifecycle,
|                                     # capability, logger, config, build
|-- android/
|   `-- app/src/main/java/com/axxist/app/
|       |-- core/                     # nativebridge, permission, eventbus, build,
|       |                             # lifecycle, capability, logger, config
|       `-- runtime/                  # manager, audio, wakeword, conversation,
|                                     # ai, intent, action, service, health,
|                                     # receiver, interfaces
|-- architecture/ADR/                 # Decisiones de arquitectura (placeholder)
|-- docs/
|   |-- reports/                      # FASE_00 a STAGE_08
|   `-- PROJECT_STATE.md
|-- prompts/
|   |-- master/PROMPT_MASTER_V1.md
|   |-- stages/                       # STAGE_00 a STAGE_20
|   `-- NEXT_TASK.md
|-- knowledge/PROJECT_BOOK.md
|-- .github/
|   |-- OPENHANDS.md                  # Reglas y flujo obligatorio
|   `-- workflows/                    # Dual Quality Gate
`-- .agents/skills/
    `-- investigacion-cientifica.md
```

No existe directorio `tests/`: el proyecto no tiene suite de tests propia. La validación
recae en el Quality Gate de CI, aunque `package.json` define los scripts `test` y `lint`.

## Convenciones de Trabajo

Decisiones aprobadas en `docs/DECISIONS.md`:

- Los stages aprobados no se modifican; las nuevas funcionalidades van en un stage nuevo.
- Cada stage debe generar: APK, reporte, actualización de CHANGELOG, commits descriptivos,
  push a GitHub y una GitHub Release.
- TypeScript y JDK 17 son obligatorios.
- El AI Router es la única puerta de acceso a cualquier IA.
- Todos los cambios deben documentarse.

## Inconsistencias Conocidas

Documentadas para que no se interpreten como trabajo pendiente de stages:

- `package.json` declara `"version": "0.0.1-foundation"` mientras la app Android usa
  `0.0.9-action-framework` (versionCode 9).
- `README.md` conserva un roadmap antiguo que marca STAGE_01 a STAGE_20 como
  "Pendiente", contradiciendo `docs/PROJECT_STATE.md` (STAGE_08 completado).
- `IMPLEMENTATION_REPORT.md` referencia `pdf_images/` y `pdf_extracted_text.txt`, que no
  existen en el repositorio.
- `knowledge/PROJECT_BOOK.md` dice que el proyecto está "iniciado" con la Fundación
  pendiente.
- `architecture/ADR/README.md` sigue siendo un placeholder: no hay ADRs registrados pese
  a que `docs/DECISIONS.md` recoge decisiones aprobadas.
- `docs/PROJECT_STATE.md` cita una rama `develop` inexistente.
- El rango correcto de reportes es `docs/reports/` (FASE_00 a STAGE_08).

## Notas de Mantenimiento

- `docs/PROJECT_STATE.md` es la fuente de verdad del stage actual; verificar ahí antes de
  asumir un avance de stage.
- El Required Status Check de branch protection sigue pendiente de configuración manual
  (`docs/GITHUB_BRANCH_PROTECTION.md`).
- Las ramas `docs/*` y `omar-2026-validacion` no contienen código de producto: son
  documentación de investigación. `docs/resumen-chiavenato` y
  `docs/resumen-commitment-workplace` llevan 2 commits cada una por delante de `main` y no
  tienen PR asociado; quedaron huérfanas.
- Antes de abrir un PR que toque `AGENTS.md`, revisar si ya existe otro abierto del mismo
  monitoreo: el 2026-09-15 se generaron cuatro PRs idénticos (#5 a #8) y quedaron en
  conflicto entre sí.

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Flujo obligatorio para cualquier cambio: `.github/OPENHANDS.md`
- Prompt maestro (referencia arquitectónica principal): `prompts/master/PROMPT_MASTER_V1.md`
- Políticas: `docs/DEVELOPMENT_POLICY.md`, `docs/RELEASE_PROCESS.md`,
  `docs/APK_VALIDATION_POLICY.md`, `docs/RELEASE_VALIDATION_POLICY.md`,
  `docs/GITHUB_BRANCH_PROTECTION.md`, `docs/KNOWN_ISSUES.md`