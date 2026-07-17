# GitHub Branch Protection Configuration

## Propósito

Este documento describe la configuración necesaria para establecer el **Required Status Check** del Quality Gate en la rama `main` del repositorio Axxist.

**Esta configuración debe realizarse manualmente** con permisos de administrador del repositorio.

---

# CONFIGURACIÓN DE BRANCH PROTECTION

## Pasos para Configurar

### 1. Acceder a Configuración del Repositorio

1. Navegar a: `https://github.com/Vonwalter23/Axxist`
2. Hacer clic en **Settings** (Configuración)
3. En el menú lateral, hacer clic en **Branches**

### 2. Crear Regla de Protección

1. En la sección "Branch protection rules", hacer clic en **Add rule**

### 3. Configurar la Regla

#### Información Básica

| Campo | Valor |
|-------|-------|
| **Branch name pattern** | `main` |

#### Configuración de Protección

Activar las siguientes opciones:

| Opción | Estado | Descripción |
|--------|--------|-------------|
| ✅ **Require pull request before merging** | Activar | Obliga a crear PR para fusionar a main |
| ✅ **Require approvals** | Activar (1) | Requiere al menos 1 aprobación |
| ✅ **Dismiss stale approvals** | Activar | Dismiss reviews cuando se hace push |
| ✅ **Require status checks to pass before merging** | Activar | Obliga a pasar checks |
| ✅ **Require branches to be up to date before merging** | Opcional | Puede causar delays |

#### Required Status Checks

En la sección "Required status checks", agregar:

| Status Check | Descripción |
|--------------|-------------|
| `Android Quality Gate` | Workflow de validación automática |

#### Opciones Adicionales (Opcionales)

| Opción | Recomendación |
|--------|---------------|
| ✅ "Include administrators" | Mantener protección para admins |
| ❌ "Allow force pushes" | NO permitir para main |
| ❌ "Allow deletions" | NO permitir eliminar main |

---

# CONFIGURACIÓN DETALLADA

## GitHub Repository Settings

```
Repository: Vonwalter23/Axxist
```

## Branch Protection Rule

```
Pattern: main
```

## Settings Activation

```yaml
Require pull request before merging: true
Require approvals: true
Approval count: 1
Dismiss stale approvals: true
Require status checks to pass: true
Include administrators: true
Allow force pushes: false
Allow deletions: false
```

## Required Status Checks

```yaml
Status checks required:
  - name: "Android Quality Gate"
    type: "GitHub Actions"
```

---

# NOTAS IMPORTANTES

## Permisos Requeridos

Para realizar esta configuración se necesitan permisos de:
- **Repository Administrator** o
- **Owner** de la organización

## Verificación Post-Configuración

Después de configurar:

1. Intentar hacer push directo a `main` → Debe ser rechazado
2. Crear PR → Debe mostrar el status check
3. Fusionar sin pasar el check → Debe ser bloqueado

## Workflow Disponibilidad

El workflow `.github/workflows/android-quality-gate.yml` debe estar:
- En la rama `main`
- Sin errores de sintaxis
- Ejecutándose correctamente

Para verificar:
1. Ir a **Actions** tab
2. Seleccionar "Android Quality Gate"
3. Ejecutar workflow manually si es necesario

---

# SOLUCIÓN DE PROBLEMAS

## El Status Check No Aparece

1. Verificar que el workflow está en `.github/workflows/`
2. Verificar que el nombre del job en el workflow coincide exactamente
3. Hacer un push para activar el workflow
4. Esperar a que se ejecute al menos una vez

## El Workflow Falla

1. Revisar logs en **Actions** tab
2. Verificar configuración de JDK y Android SDK
3. Revisar permissions del workflow

## No Se Puede Configurar

Si no tienes permisos:
1. Solicitar a un administrador del repositorio
2. Proporcionar este documento como referencia
3. Dejar preparado hasta que se configure

---

# ALTERNATIVA: Configuración via GitHub CLI

Si prefieres usar GitHub CLI:

```bash
# Instalar gh si no está instalado
# gh auth login

# Crear protección de rama
gh api repos/:owner/:repo/branches/main/protection \
  --method PUT \
  -f required_status_checks='{"strict":true,"contexts":["Android Quality Gate"]}' \
  -f enforce_admins=true \
  -f required_pull_request_reviews='{"required_approving_review_count":1}' \
  -f restrictions=null
```

---

# CHECKLIST DE CONFIGURACIÓN

- [ ] Acceder a Settings del repositorio
- [ ] Ir a Branch protection rules
- [ ] Crear regla para "main"
- [ ] Activar "Require pull request before merging"
- [ ] Activar "Require status checks to pass"
- [ ] Agregar "Android Quality Gate" como required check
- [ ] Activar "Include administrators"
- [ ] Desactivar "Allow force pushes"
- [ ] Desactivar "Allow deletions"
- [ ] Guardar cambios
- [ ] Verificar configuración

---

# INFORMACIÓN DEL WORKFLOW

## Nombre del Workflow

```
Android Quality Gate
```

## Ubicación

```
.github/workflows/android-quality-gate.yml
```

## Jobs Incluidos

| Job | Descripción |
|-----|-------------|
| build-validation | Compilación Debug, Release, Lint |
| runtime-validation | Emulador, instalación, detección de crashes |
| generate-report | Generación de reportes HTML |
| final-status | Estado final del Quality Gate |

## Duración Estimada

| Fase | Tiempo |
|------|--------|
| Build Validation | 5-10 minutos |
| Runtime Validation | 5-7 minutos |
| Total | 10-17 minutos |

---

# CONFIGURACIÓN ACTUAL (si ya está configurada)

**Última verificación**: 2024-07-17

**Estado**: ⏳ Pendiente de configuración manual

**Responsable**: Administrador del repositorio

---

**Documento creado**: 2024-07-17
**Por**: OpenHands
