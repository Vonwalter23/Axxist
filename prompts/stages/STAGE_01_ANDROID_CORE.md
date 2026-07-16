# STAGE_01_ANDROID_CORE

## Objetivo

Construir el núcleo Android ("Android Core") de Axxist.

Este Stage debe crear toda la infraestructura técnica que servirá de base para las siguientes etapas.

No debe implementar funcionalidades del asistente.

---

## Dependencia

Este Stage solo puede ejecutarse si STAGE_00 fue completado exitosamente.

---

## Lectura obligatoria

Antes de comenzar leer:

- prompts/master/PROMPT_MASTER_V1.md
- reports/STAGE_00_REPORT.md

---

## Objetivos técnicos

Crear la infraestructura Android reutilizable.

Crear módulos base.

Preparar la comunicación React Native ↔ Kotlin.

Crear servicios reutilizables.

Preparar Dependency Injection.

Preparar Event Bus.

Preparar Logger.

Preparar Config Manager.

Preparar Permission Manager.

Preparar Capability Manager.

---

## Implementación requerida

Crear los siguientes módulos:

AndroidCore

Logger

EventBus

ConfigManager

PermissionManager

CapabilityManager

NativeBridge

AppLifecycle

BuildConfiguration

No implementar lógica del asistente.

---

## React Native

Crear la comunicación entre:

React Native

↓

Native Modules

↓

Kotlin

Toda futura comunicación deberá utilizar esta infraestructura.

---

## Kotlin

Crear interfaces limpias.

No crear código acoplado.

Cada módulo deberá poder probarse independientemente.

---

## Event Bus

Crear un sistema interno de eventos.

Ejemplos:

APP_STARTED

APP_STOPPED

MODULE_LOADED

MODULE_ERROR

CONFIG_UPDATED

---

## Logger

Implementar un único sistema de logging.

Niveles:

DEBUG

INFO

WARNING

ERROR

Debe permitir desactivarse en Release.

---

## Config Manager

Centralizar toda configuración.

No hardcodear valores.

Preparar soporte para:

Debug

Release

Feature Flags

---

## Permission Manager

Crear únicamente la infraestructura.

NO solicitar permisos todavía.

---

## Capability Manager

Crear la estructura para registrar capacidades futuras.

Ejemplo:

VOICE

PHONE

SPOTIFY

EMAIL

WHATSAPP

GPS

CAMERA

Sin implementación funcional.

---

## Arquitectura

Todo deberá respetar el Prompt Maestro.

No modificar módulos existentes.

---

## Restricciones

NO implementar:

Foreground Service

Wake Word

Speech To Text

Text To Speech

Groq

IA

Accessibility

Notification Listener

WhatsApp

Spotify

Planner

Memory

Android Actions

---

## Testing

Crear pruebas unitarias para:

Logger

EventBus

ConfigManager

---

## APK

Compilar APK Debug.

---

## Documentación

Actualizar:

README

PROJECT_BOOK

CHANGELOG

---

## Reporte

Crear:

reports/STAGE_01_REPORT.md

Debe incluir:

- módulos creados;
- árbol actualizado;
- dependencias;
- cobertura de pruebas;
- mejoras sugeridas;
- próximos pasos.

---

## Definición de éxito

El Stage se considera exitoso únicamente si:

✓ Compila correctamente.

✓ Todos los tests pasan.

✓ Se genera APK.

✓ Se crea Release.

✓ Se actualiza documentación.

✓ Se genera reporte técnico.

✓ Se realiza commit, push y release.

---

## Entrega obligatoria

OpenHands deberá entregar:

1. Resumen ejecutivo.

2. Arquitectura creada.

3. Módulos creados.

4. Archivos modificados.

5. Dependencias instaladas.

6. Resultado de pruebas.

7. APK generada.

8. Link Release.

9. Riesgos detectados.

10. Recomendaciones para STAGE_02.
