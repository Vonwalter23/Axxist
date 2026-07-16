# STAGE_06 AI ROUTER - INFORME TÉCNICO

## Resumen Ejecutivo

STAGE_06 AI Router implementa la capa inteligente de selección y gestión de proveedores IA para Axxist, proporcionando la infraestructura para coordinar múltiples proveedores de IA con fallback automático.

**Objetivos alcanzados:**
- ✅ AIManager con coordinación de proveedores
- ✅ AIRouter con selección y fallback
- ✅ AIProvider placeholders (Groq, Gemini, OpenAI, Local)
- ✅ AIConfiguration para configuración de proveedores
- ✅ AIStateManager con state machine
- ✅ AI Events en EventBus
- ✅ Capability AI_ROUTER
- ✅ Sin implementación real de APIs

---

## 1. Módulos Creados

### 1.1 Configuration

**AIConfiguration.kt**
- Configuración para proveedores y routing
- Parámetros: preferredProvider, fallbackProvider, timeout, model, temperature, etc.
- Método: `isProviderAvailable()`, `getTimeout()`, `getRetryCount()`

### 1.2 State Management

**AIState.kt**
- Enum con 5 estados: DISABLED, IDLE, REQUESTING, RESPONDING, ERROR
- AIProviderStatus: Estado de cada proveedor

**AIStateManager.kt**
- Singleton para gestión de estados de IA
- Historial de transiciones
- Tracking de uso de proveedores

### 1.3 AI Providers

**GroqProvider.kt**
- Placeholder para Groq API
- Modelos: llama-3.3-70b-versatile, llama-3.1-8b-instant, etc.

**GeminiProvider.kt**
- Placeholder para Google Gemini
- Modelos: gemini-1.5-flash, gemini-1.5-pro, etc.

**OpenAIProvider.kt**
- Placeholder para OpenAI
- Modelos: gpt-4o, gpt-4o-mini, etc.

**LocalAIProvider.kt**
- Placeholder para LLM local (Ollama)
- Modelos: llama3.2, mistral, phi3, etc.

### 1.4 AIRouter

**AIRouter.kt**
- Coordinator para routing de requests
- Responsabilidades:
  - Seleccionar provider según configuración
  - Manejar fallback automático
  - Controlar disponibilidad de providers
  - Trackear uso y errores

### 1.5 AIManager

**AIManager.kt**
- Punto de entrada para el sistema de IA
- Responsabilidades:
  - Recibir solicitudes de conversación
  - Coordinar providers
  - Devolver respuestas
  - Integración con ConversationEngine

---

## 2. Flujo de AI

```
User Message
      ↓
Conversation Engine
      ↓
AIManager.sendRequest()
      ↓
AIRouter.selectProvider()
      ↓
Selected Provider (Groq/Gemini/OpenAI/Local)
      ↓
AI Response
      ↓
Conversation Engine
      ↓
Response Output
```

---

## 3. Estados de AI

```
DISABLED → IDLE → REQUESTING → RESPONDING → IDLE
                ↓               ↓
              ERROR ←───────────┘
```

---

## 4. Eventos de AI

| Evento | Descripción |
|--------|-------------|
| `AI_REQUEST_STARTED` | Request de IA iniciado |
| `AI_PROVIDER_SELECTED` | Provider seleccionado |
| `AI_RESPONSE_RECEIVED` | Respuesta recibida |
| `AI_STATE_CHANGED` | Cambio de estado |
| `AI_ERROR` | Error en AI |

---

## 5. Configuración de AI

**AIConfiguration:**
```kotlin
AIConfiguration(
    enabled = true,
    preferredProvider = GROQ,
    fallbackProvider = null,
    timeoutMs = 30000,
    retryCount = 2,
    model = "default",
    temperature = 0.7f,
    maxTokens = 1000,
    systemPrompt = "You are Axxist...",
    enableFallback = true,
    logRequests = true
)
```

---

## 6. Provider Types

| Provider | Tipo | Modelos |
|---------|------|---------|
| GROQ | Cloud | llama-3.3-70b-versatile |
| GEMINI | Cloud | gemini-1.5-flash |
| OPENAI | Cloud | gpt-4o-mini |
| LOCAL | Local | llama3.2 |

---

## 7. Estructura de Archivos

```
android/app/src/main/java/com/axxist/app/runtime/ai/
├── AIManager.kt                  # Coordinator principal
├── AIRouter.kt                   # Router de providers
├── config/
│   └── AIConfiguration.kt        # Configuración
├── provider/
│   ├── GroqProvider.kt          # Placeholder Groq
│   ├── GeminiProvider.kt         # Placeholder Gemini
│   ├── OpenAIProvider.kt        # Placeholder OpenAI
│   └── LocalAIProvider.kt       # Placeholder Local
└── state/
    ├── AIState.kt               # Enum de estados
    └── AIStateManager.kt         # Gestor de estados
```

---

## 8. Capability

**Agregada:**
- `AI_ROUTER`: AI provider routing (NOT_IMPLEMENTED)

---

## 9. Decisiones Técnicas

### 9.1 Arquitectura Desacoplada
- AIManager coordina sin conocer implementaciones específicas
- AIRouter selecciona provider según configuración
- Providers son intercambiables

### 9.2 Fallback Strategy
- Si falla el provider preferido, intenta fallback
- Si falla fallback, retorna error
- Configurable via `enableFallback`

### 9.3 Provider Availability
- Cada provider reporta su disponibilidad
- AIRouter verifica antes de enviar request
- Estado de providers trackeado

### 9.4 Placeholder Implementations
- Providers no realizan llamadas reales
- Retornan respuestas placeholder
- API keys no almacenadas

---

## 10. Resultados de Build

### Debug APK
```
✅ BUILD SUCCESSFUL
```

### Release APK
```
✅ BUILD SUCCESSFUL
```

---

## 11. Restricciones Cumplidas

| Restricción | Estado |
|-------------|--------|
| Sin API keys | ✅ |
| Sin llamadas reales | ✅ |
| Sin comandos reales | ✅ |
| Arquitectura desacoplada | ✅ |
| Fallback preparado | ✅ |

---

## 12. Puntos Pendientes (Futuros Stages)

### STAGE_07 - Intent Engine
- Implementar IntentProcessor real
- Definir intents soportados
- Integrar con AI Router

### STAGE_XX - AI Integration
- Implementar GroqProvider real
- Implementar GeminiProvider real
- Agregar API key management seguro

---

## 13. Recomendaciones

### 13.1 Para Implementación Real
1. Implementar GroqProvider usando Retrofit
2. Configurar API keys de forma segura
3. Agregar retry logic con exponential backoff

### 13.2 Testing
1. Probar fallback entre providers
2. Probar timeout handling
3. Probar estado de errores

### 13.3 Optimizaciones Futuras
1. Connection pooling
2. Response caching
3. Cost tracking por provider

---

## 14. Definición de Éxito

| Criterio | Estado |
|----------|--------|
| Aplicación compila | ✅ |
| AIManager implementado | ✅ |
| AIRouter implementado | ✅ |
| Providers placeholders | ✅ |
| Configuration | ✅ |
| State machine | ✅ |
| Events integrados | ✅ |
| Capability | ✅ |
| Documentación creada | ✅ |
| Commit realizado | ✅ |
| Push realizado | ✅ |

---

**Versión del reporte**: 1.0  
**Fecha**: 2024-07-16  
**Stage**: STAGE_06 AI Router  
**Estado**: ✅ COMPLETADO
