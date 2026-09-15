# AGENTS.md - Axxist

## Documento principal

Antes de realizar cualquier modificación, leer `prompts/master/PROMPT_MASTER_V1.md`.
Las reglas de trabajo y el flujo obligatorio están en `.github/OPENHANDS.md`.

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: 0.0.9-action-framework (versionCode 9, en `android/app/build.gradle`)
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions
- **Último commit en `main`**: `a414c65` (2026-08-20) — solo `AGENTS.md`, no es avance de stage
- **Última revisión de monitoreo**: 2026-09-15 (verificado contra la API de GitHub)

No hay stages nuevos completados desde STAGE_08. Restan STAGE_09 a STAGE_20, todos pendientes.
`docs/PROJECT_STATE.md` es la fuente de verdad del stage: si este archivo y PROJECT_STATE.md
difieren, prevalece PROJECT_STATE.md.

Como el último commit de producto es `aa29406` (2026-07-17), nada de lo publicado después en
`main` es avance de roadmap: es documentación de investigación y de monitoreo.

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

## Actividad Reciente del Repositorio

Revisión del monitoreo: 2026-09-15, con datos verificados contra la API de GitHub.
No hay actividad de producto desde 2026-07-17; todo lo posterior es documentación de
investigación (PDFs de FUNIBER, validación OMAR 2026), ajena al roadmap de stages.

### Commits en `main`

