# Proceso de Generación de APK Release - Axxist

Este documento describe el proceso para generar una APK release de producción para Axxist.

---

## 1. Configuración Inicial (Una vez)

### 1.1 Generar Keystore de Release

```bash
# En el directorio android/
cd android/

# Generar keystore (reemplazar con tus datos)
keytool -genkey -v -keystore release.keystore \
    -alias axxist-release \
    -keyalg RSA -keysize 2048 -validity 10000 \
    -storepass tu_store_password \
    -keypass tu_key_password \
    -dname "CN=Tu Nombre, OU=Organización, O=Empresa, L=Ciudad, ST=Estado, C=CódigoPaís"
```

### 1.2 Configurar keystore.properties

```bash
# Copiar el template
cp keystore.properties.example keystore.properties

# Editar con tus datos
nano keystore.properties
```

Contenido esperado:
```properties
RELEASE_KEYSTORE_PATH=release.keystore
RELEASE_STORE_PASSWORD=tu_store_password
RELEASE_KEY_ALIAS=axxist-release
RELEASE_KEY_PASSWORD=tu_key_password
```

### 1.3 Asegurar el archivo (NO commitear)

```bash
# Agregar a .gitignore si no está
echo "keystore.properties" >> .gitignore
echo "*.keystore" >> .gitignore
echo "*.jks" >> .gitignore

# Guardar keystore en lugar seguro
# O usar GitHub Secrets para CI/CD
```

---

## 2. Generación de APK Release (Desarrollo Local)

### 2.1 Debug APK (Desarrollo)

```bash
cd android/
./gradlew assembleDebug
```

Salida: `android/app/build/outputs/apk/debug/app-debug.apk`

### 2.2 Release APK (Con keystore configurado)

```bash
cd android/
./gradlew assembleRelease
```

Salida: `android/app/build/outputs/apk/release/app-release.apk`

### 2.3 Release APK (Unsigned - Solo para testing)

Si no tienes keystore configurado, puedes generar una APK unsigned:

```bash
cd android/
./gradlew assembleRelease -x signReleaseJsAndAssets
```

**⚠️ ADVERTENCIA**: Esta APK no puede ser instalada en dispositivos sin modificar la configuración de seguridad.

---

## 3. GitHub Actions CI/CD

### 3.1 Configurar Secrets en GitHub

1. Ir a GitHub Repository → Settings → Secrets and variables → Actions
2. Agregar:
   - `RELEASE_KEYSTORE_BASE64`: Base64 del keystore
   - `RELEASE_STORE_PASSWORD`: Password del keystore
   - `RELEASE_KEY_PASSWORD`: Password de la key
   - `RELEASE_KEY_ALIAS`: Alias de la key

### 3.2 Generar Base64 del Keystore

```bash
# macOS/Linux
base64 -i release.keystore -o release.keystore.base64

# Windows (PowerShell)
[Convert]::ToBase64String([IO.File]::ReadAllBytes("release.keystore")) > release.keystore.base64
```

### 3.3 Workflow de GitHub Actions

El proyecto usa el workflow en `.github/workflows/android-release.yml` (si existe).

Para generar manualmente:
```bash
# Trigger workflow desde CLI
gh workflow run release.yml --field version=0.0.2-android-core
```

---

## 4. Verificación de APK

### 4.1 Verificar Signature

```bash
# Listar firma
jarsigner -verify -verbose -certs app-release.apk

# Ver información de firma
keytool -printcert -jarfile app-release.apk
```

### 4.2 Verificar Metadatos

```bash
# Usar aapt (Android Asset Packaging Tool)
aapt dump badging app-release.apk | grep -E "package:|application-label:|application-icon:"
```

Salida esperada:
```
package: name='com.axxist.app' versionCode='2' versionName='0.0.2-android-core'
application-label:'Axxist'
```

### 4.3 Instalar en Dispositivo

```bash
# USB debugging habilitado requerido
adb install app-release.apk

# O para reinstalar
adb install -r app-release.apk
```

---

## 5. Estructura de Build

```
android/
├── app/
│   ├── build.gradle
│   ├── debug.keystore          # Para debug builds
│   └── build/
│       └── outputs/
│           ├── apk/
│           │   ├── debug/
│           │   │   └── app-debug.apk
│           │   └── release/
│           │       └── app-release.apk
│           └── bundled/
│               └── javascript/
│                   └── index.android.bundle
├── keystore.properties.example # Template para configuración
├── keystore.properties         # NO COMMITEAR - Configuración local
└── release.keystore           # NO COMMITEAR - Keystore de producción
```

---

## 6. Troubleshooting

### Error: "Keystore not found"

```bash
# Verificar que keystore.properties existe y tiene la ruta correcta
cat keystore.properties | grep RELEASE_KEYSTORE_PATH

# Verificar que el archivo existe
ls -la android/release.keystore
```

### Error: "Wrong password"

```bash
# Verificar passwords en keystore.properties
# Regenerar keystore si es necesario
```

### Error: "Key not found"

```bash
# Verificar que el alias coincida
keytool -list -keystore release.keystore
```

### Error: "minifyEnabled requires..."

```bash
# Asegurar que proguard-rules.pro existe
cat android/app/proguard-rules.pro
```

---

## 7. Checklist Pre-Release

- [ ] Versión actualizada en `build.gradle`
- [ ] Changelog actualizado
- [ ] Keystore configurado
- [ ] APK firmada verificada
- [ ] Test en dispositivo físico
- [ ] Test de instalación/desinstalación
- [ ] Release notes preparadas

---

## 8. Roles y Responsabilidades

| Rol | Responsabilidad |
|-----|-----------------|
| Developer | Generar debug APKs durante desarrollo |
| Release Manager | Generar y firmar release APKs |
| DevOps | Mantener CI/CD y GitHub Actions |
| Security | Proteger keystores y secrets |

---

**Última actualización**: 2024-07-16  
**Versión del documento**: 1.0
