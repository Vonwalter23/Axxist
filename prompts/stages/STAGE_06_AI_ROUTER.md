# STAGE_06_AI_ROUTER

## Proyecto

Axxist

---

## Objetivo

Construir el AI Router de Axxist.

Este componente será el único punto de acceso a cualquier modelo de Inteligencia Artificial utilizado por el asistente.

Ningún otro módulo del sistema podrá comunicarse directamente con Groq ni con futuros proveedores.

El AI Router decidirá qué proveedor utilizar según la configuración y las capacidades disponibles.

En esta etapa se integrará Groq como primer proveedor y se dejará preparada la infraestructura para modelos locales y futuros motores de IA.

---

## Dependencias

Requiere:

- STAGE_00 al STAGE_05 completados.

---

## Lectura obligatoria

Antes de comenzar:

- PROMPT_MASTER_V1.md
- Todos los REPORTS anteriores

---

# Objetivos técnicos

Implementar:

- AIRouter
- AIProvider
- GroqProvider
- LocalAIProvider (estructura)
- AIRequest
- AIResponse
- AIContext
- AIConfiguration
- AIHealthMonitor
- AICapabilityRegistry
- AIProviderFactory

Todos los componentes deberán ser desacoplados y seguir principios SOLID.

---

# AIProvider

Definir una interfaz común para todos los motores de IA.

Debe soportar:

- enviar prompts
- recibir respuestas
- consultar disponibilidad
- obtener capacidades
- cancelar solicitudes
- informar errores

Ningún módulo podrá depender de implementaciones concretas.

---

# AIRouter

Será el único responsable de:

- seleccionar proveedor
- enrutar solicitudes
- aplicar políticas
- manejar errores
- cambiar de proveedor
- registrar métricas

No contendrá lógica específica de Groq.

---

# GroqProvider

Implementar el primer proveedor funcional utilizando la API oficial de Groq.

Debe soportar:

- autenticación mediante API Key
- solicitudes síncronas
- solicitudes asíncronas
- manejo de errores
- reintentos configurables
- timeout configurable

La API Key deberá almacenarse de forma segura utilizando variables de entorno o almacenamiento seguro. Nunca hardcodearla en el código.

---

# LocalAIProvider

Crear únicamente la infraestructura.

No ejecutar modelos locales todavía.

Preparar soporte para:

- Qwen
- Gemma
- Phi
- SmolLM

---

# AIConfiguration

Centralizar:

- proveedor activo
- modelo utilizado
- timeout
- temperatura
- tokens máximos
- políticas de fallback

Todos los parámetros deberán ser configurables.

---

# AIContext

Crear una estructura para transportar el contexto de una conversación.

Debe ser independiente del proveedor.

Preparar soporte para memoria futura.

---

# AIResponse

Normalizar las respuestas de cualquier proveedor.

Debe incluir:

- texto
- proveedor
- modelo
- tiempo de respuesta
- uso de tokens (si está disponible)
- estado
- errores

---

# Fallback

Preparar un mecanismo para:

1. intentar con Groq;
2. si falla y existe un proveedor alternativo habilitado, utilizarlo;
3. registrar el evento.

No implementar lógica compleja de balanceo en esta etapa.

---

# EventBus

Agregar:

AI_REQUEST_STARTED

AI_REQUEST_COMPLETED

AI_REQUEST_FAILED

AI_PROVIDER_CHANGED

AI_PROVIDER_UNAVAILABLE

AI_TIMEOUT

---

# Logger

Registrar:

- proveedor utilizado
- modelo
- tiempo de respuesta
- errores
- fallback
- cancelaciones

Nunca registrar la API Key ni datos sensibles.

---

# React Native

Crear Native Modules para consultar:

- proveedor activo
- estado del AI Router
- latencia
- estadísticas básicas

No exponer la API Key ni permitir modificar configuraciones críticas desde la interfaz.

---

# Configuración

Agregar soporte para:

- variables de entorno
- BuildConfig
- configuración Debug y Release

Preparar el proyecto para almacenar secretos de forma segura.

---

# Restricciones

NO implementar:

- ejecución de comandos Android
- WhatsApp
- Spotify
- llamadas
- correo electrónico
- Planner
- Memory Engine
- automatizaciones
- plugins
- conversación continua

Este Stage únicamente integra el enrutamiento de IA.

---

# Testing

Crear pruebas para:

- AIRouter
- AIProvider
- GroqProvider (con mocks)
- AIConfiguration
- AIResponse
- AIContext

No realizar pruebas que consuman la API real por defecto.

---

# APK

Generar APK Debug instalable en Samsung Galaxy A21.

---

# Documentación

Actualizar:

- README
- PROJECT_BOOK
- CHANGELOG

Documentar cómo configurar la API Key de Groq para desarrollo y producción.

---

# Reporte

Crear:

reports/STAGE_06_REPORT.md

Debe incluir:

- arquitectura del AI Router
- proveedor implementado
- diagrama de flujo
- estrategia de fallback
- configuración de seguridad
- pruebas ejecutadas
- riesgos detectados
- recomendaciones para STAGE_07

---

# Definición de éxito

El Stage será exitoso únicamente si:

✓ El AI Router selecciona correctamente el proveedor activo.

✓ Groq responde correctamente utilizando la API configurada.

✓ Los errores se manejan sin afectar al Runtime.

✓ La API Key nunca queda expuesta.

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
3. Interfaces creadas.
4. Integración con Groq.
5. Estrategia de fallback.
6. Resultado de pruebas.
7. APK generada.
8. Link de la Release.
9. Riesgos detectados.
10. Recomendaciones para STAGE_07.
