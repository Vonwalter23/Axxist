# AGENTS.md - Axxist

## Visión del Proyecto

Axxist es un asistente inteligente para Android con capacidades de voz e IA, desarrollado con React Native + Kotlin. El proyecto busca crear una plataforma de asistencia inteligente híbrida que combine IA local con proveedores externos como Groq.

## Estado Actual

- **Versión**: 0.0.9-action-framework (versionCode 9)
- **Stage Actual**: STAGE_08 Action Framework completado
- **Próximo Stage**: STAGE_09 Android Actions
- **Último commit en `main`**: `a414c65` (2026-08-20) — corrección de referencias en AGENTS.md

> Este archivo es el punto de entrada para agentes. El detalle completo del estado
> vive en `docs/PROJECT_STATE.md`; si ambos difieren, prevalece PROJECT_STATE.md.

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

Restan STAGE_09 a STAGE_20 (todos ⏳ pendientes). No existe ningún stage
nuevo completado desde STAGE_08.

## Estado del Repositorio (monitoreo)

Última revisión: 2026-09-15.

**Ramas remotas**: `main` es la única rama de producto. Existen ramas de
documentación sin fusionar (`docs/colquitt-compact-pdf`,
`docs/meyer-allen-compact-pdf`, `docs/resumen-chiavenato`,
`docs/resumen-commitment-workplace`, `omar-2026-validacion`). La rama `develop`
mencionada en `docs/PROJECT_STATE.md` **no existe** en el remoto.

**Pull requests abiertos** (todos en borrador, sin fusionar):
- #1 `docs/colquitt-compact-pdf` — PDF compacto de Colquitt
- #2 `docs/meyer-allen-compact-pdf` — PDF compacto de Meyer & Allen
- #4 `omar-2026-validacion` — carpeta `OMAR 2026` (23 archivos, ~16.5k líneas)

Ninguno de estos PRs toca `src/` ni `android/`; son material de investigación ajeno
al roadmap de stages.

**Tags / Releases**: `v0.0.9` y `v0.0.9-action-framework` (2026-07-17) más
`OMAR-2026` (2026-09-11). `OMAR-2026` apunta al mismo commit que `main` y es
material documental, no una release de producto.

**Quality Gate**: `Android Quality Gate` está activo y en verde para el último
commit de `main`. `Android Runtime Validation` sigue siendo manual.

## Inconsistencias Conocidas

Documentadas para que no se interpreten como trabajo pendiente de stages:

- `package.json` declara `"version": "0.0.1-foundation"` mientras la app Android
  usa `0.0.9-action-framework` (versionCode 9).
- `IMPLEMENTATION_REPORT.md` y `knowledge/PROJECT_BOOK.md` están desactualizados:
  el primero referencia `pdf_images/` y `pdf_extracted_text.txt`, que no existen;
  el segundo dice que el proyecto está "iniciado" con la Fundación pendiente.
- `docs/PROJECT_STATE.md` cita una rama `develop` inexistente.
- El rango correcto de reportes es `docs/reports/` (FASE_00 a STAGE_08).

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
axxist/
├── src/
│   ├── App.tsx
│   └── core/
├── android/
│   └── app/src/main/java/com/axxist/app/
├── architecture/
│   └── ADR/
├── docs/
│   └── reports/
├── prompts/
│   ├── master/
│   ├── stages/
│   └── NEXT_TASK.md
├── knowledge/
└── .agents/
    └── skills/
        └── investigacion-cientifica.md
```

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Flujo obligatorio para cualquier cambio: `.github/OPENHANDS.md`
- Prompt maestro (referencia arquitectónica principal): `prompts/master/PROMPT_MASTER_V1.md`
