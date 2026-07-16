# STAGE_05_CONVERSATION_ENGINE

## Proyecto

Axxist

---

## Objetivo

Construir el Conversation Engine de Axxist.

Este módulo administrará todo el ciclo conversacional desde que se detecta la Wake Word hasta que el sistema entregue el audio capturado al motor de reconocimiento de voz.

En esta etapa NO se realizará reconocimiento del contenido hablado.

Su responsabilidad será determinar cuándo comienza y cuándo termina una interacción de voz.

---

## Dependencias

Requiere:

- STAGE_00 Foundation
- STAGE_01 Android Core
- STAGE_02 Runtime
- STAGE_03 Audio Core
- STAGE_04 Wake Word Framework

---

## Lectura obligatoria

Antes de comenzar:

- PROMPT_MASTER_V1.md
- Todos los REPORTS anteriores

---

# Objetivos técnicos

Implementar:

- ConversationManager
- ConversationSession
- VoiceActivityDetector (VAD)
- EndOfSpeechDetector
- SilenceDetector
- AudioSegmentManager
- ConversationStateMachine
- ConversationTimeoutManager
- ConversationDiagnostics

Todos los módulos deberán ser desacoplados y reutilizables.

---

# Flujo conversacional

1. Runtime activo.
2. Wake Word detectada.
3. Se crea una nueva ConversationSession.
4. Se activa el VAD.
5. Se detecta el inicio del habla.
6. Se almacena el audio en memoria mediante AudioBuffer.
7. Se detecta el final del habla.
8. Se cierra la sesión.
9. El audio queda listo para el siguiente Stage (Speech-to-Text).

No enviar audio a ninguna IA todavía.

---

# Estados de la conversación

Implementar la siguiente máquina de estados:

IDLE

WAKEWORD_DETECTED

WAITING_FOR_SPEECH

LISTENING

PROCESSING_AUDIO

COMPLETED

TIMEOUT

CANCELLED

ERROR

Todos los cambios deberán publicarse mediante EventBus.

---

# Voice Activity Detection (VAD)

Implementar un VAD modular.

El motor deberá ser intercambiable.

Debe permitir incorporar en el futuro:

- Silero VAD
- WebRTC VAD
- Modelo propio
- Otros motores

No depender de un proveedor específico.

---

# End Of Speech Detector

Implementar un detector de final de habla.

Debe identificar:

- pausa natural
- silencio prolongado
- cancelación
- tiempo máximo de conversación

Todos los parámetros deberán ser configurables.

---

# Silence Detector

Detectar:

- ausencia de voz
- ruido ambiente
- micrófono desconectado

Registrar eventos y métricas.

---

# AudioSegmentManager

Dividir el audio capturado en segmentos lógicos.

Preparar la estructura para futuras funciones:

- streaming STT
- conversaciones largas
- memoria contextual

En esta etapa no almacenar audio en disco.

---

# Conversation Timeout

Implementar temporizadores configurables para:

- tiempo máximo esperando que el usuario hable
- duración máxima de una conversación
- silencio máximo permitido

---

# EventBus

Agregar nuevos eventos:

CONVERSATION_CREATED

CONVERSATION_STARTED

VOICE_DETECTED

VOICE_ENDED

SILENCE_DETECTED

CONVERSATION_COMPLETED

CONVERSATION_TIMEOUT

CONVERSATION_CANCELLED

CONVERSATION_ERROR

---

# Logger

Registrar:

- inicio de conversación
- duración
- tiempo de respuesta
- duración del audio
- silencios
- errores

---

# React Native

Exponer únicamente:

- estado de la conversación
- duración
- estadísticas
- último estado

No permitir iniciar conversaciones manualmente.

---

# Restricciones

NO implementar:

- Speech-to-Text
- Text-to-Speech
- Groq
- IA Local
- Intent Engine
- Planner
- Memory
- Ejecución de comandos
- Automatizaciones

---

# Calidad

Optimizar para:

- bajo consumo de batería
- baja latencia
- mínimo uso de CPU
- sesiones concurrentes seguras
- estabilidad en Android 10+

---

# Testing

Crear pruebas para:

- ConversationStateMachine
- VoiceActivityDetector
- EndOfSpeechDetector
- SilenceDetector
- TimeoutManager

Realizar pruebas de integración con:

- Audio Core
- Wake Word Framework
- Runtime

---

# APK

Generar APK Debug instalable en Samsung Galaxy A21.

---

# Documentación

Actualizar:

- README
- PROJECT_BOOK
- CHANGELOG

---

# Reporte

Crear:

reports/STAGE_05_REPORT.md

Debe incluir:

- arquitectura conversacional
- máquina de estados
- flujo de audio
- métricas
- riesgos
- mejoras
- recomendaciones para STAGE_06

---

# Definición de éxito

El Stage será exitoso únicamente si:

✓ La Wake Word activa correctamente una sesión.

✓ El sistema detecta inicio y fin de voz.

✓ El audio queda preparado para STT.

✓ No existen bloqueos del Runtime.

✓ Todos los tests pasan.

✓ Se genera APK.

✓ Se actualiza documentación.

✓ Se realiza commit.

✓ Se realiza push.

✓ Se crea Release.

---

# Entrega obligatoria

1. Resumen ejecutivo.
2. Arquitectura del Conversation Engine.
3. Módulos implementados.
4. Flujo completo de estados.
5. Resultado de pruebas.
6. APK generada.
7. Link de la Release.
8. Riesgos detectados.
9. Recomendaciones para STAGE_06.
