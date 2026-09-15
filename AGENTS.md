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
- **Issues abiertos**: ninguno (verificado contra la API de GitHub)
- **Automation de monitoreo**: `Axxist GitHub Monitor` deshabilitado desde 2026-09-15T18:16:42Z

### Datos verificados

Fuente: API de GitHub, 2026-09-15T18:20Z.

| Campo | Valor |
|-------|-------|
| HEAD de `main` | `a414c65` (2026-08-20, tag `OMAR-2026`) |
| Runtime (React Native) | 0.76.6 / React 18.3.1, `package.json` con `"version": "0.0.1-foundation"` |
| Build Android | Gradle 8.5 (wrapper), Kotlin 2.1.0, AGP vía `android/build.gradle` |
| `minSdk` / `targetSdk` | 29 / 34 |
| Stage actual | STAGE_08 completado; STAGE_09 pendiente |
| Releases publicadas | 4 (`OMAR-2026`, `v0.0.9-action-framework`, `v0.0.9`, `v0.0.2-android-core`) |
| Tags en el repo | 4 (ninguno para `v0.0.3`..`v0.0.8`) |
| Ramas remotas | 12 |
| PRs totales | 9 (7 abiertos: #1, #2 y #4 en `draft`; #6, #7, #8 y #9 activos; #5 cerrado sin merge; #3 mergeado) |
| Issues abiertos | 0 |
| Execuciones del Quality Gate | 9 por `push` a `main` (estables: 6 `success`, 3 `failure` de 2026-07-17). Las de `pull_request` son volátiles mientras haya PRs abiertos (~43 a las 18:20 UTC; ver el aviso de bucle más abajo) |

No hay stages nuevos completados desde STAGE_08. El estado de stages de este archivo
está sincronizado con `docs/PROJECT_STATE.md`.

Existe la especificación `prompts/stages/STAGE_09_ANDROID_ACTIONS.md`, pero **no hay
código de producto de STAGE_09**: ninguna fuente en `src/` o `android/` referencia
`stage_09` ni `android.actions`; las apariciones de "STAGE_09" están en
`docs/PROJECT_STATE.md`, `docs/reports/FASE_00_REPORT.md` y `docs/reports/STAGE_08_REPORT.md`,
que la listan como pendiente. STAGE_09 sigue pendiente de implementación.

## Actividad Reciente del Repositorio

Verificado contra la API de GitHub el 2026-09-15 (18:20 UTC). No hay actividad de producto
reciente: el último commit de `main` es de 2026-08-20. El workflow Android Quality Gate se
dispara con `push` a `main` o `develop` y con `pull_request` hacia `main` (sin filtro de
rutas).

Los `push` sobre `main` son 9, cifra estable: 6 en `success` y 3 en `failure`, las tres
fallidas del 2026-07-17 (`68a8756e`, `4d499b31`, por la creación del workflow, y
`6037c4ed`, por sintaxis YAML), corregidas en la misma jornada; desde `f073ca7` todos los
`push` a `main` están en `success`. Las ejecuciones de `pull_request` (~43 a las 18:20
UTC, y creciendo) cubren ramas `docs/*`, `omar-2026-validacion` y de monitoreo. **El
conteo de `pull_request` no es citable**: cada push a un PR abierto lanza una ejecución
nueva, así que varía en minutos.

> **Bucle de retroalimentación (medido en este run)**: 38 de las ~43 ejecuciones de
> `pull_request` se lanzaron el 2026-09-15 entre las 17:22 y las 18:19, y todas
> corresponden a PRs de monitoreo que solo tocan documentación de estado (`AGENTS.md`, y
> en el caso de #6 también `docs/PROJECT_STATE.md`). El disparador es el workflow
> (`pull_request` sin filtro de rutas), no el automation: cada commit empujado a una de
> esas ramas dispara una ejecución nueva, y el automation commitea a su propia rama en
> cada run. La correlación es directa — la rama de monitoreo acumula 19 ejecuciones para
> 17 commits, aproximadamente una por commit. El reparto por rama es
> `openhands/monitor-activity-2026-09-15` (19),
> `docs/agents-refresh-repo-status` (14), `docs/agents-md-monitor-2026-09-15` (3),
> `docs/agents-md-consolidated-2026-09-15` (1, PR #9) y
> `docs/agents-md-monitor-update` (1, del PR #5 ya cerrado). De ahí nacieron las cinco
> ramas del aviso de duplicación. Mientras los PRs #6 a #9 sigan abiertos, cada push
> relanza el Quality Gate sobre todos ellos; cortar el ciclo requiere mergear uno y cerrar
> los demás, y a futuro conviene filtrar el trigger por rutas
> (`paths-ignore: ['**/*.md']`) o excluir las ramas `openhands/*` y `docs/agents-*`.
> El automation ya está deshabilitado (ver Notas de Mantenimiento), así que el bucle está
> detenido en origen.

Commits en `main` (más recientes primero):

| SHA | Fecha | Autor | Descripción |
|-----|-------|-------|-------------|
| `a414c65` | 2026-08-20 | Vonwalter23 | Corregir referencias de recursos en AGENTS.md (#3) |
| `fcecc5c` | 2026-08-05 | openhands | Agregar PDF de investigación científica y skills |
| `da1ddc0` | 2026-08-05 | Vonwalter23 | Add files via upload |
| `aa29406` | 2026-07-17 | openhands | Android runtime validation y certificación APK |
| `d7ce807` .. `4d499b3` | 2026-07-17 | openhands | Implementación del Quality Gate CI/CD |
| `b9a1edc` .. `0d544c2` | 2026-07-16 | openhands | STAGE_01 a STAGE_08 |

El último commit de `main` sigue siendo `a414c65` (2026-08-20, tag `OMAR-2026`): no hay
commits nuevos en `main` desde esa fecha.

Ramas:

| Rama | Estado |
|------|--------|
| `main` | Rama de integración |
| `docs/colquitt-compact-pdf` | PR #1 abierto (`draft`) |
| `docs/meyer-allen-compact-pdf` | PR #2 abierto (`draft`) |
| `omar-2026-validacion` | PR #4 abierto (`draft`); Quality Gate en verde |
| `docs/resumen-chiavenato` | Sin PR asociado |
| `docs/resumen-commitment-workplace` | Sin PR asociado |
| `docs/agents-refresh-repo-status` | PR #7 abierto (monitoreo duplicado) |
| `docs/agents-md-monitor-update` | PR #5 cerrado sin merge |
| `openhands/monitor-activity-2026-09-15` | PR #6 abierto (monitoreo, este PR) |
| `docs/agents-md-monitor-2026-09-15` | PR #8 abierto (monitoreo duplicado) |
| `docs/agents-md-consolidated-2026-09-15` | PR #9 abierto (consolidación; propone reemplazar a #6, #7 y #8) |
| `docs/agents-consolidation-2026-09-15` | Sin PR asociado |

Los PR #6, #7, #8 y #9 **no son idénticos**: son redacciones distintas del mismo estado,
con diffs que se solapan. #7 y #8 tocan solo `AGENTS.md`; #6 toca `AGENTS.md` y
`docs/PROJECT_STATE.md`; #9 toca solo `AGENTS.md` (74 líneas agregadas, 7 eliminadas).
Mergear más de uno produce conflicto, así que la revisión humana debe elegir uno y cerrar
los demás. Los conteos de líneas cambian con cada push, así que el criterio de elección
debe ser el contenido, no el tamaño del diff.

Pull requests abiertos (requieren revisión humana):

| PR | Título | Contenido |
|----|--------|-----------|
| #1 | Versión compacta del PDF: Jason A. Colquitt | 1 PDF en `docs/material/` (draft) |
| #2 | Versión compacta del PDF: Meyer y Allen | 1 PDF en `docs/material/` (draft) |
| #4 | OMAR 2026: respaldo documental | 23 archivos en `OMAR 2026/` (draft) |
| #6 | Sincronizar AGENTS.md y PROJECT_STATE.md (monitoreo 2026-09-15) | 2 archivos: `AGENTS.md`, `docs/PROJECT_STATE.md` |
| #7 | Refrescar AGENTS.md (monitoreo 2026-09-15) | 1 archivo, solo `AGENTS.md` |
| #8 | Sincronizar AGENTS.md (monitoreo 2026-09-15) | 1 archivo, solo `AGENTS.md` |
| #9 | Consolidar AGENTS.md y detener el bucle del monitoreo | 1 archivo, solo `AGENTS.md`; reemplaza a #6, #7 y #8 |

Todos salvo el presente son **documentación de investigación**, no código de producto.
Ninguno toca `src/` ni `android/`, por lo que el roadmap de stages no cambia. El
**PR #4 es el más grande (23 archivos, ~16.5k líneas)** y trae PDFs binarios; conviene
revisarlo con criterio de peso del repositorio.

> **Aviso de duplicación**: el monitoreo del 2026-09-15 disparó varias ejecuciones
> concurrentes que produjeron los PR #5 (cerrado sin merge), #6, #7, #8 y #9, todos
> actualizando `AGENTS.md`. **El PR #9 se propone explícitamente como reemplazo de #6, #7
> y #8** y documenta la causa raíz del bucle. Conviene mergear uno solo y cerrar el resto
> para evitar conflictos recurrentes. Ninguno tiene required status checks configurados
> en `main`, así que el Quality Gate no sirve para elegirlos: como cada push relanza las
> ejecuciones (`Build Summary`, `APK Validation Report`, `Build Validation`, `Final
> Quality Gate Status`), su estado cambia de un push a otro y varios pueden estar en
> verde a la vez. La decisión debe tomarse por contenido, no por el color del gate.

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

Quedan **STAGE_09 a STAGE_20** pendientes (Android Actions, Contacts & Phone, WhatsApp,
Email, Spotify & Media, Calendar, Memory Engine, Automations, Plugin System, Local AI,
Beta y Commercial Ready). Tabla sincronizada con `docs/PROJECT_STATE.md`.

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

| Tag | Fecha | Assets |
|-----|-------|--------|
| `OMAR-2026` | 2026-08-20 | `OMAR_2026.zip` |
| `v0.0.9` | 2026-07-17 | `Axxist-debug-0.0.9.apk`, `Axxist-release-0.0.9.apk` |
| `v0.0.9-action-framework` | 2026-07-17 | `app-debug.apk`, `app-release.apk` |
| `v0.0.2-android-core` | 2026-07-16 | Sin assets |

`OMAR-2026` es un tag ligero sobre `a414c65`, el mismo commit que `main`: su ZIP es un
respaldo documental, no un artefacto de código. Los tags `v0.0.3` a `v0.0.8` no existen
como releases: los stages STAGE_02 a STAGE_07 quedaron en `docs/PROJECT_STATE.md` y el
CHANGELOG, pero sin release propia en GitHub.

Precisión sobre `OMAR-2026`: la release fue **creada** el 2026-08-20 (mismo timestamp que
el commit `a414c65`) y **publicada** el 2026-09-11, cuando se abrió el PR #4. `main` no
recibió ningún commit en septiembre; el archivo `OMAR_2026.zip` se subió el 2026-09-11
junto con el tag, que apunta a `main` sin aportar código nuevo. Al citar la fecha de esa
release, conviene decir cuál de las dos se usa.

## Documentación Desactualizada (drift conocido)

Detectado durante el monitoreo automático del 2026-09-15. Conviene corregirlo, pero
queda fuera del alcance de este monitoreo:

- `README.md` conserva un roadmap antiguo que marca STAGE_01 en adelante como
  "Pendiente", contradiciendo `docs/PROJECT_STATE.md` (STAGE_08 completado).
- `package.json` declara `"version": "0.0.1-foundation"`, mientras la app Android va
  por `0.0.9-action-framework` (versionCode 9).
- `IMPLEMENTATION_REPORT.md` referencia `pdf_extracted_text.txt` y `pdf_images/`, que
  no existen en el repositorio.
- `architecture/ADR/README.md` sigue siendo un placeholder: no hay ADRs registrados
  pese a que `docs/DECISIONS.md` recoge decisiones aprobadas.
- `knowledge/PROJECT_BOOK.md` dice que el proyecto está "iniciado" y que la Fundación
  está pendiente, cuando STAGE_08 ya cerró.
- `prompts/NEXT_TASK.md` sigue apuntando a "Ejecutar FASE_00 Auditoría Técnica", tarea
  completada hace tiempo.
- `docs/PROJECT_STATE.md` declara una "Rama de desarrollo" `develop` que no existe en
  el repositorio (verificado contra la API: solo existen `main`, las ramas `docs/*`, la
  rama de investigación `omar-2026-validacion` y la rama de monitoreo actual).
- `docs/PROJECT_STATE.md` y `CHANGELOG.md` fechan los stages STAGE_00 a STAGE_08 y el
  Quality Gate en 2024-07-16/17, pero las releases de GitHub para los mismos commits se
  publicaron en 2026-07-16/17. Son un año de diferencia, no una discrepancia de zona
  horaria.
- `docs/PROJECT_STATE.md` lista releases `v0.0.3-runtime` a `v0.0.8-intent-framework` y
  `v0.0.1-foundation` que no existen como tag ni release en GitHub; solo hay 4 tags
  (`OMAR-2026`, `v0.0.9-action-framework`, `v0.0.9`, `v0.0.2-android-core`).
  `docs/PROJECT_STATE.md` **ya reconoce** estas ausencias (sección "Releases", verificada
  contra la API el 2026-09-15): la discrepancia es entre el CHANGELOG y GitHub, no entre
  los dos documentos de estado. Su tabla de versiones sigue listando las entradas
  inexistentes para preservar la numeración de stages, con "Sin tag ni release en GitHub".
- `docs/PROJECT_STATE.md` fecha `OMAR-2026` como 2026-09-11 en su tabla de releases,
  mientras la API de GitHub devuelve 2026-08-20 (misma fecha que `a414c65`). La release
  se creó el 2026-08-20; el 2026-09-11 corresponde al PR #4 (`omar-2026-validacion`).
  Conviene corregir esa fecha para no atribuir a la release un respaldo que aún está en
  revisión.

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
  (`docs/GITHUB_BRANCH_PROTECTION.md`). Este monitoreo **no puede verificarlo**: el token
  del agente recibe `403 Resource not accessible by integration` al consultar
  `/branches/main/protection`, así que la afirmación se sostiene en la documentación del
  repo, no en una lectura directa de la configuración.
- Las ramas `docs/*` no contienen código de producto: son documentación de investigación.
- El automation `Axxist GitHub Monitor` **está deshabilitado** (`enabled: false`,
  `disabled_reason: manual`, 2026-09-15T18:16:42Z). Su trigger era
  `on: [push, pull_request.opened]`: cada run abría un PR nuevo contra `main`, ese PR
  emitía `pull_request.opened` y disparaba el run siguiente. Se verificó contra la API de
  automatizaciones en este run (2026-09-15 18:20 UTC). Para reactivarlo, acotar el trigger
  a `push` con filtro de rama, o excluir las ramas del propio automation con un `filter`
  JMESPath sobre `pull_request.head.ref`.
- Los PRs de monitoreo (#5 cerrado, #6, #7, #8 y #9 abiertos) provienen de ejecuciones
  repetidas del mismo automation; requieren revisión humana para elegir uno y cerrar
  los demás. El PR #9 se propone explícitamente como consolidación y reemplazo de #6-#8.
  Este run de monitoreo **no abrió un PR nuevo**: reutilizó la rama
  `openhands/monitor-activity-2026-09-15` (PR #6) para no generar otra copia.
- No commitear cifras volátiles de PRs o de ejecuciones de CI: el propio push las
  invalida en segundos y genera cadenas de correcciones. Anclar cada cifra a un SHA o
  fecha, o describir el estado cualitativamente. Esa fue la causa del ruido en las ramas
  #6 y #7.
- El Quality Gate sobre un PR de solo documentación no aporta señal de calidad: no compila
  nada distinto según el texto de `AGENTS.md`. Ese es el argumento de fondo para filtrarlo
  por rutas. Útil además porque el gate tarda varios minutos por ejecución.
- Antes de reportar estado de CI, distinguir "ejecuciones por `push` a `main`" de
  "ejecuciones de `pull_request`": mezclarlas produce conteos que cambian a cada minuto
  mientras haya PRs abiertos.
- Los cuatro PRs de monitoreo (#6, #7, #8, #9) son `mergeable` a las 18:20 UTC del
  2026-09-15. No hay required status checks configurados en `main`, así que el color del
  Quality Gate no sirve para elegir entre ellos: cada push relanza las cuatro ejecuciones
  y el estado cambia en minutos. El criterio debe ser el contenido. #9 es el único que
  documenta la causa raíz del bucle y propone cerrar los duplicados.
- Antes de dar por completado un stage, verificar que exista código en `src/` o
  `android/` y un reporte en `docs/reports/`; una especificación en `prompts/stages/`
  no equivale a un stage implementado.

## Recursos Adicionales

- Material de Investigación FUNIBER: `Material_Investigacion_FUNIBER.pdf`
- Material de soporte: `docs/Material de soporte.pdf`
- Reportes por stage: `docs/reports/` (FASE_00 a STAGE_08)
- Estado del proyecto: `docs/PROJECT_STATE.md`
- Decisiones técnicas: `docs/DECISIONS.md`
- Políticas: `docs/DEVELOPMENT_POLICY.md`, `docs/RELEASE_PROCESS.md`,
  `docs/APK_VALIDATION_POLICY.md`, `docs/RELEASE_VALIDATION_POLICY.md`,
  `docs/GITHUB_BRANCH_PROTECTION.md`, `docs/KNOWN_ISSUES.md`
