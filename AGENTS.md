# AGENTS.md - Axxist

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: 0.0.9-action-framework
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions (⏳ Pendiente - no iniciado)
- **Última Actividad**: 2026-09-11
- **Quality Gate**: ✅ Operativo, últimos runs en verde

> No hay nuevos stages completados. `docs/PROJECT_STATE.md` sigue en STAGE_08.
> La actividad posterior corresponde a documentación de investigación y PRs abiertos
> (ver "Actividad Reciente"), no a avance de stages de producto.

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

## Actividad Reciente

### Commits en `main`

| Fecha | Commit | Autor | Descripción |
|-------|--------|-------|-------------|
| 2026-08-20 | a414c65 | Vonwalter23 | Corregir referencias de recursos en AGENTS.md (#3) |
| 2026-08-05 | fcecc5c | openhands | Agregar PDF de investigación científica y habilidades OpenHands |
| 2026-08-05 | da1ddc0 | Vonwalter23 | Add files via upload (docs/Material de soporte.pdf) |
| 2026-07-17 | aa29406 | openhands | Add android runtime validation and apk certification |

El último commit de producto es el Quality Gate (2026-07-17). Lo posterior es
documentación de investigación (PDFs de FUNIBER), no avance de stages.

### Releases

| Tag | Fecha | Notas |
|-----|-------|-------|
| OMAR-2026 | 2026-09-11 | Respaldo documental validación escala Meyer & Allen |
| v0.0.9 / v0.0.9-action-framework | 2026-07-17 | STAGE_08 Action Framework |
| v0.0.2-android-core | 2026-07-16 | STAGE_01 Android Core |

### Pull Requests abiertos

| PR | Título | Rama | Estado |
|----|--------|------|--------|
| #4 | OMAR 2026: respaldo documental (Meyer & Allen; Omar) | omar-2026-validacion | Draft, 23 archivos |
| #2 | Versión compacta del PDF: El aporte de Meyer y Allen | docs/meyer-allen-compact-pdf | Draft |
| #1 | Versión compacta del PDF: El aporte de Jason A. Colquitt | docs/colquitt-compact-pdf | Draft |

Ninguno es de producto: los tres son documentación de investigación, están en draft
y no fueron fusionados.

### Ramas sin PR asociado

- `docs/resumen-chiavenato` (2026-08-19) — resumen Chiavenato (2009)
- `docs/resumen-commitment-workplace` (2026-08-19) — resumen Meyer & Allen (1997)

### Issues

Sin issues abiertos.

---

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
- Java JDK 17
- Android SDK API 34
- React Native 0.76.6
- Kotlin 2.1.0

## Estructura del Proyecto

```
Axxist/
├── src/
│   ├── App.tsx
│   └── core/                 # logger, eventbus, nativebridge, capability,
│                             # config, lifecycle, build, types
├── android/
│   └── app/src/main/java/com/axxist/app/
│       └── runtime/          # service, manager, state, conversation,
│                             # intent, action, audio, wakeword, ai
├── .github/workflows/        # android-quality-gate.yml,
│                             # android-runtime-validation.yml
├── docs/
│   ├── reports/              # FASE_00 a STAGE_08
│   ├── PROJECT_STATE.md
│   ├── DECISIONS.md
│   └── (PDFs de investigación)
├── prompts/
├── knowledge/
└── .agents/
    └── skills/
        └── investigacion-cientifica.md
```

## Convenciones de Trabajo

Decisiones aprobadas en `docs/DECISIONS.md`:

- Los stages aprobados no se modifican; las nuevas funcionalidades van en un stage nuevo.
- Cada stage debe generar: APK, reporte, actualización de CHANGELOG, commits descriptivos,
  push a GitHub y una GitHub Release.
- TypeScript y JDK 17 son obligatorios.
- El AI Router es la única puerta de acceso a cualquier IA.

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
