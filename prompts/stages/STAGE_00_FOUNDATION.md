# STAGE_00_FOUNDATION

## Proyecto

Axxist

---

## Objetivo

Construir la fundación técnica del proyecto Axxist respetando completamente la arquitectura definida en:

prompts/master/PROMPT_MASTER_V1.md

Este Stage NO desarrolla funcionalidades del asistente.

Su única misión es dejar un proyecto Android profesional, compilable, documentado y listo para comenzar el desarrollo por etapas.

---

## Instrucción Obligatoria

Antes de escribir una sola línea de código deberás leer completamente:

prompts/master/PROMPT_MASTER_V1.md

Luego deberás entregar un análisis indicando que comprendiste la arquitectura, las reglas del proyecto y el plan de implementación.

No comenzar el desarrollo hasta finalizar dicho análisis.

---

# Stack obligatorio

- React Native (última versión estable compatible)
- TypeScript
- Kotlin
- Android SDK
- AndroidX
- JDK 17
- Gradle 8+
- GitHub Actions

---

# Arquitectura

Respetar estrictamente la arquitectura definida en el Prompt Maestro.

No modificarla.

No simplificarla.

No reinterpretarla.

Si detectas una mejora deberás documentarla pero NO implementarla sin autorización.

---

# Implementación requerida

Crear el proyecto Android.

Configurar React Native.

Configurar Kotlin.

Configurar TypeScript.

Configurar Gradle.

Configurar Android.

Configurar compilación Debug.

Crear estructura inicial de carpetas respetando la arquitectura.

Crear configuración base para futuras etapas.

No implementar funcionalidades del asistente.

---

# Configuración Android

Nombre de la aplicación:

Axxist

Idioma inicial:

Español

Min SDK:

Elegir el más adecuado y justificarlo.

Target SDK:

Última versión estable compatible.

Package Name sugerido:

com.axxist.app

---

# Calidad

Configurar:

- ESLint
- Prettier
- EditorConfig
- ktlint

Si propones herramientas adicionales deberás justificarlo.

---

# GitHub

Configurar:

- README.md
- CHANGELOG.md
- .gitignore
- LICENSE (MIT)

Preparar GitHub Actions para:

- instalar dependencias
- compilar
- ejecutar tests
- generar APK Debug

---

# APK

Generar:

app-debug.apk

---

# Release

Crear Release:

v0.0.1-foundation

Adjuntar:

app-debug.apk

---

# Documentación

Actualizar automáticamente:

README

PROJECT_BOOK

CHANGELOG

---

# Reporte

Crear:

reports/STAGE_00_REPORT.md

Debe incluir:

- Resumen del trabajo realizado.
- Arquitectura creada.
- Árbol del proyecto.
- Dependencias utilizadas.
- Justificación técnica de cada dependencia.
- Riesgos detectados.
- Mejoras sugeridas.
- Próximos pasos.

---

# Restricciones

NO implementar:

- Wake Word
- IA
- Groq
- Modelos locales
- Voice Engine
- Intent Engine
- Planner
- Memory
- Accessibility
- Foreground Service
- WhatsApp
- Spotify
- Email
- Automatizaciones
- Acciones Android

Este Stage es únicamente la fundación del proyecto.

---

# Definición de éxito

El Stage será considerado exitoso únicamente si:

✓ El proyecto compila sin errores.

✓ Se genera una APK Debug.

✓ La APK puede instalarse en el Samsung Galaxy A21.

✓ GitHub Actions ejecuta correctamente la compilación.

✓ Toda la documentación fue actualizada.

✓ Se creó el reporte técnico.

✓ Se realizaron commit, push y release.

---

# Entrega obligatoria

Al finalizar deberás entregar:

1. Resumen ejecutivo.

2. Archivos creados.

3. Archivos modificados.

4. Dependencias instaladas.

5. Justificación técnica.

6. Resultado de compilación.

7. Resultado de pruebas.

8. Ubicación de la APK.

9. Link de la Release.

10. Recomendaciones para STAGE_01.

No finalizar el Stage sin entregar esta información.
