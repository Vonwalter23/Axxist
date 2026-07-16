# STAGE_07_INTENT_ENGINE

## Objetivo

Construir el Intent Engine de Axxist.

Este componente será responsable de interpretar la respuesta de la IA (Groq u otro proveedor) y convertirla en una intención estructurada que el sistema pueda ejecutar.

Ningún módulo podrá ejecutar acciones directamente a partir del texto generado por la IA.

---

## Dependencias

- STAGE_00 a STAGE_06 completados.

---

## Objetivos técnicos

Implementar:

- IntentEngine
- IntentParser
- IntentRegistry
- IntentValidator
- IntentExecutor (estructura)
- EntityExtractor
- ConfidenceEvaluator
- IntentDiagnostics

---

## Formato de salida obligatorio

Toda IA deberá responder utilizando un formato estructurado (JSON).

Ejemplo:

{
  "intent": "CALL_CONTACT",
  "confidence": 0.98,
  "entities": {
    "contact": "Juan Pérez"
  }
}

---

## Intentos mínimos soportados

- CALL_CONTACT
- SEND_WHATSAPP
- SEND_EMAIL
- OPEN_APP
- PLAY_SPOTIFY
- SEARCH_WEB
- CREATE_REMINDER
- CREATE_ALARM
- READ_NOTIFICATIONS
- UNKNOWN

---

## Entity Extractor

Extraer:

- nombres
- teléfonos
- fechas
- horas
- aplicaciones
- direcciones
- correos electrónicos

---

## Restricciones

No ejecutar todavía ninguna acción Android.

Solo interpretar.

---

## Testing

Validar más de 100 ejemplos de intents.

---

## Reporte

reports/STAGE_07_REPORT.md
