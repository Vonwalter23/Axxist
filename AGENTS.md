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
- **Última revisión de monitoreo**: 2026-09-15 (cuarta pasada del mismo día, ~17:58 UTC)

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

Última revisión: 2026-09-15, cuarta pasada del mismo día sobre la misma `main`.

El proyecto no registra actividad de producto desde 2026-07-17. Todo lo posterior es
documentación de investigación (PDFs de FUNIBER, validación OMAR 2026), ajena al
roadmap de stages.

La única actividad del 2026-09-15 son ejecuciones del propio monitoreo automático:
ramas `docs/agents-*` y `openhands/monitor-activity-2026-09-15`, sus PRs asociados (#5
a #8) y los workflows disparados por esos pushes. Ninguna toca `src/` ni `android/`.

**Causa raíz del bucle**: la automatización `Axxist GitHub Monitor` se dispara con
`trigger.on = ["push", "pull_request.opened"]` y **sin filtro**. Al escribir `AGENTS.md`
en su propia rama, su push vuelve a dispararla; cada pasada añade un commit y un push, lo
que la re-dispara indefinidamente. Eso explica los cuatro PRs (#5 a #8) y la rama
`openhands/monitor-activity-2026-09-15`, que acumula 13 commits y recibió otro push
durante esta cuarta pasada. Mientras el trigger siga sin filtro, el bucle continúa.

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
`AGENTS.md`. Los conteos de commits sobre `main` cambian en cada pasada porque el bucle
las sigue alimentando, así que no se fijan aquí como dato: al cierre de esta cuarta pasada
iban aproximadamente 13 en `openhands/monitor-activity-2026-09-15` (#6), 10 en
`docs/agents-refresh-repo-status` (#7, la rama de este PR), 3 en
`docs/agents-md-monitor-2026-09-15` (#8) y 1 en `docs/agents-md-monitor-update` (#5,
cerrado). #6 y #7 siguen escribiéndose; el contenido de #8 y #5 está fijo.

La rama `develop` mencionada en `docs/PROJECT_STATE.md` **no existe** en el remoto.

Pull requests abiertos (ninguno fusionado):

| PR | Título | Contenido | Tamaño | Draft |
|----|--------|-----------|--------|-------|
| #1 | Versión compacta del PDF: Jason A. Colquitt | 1 PDF en `docs/material/` | — | Sí |
| #2 | Versión compacta del PDF: Meyer y Allen | 1 PDF en `docs/material/` | — | Sí |
| #4 | OMAR 2026: respaldo documental | 23 archivos en `OMAR 2026/` | +16479 líneas, PDFs binarios | Sí |
| #6 | Actualizar AGENTS.md con el estado real | Solo `AGENTS.md` | +205 −13 | No |
| #7 | Refrescar AGENTS.md con el estado real | Solo `AGENTS.md` | ≈ +230 −16 (esta pasada) | No |
| #8 | Sincronizar AGENTS.md (monitoreo 2026-09-15) | Solo `AGENTS.md` | +184 −17 | No |

Los PRs #1, #2 y #4 son documentales y ninguno toca `src/` ni `android/`. El PR #4
conviene revisarlo con criterio de peso del repositorio (~4 MB de PDFs).

Los PRs #6, #7 y #8 son **duplicados**: tres ejecuciones del mismo monitoreo automático
del 2026-09-15 escribieron `AGENTS.md` de forma independiente, y el PR #5 quedó cerrado
sin fusionar por el mismo motivo. Conviene fusionar uno solo y cerrar el resto.

Sobre cuál fusionar: **el color del Quality Gate no sirve como criterio**: el workflow
se relanza en cada push, así que cualquier snapshot de más de un minuto está vencido. En
esta cuarta pasada #7 y #8 estaban en `clean` y #6 en `unstable` (tenía un run en curso,
sha `702afc7`). Los tres son `mergeable: true` y rebaseables, así que ninguno está
bloqueado. #6 es el único que sigue recibiendo commits y por eso es también el más
cambiante. Elegir por contenido y cobertura de la verificación, no por el badge; y
mientras los tres sigan abiertos van a quedar en conflicto entre sí, porque todos editan
el mismo archivo desde el mismo `main`.

Las tres ramas de monitoreo parten de `main` en `a414c65` y no se han rebasado entre sí,
así que sus diffs se solapan en las mismas secciones de `AGENTS.md`: fusionar dos produce
conflicto. Ninguna rama de monitoreo está *behind* `main` (`behind_by: 0`).

Nota de alcance: `docs/resumen-chiavenato` y `docs/resumen-commitment-workplace` no están
"sin PR asociado" por olvido — están **divergidas** de `main` (`ahead 2 / behind 1`), no
solo adelantadas, así que un merge directo tampoco sería limpio.

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

**Quality Gate**: `Android Quality Gate` está activo en `push` y `pull_request`. En `main`
acumula 9 ejecuciones, 6 en verde y 3 en rojo — las tres fallas son del 2026-07-17, cuando
se estaba implementando el propio workflow (commits `68a8756`, `4d499b3` —que aún corrían
como `.github/workflows/android-quality-gate.yml` sin nombre de workflow— y `6037c4e`).
Todos los commits posteriores (`f073ca7` en adelante, hasta `a414c65` del 2026-08-20) están
en verde. `Android Runtime Validation` sigue siendo manual (`workflow_dispatch`).

El repositorio acumula ~39 ejecuciones del workflow, casi todas del 2026-09-15 y casi todas
disparadas por los pushes del propio bucle de monitoreo (una por cada commit de cada rama
`docs/agents-*` y `openhands/monitor-activity-2026-09-15`). Es costo de CI generado por la
automatización, no por trabajo de producto.

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
  `docs/resumen-commitment-workplace` llevan 2 commits por delante de `main` y 1 por detrás
  (divergidas, no solo adelantadas); no tienen PR asociado y quedaron huérfanas.
- Antes de abrir un PR que toque `AGENTS.md`, revisar si ya existe otro abierto del mismo
  monitoreo: el 2026-09-15 se generaron cuatro PRs idénticos (#5 a #8) y quedaron en
  conflicto entre sí. La causa no es el azar sino el trigger sin filtro (ver "Actividad
  Reciente"); conviene arreglarlo antes de seguir fusionando salidas del bucle.
- La automatización `Axxist GitHub Monitor` dispara en `push` sin filtro, por lo que se
  re-dispara con sus propios commits: cada pasada que escribe `AGENTS.md` genera un push,
  que genera la pasada siguiente. Recomendación: filtrar el trigger a `ref == 'refs/heads/main'`
  (o excluir las ramas `docs/agents-*` y `openhands/monitor-activity-*`) para que el
  monitoreo deje de auto-alimentarse.
- Al resumir CI en este archivo, no fijar el color del Quality Gate como un hecho: se
  relanza en cada push y un snapshot puede quedar vencido en minutos. Vale documentar el
  historial de `main` (estable) o la cobertura del workflow, no el último badge observado
  en una rama en movimiento.

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