# STAGE_04_WAKE_WORD_FRAMEWORK

## Proyecto

Axxist

---

## Objetivo

Construir el Framework de Wake Word de Axxist.

El objetivo de este Stage NO es solamente detectar la palabra "Axxist", sino crear una infraestructura desacoplada que permita incorporar distintos motores de detección de palabras de activación sin modificar la arquitectura del proyecto.

Al finalizar este Stage, Axxist deberá ser capaz de detectar la Wake Word elegida y notificar al Runtime para iniciar el flujo conversacional.

No deberá interpretar comandos ni ejecutar acciones.

---

## Dependencias

Requiere:

- STAGE_00
- STAGE_01
- STAGE_02
- STAGE_03

completados.

---

## Lectura obligatoria

Leer antes de comenzar:

- prompts/master/PROMPT_MASTER_V1.md
- reports/STAGE_00_REPORT.md
- reports/STAGE_01_REPORT.md
- reports/STAGE_02_REPORT.md
- reports/STAGE_03_REPORT.md

---

# Objetivos técnicos

Implementar:

- WakeWordManager
- WakeWordEngine
- WakeWordProvider
- WakeWordDetector
- WakeWordSession
- WakeWordConfiguration
- WakeWordDiagnostics

Todos los componentes deberán ser desacoplados y fácilmente reemplazables.

---

# Arquitectura

La arquitectura deberá permitir intercambiar motores de Wake Word sin modificar el resto del sistema.

El Runtime únicamente interactuará con la interfaz `WakeWordProvider`.

---

# Motor inicial

Implementar un proveedor inicial basado en un motor open source adecuado para uso comercial (por ejemplo, OpenWakeWord si cumple los requisitos del proyecto).

El motor utilizado deberá quedar encapsulado.

No depender directamente de él desde otros módulos.

---

# Wake Word

Configurar inicialmente:

"Axxist"

El diseño deberá permitir cambiar la palabra de activación mediante configuración futura.

---

# Flujo

1. Runtime activo.
2. Audio Core disponible.
3. WakeWordEngine recibe audio.
4. Detecta la Wake Word.
5. Publica evento.
6. Finaliza la sesión de escucha.
7. Espera instrucciones del siguiente Stage.

No iniciar todavía reconocimiento de voz.

---

# Estados

Implementar estados mínimos:

IDLE

LISTENING

DETECTED

STOPPED

ERROR

Todos los cambios deberán notificarse mediante EventBus.

---

# EventBus

Agregar:

WAKEWORD_INITIALIZED

WAKEWORD_STARTED

WAKEWORD_STOPPED

WAKEWORD_DETECTED

WAKEWORD_TIMEOUT

WAKEWORD_ERROR

---

# Logger

Registrar:

- Inicio de escucha
- Fin de escucha
- Wake Word detectada
- Falsos positivos
- Errores
- Tiempo medio de detección

---

# Configuración

Crear:

WakeWordConfiguration

Debe permitir configurar:

- palabra
- sensibilidad
- tiempo máximo de escucha
- timeout
- proveedor utilizado

No utilizar valores hardcodeados.

---

# Diagnósticos

Implementar:

WakeWordDiagnostics

Debe informar:

- proveedor activo
- consumo estimado de CPU
- consumo estimado de memoria
- cantidad de activaciones
- falsos positivos
- tiempo medio de respuesta

---

# React Native

Crear un Native Module únicamente para consultar:

- estado del Wake Word
- proveedor activo
- palabra configurada
- estadísticas

No permitir modificar parámetros desde React Native en esta etapa.

---

# Restricciones

NO implementar:

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
- Android Actions
- Automatizaciones

---

# Testing

Crear pruebas unitarias para:

- WakeWordManager
- WakeWordConfiguration
- WakeWordSession
- WakeWordDiagnostics

Crear pruebas de integración para:

- WakeWordEngine
- WakeWordProvider

Validar:

- detección correcta
- manejo de errores
- falsos positivos básicos
- reinicio de sesión

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

reports/STAGE_04_REPORT.md

Debe incluir:

- proveedor implementado
- arquitectura
- estados soportados
- rendimiento observado
- consumo estimado
- riesgos detectados
- recomendaciones para el siguiente Stage

---

# Definición de éxito

El Stage será exitoso únicamente si:

✓ El Runtime inicia correctamente.

✓ El Audio Core entrega audio al Wake Word Engine.

✓ La palabra "Axxist" se detecta correctamente.

✓ Se genera el evento WAKEWORD_DETECTED.

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
2. Arquitectura implementada.
3. Motor Wake Word seleccionado y justificación.
4. Módulos creados.
5. Resultado de pruebas.
6. APK generada.
7. Link de la Release.
8. Riesgos detectados.
9. Recomendaciones para STAGE_05.
