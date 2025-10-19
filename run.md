# 🚀 Cómo Ejecutar Este Proyecto

Sigue estos pasos para construir, probar y ejecutar la aplicación.

---

## 📋 Requisitos Previos

- **Java 17** o superior
- **Gradle** (Opcional, puedes usar el Gradle Wrapper incluido)
- **Docker** (Opcional, para ejecutar con Docker)
- **Git** (para clonar el repositorio)

---

## 📥 Opción 1: Ejecutar Localmente con Gradle

### 1. Obtener el Proyecto

Tienes dos opciones para obtener el proyecto:

#### Opción A: Clonar desde GitHub (Recomendado)

Clona el repositorio desde GitHub usando Git:

```bash
git clone https://github.com/dgpenalozap/MeLiPrueba.git
cd MeLiPrueba
```

#### Opción B: Descargar como archivo comprimido

Si tienes el proyecto como archivo `.zip` o `.rar`:

1. **Para archivos .zip:**
   - En Windows: Click derecho → "Extraer aquí" o "Extract Here"
   - En macOS: Doble click en el archivo
   - En Linux: `unzip MeLiPrueba.zip`

2. **Para archivos .rar:**
   - Necesitarás WinRAR, 7-Zip o una herramienta similar
   - En Windows: Click derecho → "Extraer aquí"
   - En Linux: `unrar x MeLiPrueba.rar` o `7z x MeLiPrueba.rar`

3. **Navega al directorio extraído:**
   ```bash
   cd MeLiPrueba
   ```
   ó
   ```bash
   cd MeLiPrueba-main
   ```

### 2. Construir la Aplicación

Abre una terminal o símbolo del sistema en el directorio raíz del proyecto y ejecuta el siguiente comando para compilar el código y construir el proyecto. Esto también descargará todas las dependencias necesarias.

**En macOS/Linux:**
```bash
./gradlew clean build
```

**En Windows:**
```bash
gradlew.bat clean build
```

### 3. Ejecutar las Pruebas

Para asegurarte de que todo funciona correctamente, ejecuta las pruebas unitarias:

**En macOS/Linux:**
```bash
./gradlew test
```

**En Windows:**
```bash
gradlew.bat test
```

Deberías ver un mensaje `BUILD SUCCESSFUL`, indicando que todas las 8 pruebas han pasado exitosamente.

### 4. Ejecutar la Aplicación

Una vez que el proyecto esté construido y probado, puedes iniciar la aplicación con el comando `bootRun`.

**En macOS/Linux:**
```bash
./gradlew bootRun
```

**En Windows:**
```bash
gradlew.bat bootRun
```

La aplicación se iniciará en el servidor Tomcat embebido, típicamente en el puerto 8080.

---

## 🐳 Opción 2: Ejecutar con Docker

### 1. Construir la Imagen de Docker

Desde el directorio raíz del proyecto, construye la imagen de Docker:

```bash
docker build -t product-comparison-app .
```

### 2. Ejecutar el Contenedor de Docker

Ejecuta la aplicación en un contenedor de Docker:

```bash
docker run -p 8080:8080 product-comparison-app
```

**Con variables de entorno (opcional):**
```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e OPENAI_API_KEY=tu_api_key_aqui \
  product-comparison-app
```

**Ejecutar en modo desacoplado:**
```bash
docker run -d -p 8080:8080 --name product-comparison product-comparison-app
```

### 3. Comandos Útiles de Docker

**Ver logs:**
```bash
docker logs product-comparison
```

**Seguir logs en tiempo real:**
```bash
docker logs -f product-comparison
```

**Detener el contenedor:**
```bash
docker stop product-comparison
```

**Eliminar el contenedor:**
```bash
docker rm product-comparison
```

**Eliminar la imagen:**
```bash
docker rmi product-comparison-app
```

---

## 🌐 Acceder a la API

Una vez que la aplicación esté ejecutándose (localmente o en Docker), puedes acceder a las siguientes URLs:

### Endpoints Principales

| Descripción | URL |
|-------------|-----|
| **Endpoint Principal de la API** | [http://localhost:8080/api/products](http://localhost:8080/api/products) |
| **Documentación Interactiva (Swagger UI)** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| **Health Check (si actuator está habilitado)** | [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) |

---

## 🎯 Resumen de Comandos Rápidos

### Ejecución Local
```bash
# Clonar repositorio
git clone https://github.com/dgpenalozap/MeLiPrueba.git
cd MeLiPrueba

# Construir y ejecutar (Linux/Mac)
./gradlew clean build
./gradlew test
./gradlew bootRun

# Construir y ejecutar (Windows)
gradlew.bat clean build
gradlew.bat test
gradlew.bat bootRun
```

### Ejecución con Docker
```bash
# Construir imagen
docker build -t product-comparison-app .

# Ejecutar contenedor
docker run -p 8080:8080 product-comparison-app
```

---

## ❓ Solución de Problemas

- **Error de Java:** Verifica que tienes Java 17 o superior instalado: `java -version`
- **Puerto ocupado:** Si el puerto 8080 está en uso, puedes cambiarlo con `-Dserver.port=8081` al ejecutar
- **Problemas con Gradle:** Asegúrate de tener permisos de ejecución en `gradlew`: `chmod +x gradlew`

---

## 📝 Notas Adicionales

- La primera ejecución puede tardar más tiempo debido a la descarga de dependencias
- Los logs de la aplicación se mostrarán en la consola durante la ejecución
- Para detener la aplicación, presiona `Ctrl + C` en la terminal

---

**¡Listo! Tu aplicación debería estar ejecutándose exitosamente.** 🎉