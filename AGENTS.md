# AGENTS.md - Axxist

## Documento principal

Antes de realizar cualquier modificación, leer `prompts/master/PROMPT_MASTER_V1.md`.
Las reglas de trabajo y el flujo obligatorio están en `.github/OPENHANDS.md`.

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: 0.0.9-action-framework
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions
- **Última actividad verificada**: 2026-09-15 (monitoreo automático)

No hay stages nuevos completados desde STAGE_08. El estado de stages de este archivo
está sincronizado con `docs/PROJECT_STATE.md`.

## Actividad Reciente del Repositorio

Verificado contra la API de GitHub el 2026-09-15. Todos los commits de `main` tienen
el **Android Quality Gate en verde**.

Commits en `main` (más recientes primero):

| SHA | Fecha | Autor | Descripción |
|-----|-------|-------|-------------|
| `a414c65` | 2026-08-20 | Vonwalter23 | Corregir referencias de recursos en AGENTS.md (#3) |
| `fcecc5c` | 2026-08-05 | openhands | Agregar PDF de investigación científica y skills |
| `da1ddc0` | 2026-08-05 | Vonwalter23 | Add files via upload |
| `aa29406` | 2026-07-17 | openhands | Android runtime validation y certificación APK |
| `d7ce807` .. `4d499b3` | 2026-07-17 | openhands | Implementación del Quality Gate CI/CD |
| `b9a1edc` .. `0d544c2` | 2026-07-16 | openhands | STAGE_01 a STAGE_08 |

Ramas:

| Rama | Estado |
|------|--------|
| `main` | Rama de integración |
| `docs/colquitt-compact-pdf` | PR #1 abierto |
| `docs/meyer-allen-compact-pdf` | PR #2 abierto |
| `omar-2026-validacion` | PR #4 abierto |
| `docs/resumen-chiavenato` | Sin PR asociado |
| `docs/resumen-commitment-workplace` | Sin PR asociado |

Pull requests abiertos (requieren revisión humana):

| PR | Título | Contenido |
|----|--------|-----------|
| #1 | Versión compacta del PDF: Jason A. Colquitt | 1 PDF en `docs/material/` |
| #2 | Versión compacta del PDF: Meyer y Allen | 1 PDF en `docs/material/` |
| #4 | OMAR 2026: respaldo documental | 23 archivos en `OMAR 2026/` |

Estos PRs son **documentación de investigación**, no código de producto. No aportan
avance de stages. El **PR #4 es grande (23 archivos, ~16.5k líneas)** y trae PDFs
binarios; conviene revisarlo con criterio de peso del repositorio.

> Ninguno de estos PRs toca `src/` ni `android/`, por lo que el roadmap de stages
> no cambia.

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
- Java JDK 17 (`docs/DECISIONS.md` fija JDK 17 como obligatorio)
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
|       `-- runtime/                  # runtime, audio, wakeword, conversation,
|                                     # ai, intent, action, service, health,
|                                     # receiver, interfaces
|-- architecture/ADR/                 # Decisiones de arquitectura
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

## Releases

| Tag | Assets |
|-----|--------|
| `OMAR-2026` | `OMAR_2026.zip` |
| `v0.0.9` | `Axxist-debug-0.0.9.apk`, `Axxist-release-0.0.9.apk` |
| `v0.0.9-action-framework` | `app-debug.apk`, `app-release.apk` |
| `v0.0.2-android-core` | Sin assets |

## Documentación Desactualizada (drift conocido)

Detectado durante el monitoreo automático del 2026-09-15. Conviene corregirlo, pero
queda fuera del alcance de este monitoreo:

- `README.md` conserva un roadmap antiguo que marca STAGE_01 en adelante como
  "Pendiente", contradiciendo `docs/PROJECT_STATE.md` (STAGE_08 completado).
- `package.json` declara `"version": "0.0.1-foundation"`, mientras el proyecto va por
  `0.0.9-action-framework`.
- `IMPLEMENTATION_REPORT.md` referencia `pdf_extracted_text.txt` y `pdf_images/`, que
  no existen en el repositorio.
- `architecture/ADR/README.md` sigue siendo un placeholder: no hay ADRs registrados
  pese a que `docs/DECISIONS.md` recoge decisiones aprobadas.

## Convenciones de Trabajo

Decisiones aprobadas en `docs/DECISIONS.md`:

- Los stages aprobados no se modifican; las nuevas funcionalidades van en un stage nuevo.
- Cada stage debe generar: APK, reporte, actualización de CHANGELOG, commits descriptivos,
  push a GitHub y una GitHub Release.
- TypeScript y JDK 17 son obligatorios.
- El AI Router es la única puerta de acceso a cualquier IA.
- Todos los cambios deben documentarse.

## Notas de Mantenimiento

- `docs/PROJECT_STATE.md` es la fuente de verdad del stage actual; verificar ahí antes
  de asumir un avance de stage.
- El Required Status Check de branch protection sigue pendiente de configuración manual
  (`docs/GITHUB_BRANCH_PROTECTION.md`).
- Las ramas `docs/*` no contienen código de producto: son documentación de investigación.

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Políticas: `docs/DEVELOPMENT_POLICY.md`, `docs/RELEASE_PROCESS.md`,
  `docs/APK_VALIDATION_POLICY.md`, `docs/RELEASE_VALIDATION_POLICY.md`,
  `docs/GITHUB_BRANCH_PROTECTION.md`, `docs/KNOWN_ISSUES.md`
