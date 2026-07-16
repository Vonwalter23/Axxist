# STAGE_03_AUDIO_CORE

## Proyecto

Axxist

---

## Objetivo

Construir el núcleo de audio (Audio Core) de Axxist.

Este Stage establecerá toda la infraestructura de captura, procesamiento y administración del audio del dispositivo para soportar futuras funciones como:

- Wake Word
- Voice Activity Detection (VAD)
- Speech-to-Text (STT)
- Cancelación de ruido
- IA Local
- Groq
- Conversaciones continuas

En esta etapa NO se reconocerán palabras ni se ejecutarán acciones.

---

## Dependencias

Requiere:

- STAGE_00 completado
- STAGE_01 completado
- STAGE_02 completado

---

## Lectura obligatoria

Antes de comenzar leer:

- prompts/master/PROMPT_MASTER_V1.md
- reports/STAGE_00_REPORT.md
- reports/STAGE_01_REPORT.md
- reports/STAGE_02_REPORT.md

---

# Objetivos técnicos

Implementar los siguientes módulos:

- AudioManager
- AudioCaptureEngine
- AudioSessionManager
- AudioBuffer
- AudioPipeline
- AudioDeviceMonitor
- AudioFocusManager
- AudioDiagnostics

Todos los módulos deberán ser independientes y desacoplados.

---

# AudioCaptureEngine

Crear un motor capaz de capturar audio del micrófono de forma continua cuando sea solicitado por módulos superiores.

En este Stage no deberá permanecer grabando constantemente.

Debe soportar:

- Inicio
- Pausa
- Reanudación
- Detención

Debe informar su estado mediante EventBus.

---

# AudioSessionManager

Administrar:

- Sesión de audio
- Audio Focus
- Cambios de dispositivo
- Interrupciones del sistema

Debe ser compatible con Android 10+.

---

# AudioBuffer

Crear un buffer circular de audio preparado para futuras etapas.

Características:

- Bajo consumo de memoria
- Baja latencia
- Configuración dinámica del tamaño
- Limpieza automática

No almacenar audio en disco.

---

# AudioPipeline

Crear una canalización de procesamiento preparada para incorporar posteriormente:

- Filtros
- VAD
- Wake Word
- Reducción de ruido
- Compresión
- IA local

En este Stage solo implementar la infraestructura.

---

# AudioDeviceMonitor

Detectar cambios de:

- Micrófono
- Auriculares
- Bluetooth
- Dispositivos USB compatibles

Registrar eventos mediante EventBus.

No modificar comportamiento automáticamente.

---

# AudioFocusManager

Gestionar correctamente el Audio Focus de Android.

Debe respetar otras aplicaciones.

No interferir con llamadas telefónicas ni reproducción multimedia.

---

# AudioDiagnostics

Implementar un sistema de diagnóstico que informe:

- Frecuencia de muestreo
- Latencia estimada
- Dispositivo utilizado
- Estado del Audio Focus
- Estado del micrófono

Sin enviar información fuera del dispositivo.

---

# EventBus

Agregar nuevos eventos:

AUDIO_INITIALIZED

AUDIO_STARTED

AUDIO_PAUSED

AUDIO_RESUMED

AUDIO_STOPPED

AUDIO_ERROR

AUDIO_DEVICE_CHANGED

AUDIO_FOCUS_GAINED

AUDIO_FOCUS_LOST

---

# Logger

Registrar:

- Inicio de captura
- Detención
- Cambios de dispositivo
- Cambios de Audio Focus
- Errores
- Diagnósticos

---

# React Native

Crear Native Modules para consultar:

- Estado del Audio Core
- Dispositivo activo
- Frecuencia utilizada
- Estado del Audio Focus

No permitir todavía controlar el audio desde React Native.

---

# Configuración Android

Agregar únicamente los permisos necesarios para utilizar el micrófono cuando el usuario lo autorice.

No solicitar permisos durante el primer inicio.

Toda solicitud de permisos deberá quedar preparada para futuras etapas.

---

# Restricciones

NO implementar:

- Wake Word
- Speech To Text
- Text To Speech
- IA
- Groq
- Modelos locales
- Intent Engine
- Planner
- Memory
- WhatsApp
- Spotify
- Email
- Accessibility
- Automatizaciones
- Ejecución de comandos

---

# Calidad

El Audio Core deberá:

- Ser modular
- Ser reutilizable
- Minimizar consumo de batería
- Minimizar uso de CPU
- Ser compatible con Android 10+

---

# Testing

Crear pruebas unitarias para:

- AudioBuffer
- AudioSessionManager
- AudioFocusManager
- AudioDiagnostics

Crear pruebas de integración para:

- AudioCaptureEngine
- AudioPipeline

---

# APK

Compilar APK Debug.

Instalable en Samsung Galaxy A21.

---

# Documentación

Actualizar:

- README
- PROJECT_BOOK
- CHANGELOG

---

# Reporte

Crear:

reports/STAGE_03_REPORT.md

Debe incluir:

- Arquitectura del Audio Core
- Módulos implementados
- Flujo del pipeline
- Permisos utilizados
- Pruebas ejecutadas
- Riesgos detectados
- Mejoras sugeridas
- Próximos pasos

---

# Definición de éxito

El Stage será exitoso únicamente si:

✓ La aplicación compila.

✓ Audio Core inicia correctamente.

✓ El Runtime detecta el Audio Core.

✓ Los módulos registran correctamente los eventos.

✓ Los tests pasan.

✓ Se genera APK.

✓ Se actualiza documentación.

✓ Se realiza commit.

✓ Se realiza push.

✓ Se crea Release.

---

# Entrega obligatoria

OpenHands deberá entregar:

1. Resumen ejecutivo.
2. Arquitectura del Audio Core.
3. Módulos creados.
4. Permisos agregados.
5. Resultado de pruebas.
6. APK generada.
7. Link de la Release.
8. Riesgos detectados.
9. Recomendaciones para STAGE_04.
