# STAGE_02_RUNTIME

## Proyecto

Axxist

---

## Objetivo

Construir el Runtime permanente de Axxist.

Este Runtime será responsable de mantener vivo al asistente durante todo el ciclo de vida del dispositivo y de gestionar el inicio, la detención y el estado de todos los módulos futuros.

Todavía NO deberá escuchar la voz del usuario ni ejecutar acciones.

---

## Dependencias

Requiere:

- STAGE_00 completado.
- STAGE_01 completado.

---

## Lectura obligatoria

Antes de comenzar leer:

- prompts/master/PROMPT_MASTER_V1.md
- reports/STAGE_00_REPORT.md
- reports/STAGE_01_REPORT.md

---

# Objetivos técnicos

Implementar:

- RuntimeManager
- AssistantService (Foreground Service)
- ServiceController
- BootReceiver
- RuntimeStateManager
- NotificationManager del servicio
- HealthMonitor
- Watchdog interno

Todos estos módulos deberán quedar desacoplados.

---

# Runtime

El Runtime será el punto central del asistente.

Estados mínimos:

STOPPED

STARTING

RUNNING

PAUSED

STOPPING

ERROR

Cada cambio de estado deberá notificarse mediante el EventBus.

---

# BootReceiver

Implementar un BroadcastReceiver que detecte el arranque del dispositivo.

No iniciar automáticamente el servicio sin respetar las restricciones de Android.

El comportamiento deberá ser compatible con Android 10+.

---

# Foreground Service

Crear AssistantService.

Responsabilidades:

- mantener vivo el Runtime;
- administrar el ciclo de vida;
- publicar una notificación permanente;
- exponer su estado.

No debe contener lógica del asistente.

---

# Notificación permanente

Crear un Notification Channel específico.

Ejemplo:

Nombre:

Axxist Runtime

Contenido:

"Axxist está preparado."

La notificación deberá ser sencilla, estable y sin acciones todavía.

---

# RuntimeManager

Será el único responsable de:

- iniciar Runtime;
- detener Runtime;
- reiniciar Runtime;
- consultar estado;
- registrar módulos.

Ningún otro módulo podrá iniciar o detener el servicio directamente.

---

# HealthMonitor

Implementar un monitor interno que verifique periódicamente:

- estado del servicio;
- estado del Runtime;
- módulos registrados.

No deberá reiniciar procesos todavía.

Solo registrar eventos.

---

# Watchdog

Crear infraestructura para detectar:

- excepciones no controladas;
- fallos críticos;
- bloqueos.

En esta etapa solamente registrar.

No aplicar recuperación automática.

---

# EventBus

Agregar eventos nuevos:

RUNTIME_STARTED

RUNTIME_STOPPED

RUNTIME_ERROR

SERVICE_CREATED

SERVICE_DESTROYED

BOOT_COMPLETED

HEALTH_CHECK

---

# Logger

Registrar:

Inicio

Detención

Errores

Cambios de estado

Creación del servicio

Eventos importantes

---

# React Native

Exponer un Native Module que permita consultar:

- estado del Runtime;
- versión;
- tiempo de ejecución.

No exponer acciones de control todavía.

---

# Configuración Android

Agregar únicamente los permisos necesarios para:

Foreground Service.

Boot Completed.

No solicitar permisos relacionados con:

Micrófono

Contactos

Ubicación

Accesibilidad

SMS

Teléfono

Bluetooth

Notificaciones especiales

---

# Restricciones

NO implementar:

Wake Word

Speech To Text

Text To Speech

Groq

IA

Modelos locales

Intent Engine

Memory

Planner

WhatsApp

Spotify

Email

Android Actions

Accessibility

Notification Listener

---

# Testing

Crear pruebas para:

RuntimeManager

RuntimeStateManager

HealthMonitor

ServiceController

---

# APK

Compilar APK Debug.

Instalable en Samsung Galaxy A21.

---

# Reporte

Crear:

reports/STAGE_02_REPORT.md

Debe incluir:

- Runtime implementado.
- Estados soportados.
- Servicios creados.
- BroadcastReceivers registrados.
- Permisos agregados.
- Riesgos encontrados.
- Próximos pasos.

---

# Definición de éxito

El Stage será exitoso únicamente si:

✓ La aplicación compila.

✓ El Runtime inicia correctamente.

✓ El servicio permanece estable.

✓ Se genera la notificación permanente.

✓ No existen cierres inesperados.

✓ Se genera APK.

✓ Se actualiza documentación.

✓ Se realiza commit.

✓ Se realiza push.

✓ Se crea Release.

---

# Entrega obligatoria

1. Resumen ejecutivo.

2. Módulos creados.

3. Servicios creados.

4. Receivers creados.

5. Permisos agregados.

6. Resultado de pruebas.

7. APK generada.

8. Link Release.

9. Riesgos detectados.

10. Recomendaciones para STAGE_03.