| SHA | Fecha | Autor | Descripción |
|-----|-------|-------|-------------|
| `a414c65` | 2026-08-20 | Vonwalter23 | Corregir referencias de recursos en AGENTS.md (#3) — solo `AGENTS.md` |
| `fcecc5c` | 2026-08-05 | openhands | Agregar PDF de investigación científica y skills |
| `da1ddc0` | 2026-08-05 | Vonwalter23 | Add files via upload |
| `aa29406` | 2026-07-17 | openhands | Android runtime validation y certificación APK |
| `d7ce807` .. `4d499b3` | 2026-07-17 | openhands | Implementación del Quality Gate CI/CD |
| `b9a1edc` .. `0d544c2` | 2026-07-16 | openhands | STAGE_01 a STAGE_08 |

El último commit de producto es `aa29406` (Quality Gate). `a414c65` no toca `src/` ni
`android/`: solo corrige referencias de recursos en este archivo.

### Pull requests

| PR | Título | Rama | Estado |
|----|--------|------|--------|
| #8 | docs(agents): sincronizar AGENTS.md con el estado del repositorio | `docs/agents-md-monitor-2026-09-15` | Abierto (este PR) |
| #7 | docs: refrescar AGENTS.md con el estado real del repositorio | `docs/agents-refresh-repo-status` | Abierto |
| #6 | docs: actualizar AGENTS.md con el estado real del repositorio | `openhands/monitor-activity-2026-09-15` | Abierto |
| #5 | docs(agents): actualizar AGENTS.md con estado y actividad reciente | `docs/agents-md-monitor-update` | Cerrado sin fusionar el 2026-09-15 |
| #4 | OMAR 2026: respaldo documental de validación | `omar-2026-validacion` | Abierto, draft (23 archivos) |
| #3 | Corregir referencias de recursos en AGENTS.md | `docs/fix-agents-md-resources` | Fusionado el 2026-08-20 como `a414c65` |
| #2 | Versión compacta del PDF: Meyer y Allen | `docs/meyer-allen-compact-pdf` | Abierto, draft |
| #1 | Versión compacta del PDF: Jason A. Colquitt | `docs/colquitt-compact-pdf` | Abierto, draft |

Los PRs #1, #2 y #4 son documentación de investigación (PDFs y texto de respaldo), no
código de producto: ninguno toca `src/` ni `android/`, por lo que el roadmap de stages no
cambia. Los PRs #6, #7 y #8 son propuestas de actualización de este mismo archivo, las tres
abiertas y con contenido equivalente; #5 fue cerrado sin fusionar. Conviene fusionar una
sola y cerrar las otras dos.

### Ramas remotas

`main` es la única rama de producto (`a414c65`). Existen ramas de documentación sin fusionar:
`docs/agents-md-monitor-2026-09-15`, `docs/agents-md-monitor-update`,
`docs/agents-refresh-repo-status`, `openhands/monitor-activity-2026-09-15`,
`docs/colquitt-compact-pdf`, `docs/meyer-allen-compact-pdf`, `docs/resumen-chiavenato`,
`docs/resumen-commitment-workplace` y `omar-2026-validacion`.
Las ramas `docs/resumen-chiavenato` y `docs/resumen-commitment-workplace` no tienen PR asociado.

> La rama `develop` mencionada en `docs/PROJECT_STATE.md` **no existe** en el remoto.

### Releases

| Tag | Commit | Fecha | Assets |
|-----|--------|-------|--------|
| `OMAR-2026` | `a414c65` | 2026-08-20 | `OMAR_2026.zip` (4.2 MB, material documental) |
| `v0.0.9` | `aa29406` | 2026-07-17 | `Axxist-debug-0.0.9.apk`, `Axxist-release-0.0.9.apk` (prerelease) |
| `v0.0.9-action-framework` | `fec5aec` | 2026-07-17 | `app-debug.apk`, `app-release.apk` |
| `v0.0.2-android-core` | `0d544c2` | 2026-07-16 | Sin assets |

`OMAR-2026` apunta al mismo commit que `main` y es material documental, no una release de
producto. `v0.0.9-action-framework` marca el primer commit del Quality Gate, no el commit de
STAGE_08 (`b9a1edc`).

### Issues

Sin issues abiertos. Los seis elementos de `open_issues_count` son pull requests, contados
por GitHub junto con los issues.

### Quality Gate

`Android Quality Gate` corre en `push` y `pull_request`, y está en verde en todos los commits
de `main`, incluido `a414c65`. `Android Runtime Validation` sigue siendo manual
(`workflow_dispatch`). El Required Status Check de branch protection sigue pendiente de
configuración manual (`docs/GITHUB_BRANCH_PROTECTION.md`).

## Arquitectura de Calidad

Dual Quality Gate Architecture:
- **Nivel 1**: Build Quality Gate (`android-quality-gate.yml`) — trigger: push, pull_request
- **Nivel 2**: Runtime Validation (`android-runtime-validation.yml`) — trigger: workflow_dispatch

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
- Gradle 8.5 (wrapper en `android/gradle/wrapper/gradle-wrapper.properties`)
- React Native 0.76.6
- Kotlin 2.1.0 (en `android/build.gradle`)
- TypeScript 5.3 (`package.json`)

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
|       `-- runtime/                  # action, ai, audio, conversation, health,
|                                     # intent, interfaces, manager, receiver,
|                                     # service, state, wakeword
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

Flujo obligatorio (`.github/OPENHANDS.md`): leer el Prompt Maestro, analizar la tarea,
proponer plan, implementar, ejecutar pruebas, generar informe, commit y push.

Un stage solo se considera completado si existe código en `src/` o `android/`, un reporte en
`docs/reports/`, una entrada en `CHANGELOG.md` y una release. La especificación en
`prompts/stages/` (21 archivos, STAGE_00 a STAGE_20) es el plan, no evidencia de avance.

## Inconsistencias Conocidas

Detectadas durante el monitoreo automático y confirmadas contra el árbol real. Conviene
corregirlas, pero quedan fuera del alcance del monitoreo:

- `README.md` mantiene un roadmap antiguo que marca STAGE_01 a STAGE_07-20 como "Pendiente"
  aunque su encabezado ya declara STAGE_08 completado, contradiciendo `docs/PROJECT_STATE.md`.
- `package.json` declara `"version": "0.0.1-foundation"`, mientras la app Android usa
  `0.0.9-action-framework` (versionCode 9).
- `IMPLEMENTATION_REPORT.md` referencia `pdf_extracted_text.txt` y `pdf_images/`, que no
  existen en el repositorio.
- `knowledge/PROJECT_BOOK.md` dice que el proyecto está "iniciado" con la Fundación pendiente.
- `docs/PROJECT_STATE.md` cita una rama `develop` inexistente y ubica la validación del
  Quality Gate en 2024 mientras el proyecto es de 2026.
- `architecture/ADR/README.md` sigue siendo un placeholder: no hay ADRs registrados pese a
  que `docs/DECISIONS.md` recoge decisiones aprobadas.
- Faltan reportes de stages que figuran como completados: no existe
  `docs/reports/STAGE_00_REPORT.md` ni `docs/reports/STAGE_01.5_REPORT.md`. Los reportes
  presentes van de `FASE_00` a `STAGE_08`, con esos dos huecos.
- `prompts/NEXT_TASK.md` sigue pidiendo ejecutar FASE_00, ya completada.
- `CHANGELOG.md` usa fechas 2024-07-16/17 para stages que el resto de la documentación fecha
  en 2026-07-16/17.
- El monitoreo automático de AGENTS.md genera varias ramas y PRs casi idénticos el mismo día
  (#6, #7 y #8 el 2026-09-15). Conviene fusionar uno solo y cerrar el resto para no acumular
  ruido de revisión.

## Notas de Mantenimiento

- `docs/PROJECT_STATE.md` es la fuente de verdad del stage actual; verificar ahí antes de
  asumir un avance de stage.
- Las ramas `docs/*` no contienen código de producto: son documentación de investigación.
- El rango de reportes por stage es `docs/reports/` (FASE_00 a STAGE_08, sin STAGE_00 ni
  STAGE_01.5).
- Antes de abrir un nuevo PR de AGENTS.md, revisar si ya hay uno abierto de una ejecución
  previa del mismo monitoreo; el 2026-09-15 se abrieron tres a la vez.

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Políticas: `docs/DEVELOPMENT_POLICY.md`, `docs/RELEASE_PROCESS.md`,
  `docs/APK_VALIDATION_POLICY.md`, `docs/RELEASE_VALIDATION_POLICY.md`,
  `docs/GITHUB_BRANCH_PROTECTION.md`, `docs/KNOWN_ISSUES.md`
- Flujo obligatorio para cualquier cambio: `.github/OPENHANDS.md`
- Prompt maestro: `prompts/master/PROMPT_MASTER_V1.md`