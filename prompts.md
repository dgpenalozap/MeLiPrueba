# 🚀 Prompts & Herramientas GenAI Utilizadas

Este documento recopila los prompts y herramientas de IA empleadas para la generación, refactorización y documentación de este proyecto.

## 🤖 GenAI y Herramientas Modernas de Desarrollo

La integración de GenAI y herramientas modernas como Copilot y Gemini CLI potencia el desarrollo en varias dimensiones:

- **Contexto enriquecido:**  
  El uso de CLI (Copilot, Gemini) permite a las herramientas comprender el proyecto completo, generando documentación precisa y relevante.

- **Cambios globales:**  
  Estas herramientas pueden modificar cualquier parte del código, facilitando la incorporación de nuevas funcionalidades o refactorizaciones rápidas.

- **Generación y corrección ágil:**  
  Copilot acelera la creación y corrección de código, permitiendo resolver tareas repetitivas o complejas en segundos.

- **Consultas instantáneas:**  
  Interactuar con un chat para resolver dudas técnicas incrementa la productividad y reduce tiempos de bloqueo.

- **Pruebas automatizadas:**  
  La generación de pruebas unitarias, de integración y aceptación es sencilla; basta con definir el tipo de prueba y dar un prompt claro para obtener código confiable.

- **Revisión obligatoria:**  
  Todo el código generado se revisa manualmente para evitar errores, bugs o vulnerabilidades.

- **Personalización:**  
  Es posible ajustar los prompts para obtener pruebas o código adaptado a necesidades específicas del proyecto.

> Nota: La integración de GenAI no reemplaza la revisión humana, pero sí optimiza el flujo de trabajo y eleva la calidad del software.
## 🛠️ Herramientas de IA Utilizadas

| Herramienta                                 | Propósito principal                                              |
|---------------------------------------------|------------------------------------------------------------------|
| **GitHub Copilot CLI**                      | Operaciones en terminal y manipulación de archivos               |
| **Gemini CLI**                              | Generación de código, refactorización y actualización de docs    |
| **GitHub Copilot (IntelliJ IDEA Plugin)**   | Autocompletado y sugerencias en tiempo real en el IDE            |

---

## 🏗️ Generación Inicial del Proyecto

- **Prompt:**  
  > Crea un proyecto Spring Boot utilizando Gradle que implemente una API REST para comparar productos. El almacenamiento de los productos debe realizarse en un archivo JSON local en vez de una base de datos tradicional. Asegúrate de incluir endpoints REST para `/api/products` (listado y creación de productos) y `/api/products/{id}` (consulta, actualización y eliminación de un producto específico). Incluye manejo básico de errores y comentarios en línea explicando la lógica de cada método. Genera un archivo JSON de ejemplo con al menos 10 productos realistas, cada uno con nombre, descripción, precio, categoría y calificación.

---

## 🧩 Refactorización & Arquitectura

- **Prompt:**  
  > Refactoriza el proyecto para adoptar una arquitectura hexagonal (puertos y adaptadores). Crea interfaces para los servicios y repositorios, separando claramente la lógica de negocio del acceso a datos y la exposición vía controladores REST. Aplica los principios SOLID, especialmente el de Inversión de Dependencias, utilizando inyección de dependencias para todos los componentes. Añade servicios adicionales que permitan filtrar productos por precio, calificación y categoría. Implementa un manejador global de excepciones que devuelva respuestas de error en formato JSON consistente para todos los controladores. Integra Swagger (SpringDoc) para documentar automáticamente la API REST.

---

## 🔐 Autenticación y Seguridad

- **Prompt:**  
  > Implementa autenticación basada en JWT en la API de productos. Configura Spring Security para proteger todos los endpoints, permitiendo el acceso solo a usuarios autenticados mediante tokens JWT válidos. Crea endpoints para registro y login de usuarios, devolviendo el token JWT correspondiente tras la autenticación. Añade roles y permisos diferenciados para usuarios tipo 'admin' y 'user', restringiendo ciertas operaciones (como creación, actualización y eliminación de productos) solo a administradores.

---

## 🧪 Pruebas

### Pruebas Unitarias

- **Prompt:**  
  > Genera pruebas unitarias para cada servicio utilizando JUnit 5 y Mockito, asegurando la cobertura de todos los casos de uso principales. Implementa pruebas para los métodos del repositorio de productos utilizando mocks. Escribe pruebas para las clases de validación y excepciones personalizadas, verificando que se lancen los errores correctos ante entradas inválidas.

### Pruebas de Integración

- **Prompt:**  
  > Desarrolla pruebas de integración para los endpoints REST utilizando MockMvc, cubriendo los flujos principales de la API. Implementa pruebas que verifiquen el comportamiento del repositorio con datos reales del archivo JSON. Crea pruebas que validen la integración entre servicios y controladores, asegurando que la lógica de negocio y la exposición REST funcionen correctamente en conjunto.

### Pruebas de Aceptación

- **Prompt:**  
  > Implementa pruebas de aceptación con Cucumber para validar los flujos completos de la API de comparación de productos. Escribe escenarios en Gherkin que cubran las funcionalidades principales, como registro de usuario, autenticación, consulta, filtrado y comparación de productos. Añade pruebas end-to-end que simulen el comportamiento real de un usuario interactuando con la API.

---

## 🔄 Mapeo y Transformación de Datos

- **Prompt:**  
  > Crea mapeadores utilizando MapStruct para convertir entre entidades, DTOs y modelos de respuesta. Implementa validaciones personalizadas en los DTOs utilizando Bean Validation (anotaciones como @NotNull, @Size, etc.). Añade soporte para paginación y ordenamiento en los endpoints que devuelven listas de productos, permitiendo filtrar y ordenar por diferentes atributos.

---

## 📝 Operaciones CRUD Adicionales

- **Prompt:**  
  > Implementa endpoints PUT para actualizar la información completa de un producto. Crea operaciones PATCH para actualizaciones parciales de recursos. Añade soporte para eliminación lógica (soft delete) de productos, marcando los productos como inactivos en vez de eliminarlos físicamente del archivo JSON.

---

## 🧬 Generación de Datos y Contenido

- **Prompt:**  
  > Utiliza la librería `dev.langchain4j:langchain4j:0.33.0` para generar datos de productos aleatorios y realistas. Crea un script que genere un archivo JSON con 100 productos variados en categorías, precios y descripciones. Implementa un generador de descripciones detalladas para productos utilizando modelos de lenguaje (LLMs), asegurando que cada producto tenga una descripción única y convincente.

---

## 📚 Documentación

- **Prompt:**  
  > Crea documentación técnica detallada que explique la implementación de JWT y la configuración de seguridad en la API. Genera diagramas de arquitectura utilizando PlantUML para visualizar la estructura hexagonal del proyecto. Documenta todos los endpoints disponibles, incluyendo ejemplos de solicitudes y respuestas en formato Markdown. Añade un tutorial paso a paso sobre cómo consumir la API desde diferentes clientes (curl, Postman, JavaScript).

---

## 📝 Actualización de Documentación

- **Prompt:**  
  > Actualiza el archivo `README.md` para incluir una descripción de la arquitectura, los patrones de diseño utilizados y un enlace a la documentación Swagger. Mantén actualizado el archivo `prompts.md` con los últimos prompts utilizados y menciona las herramientas de IA empleadas. Crea un archivo `run.md` con todos los pasos necesarios para compilar, probar y ejecutar el proyecto desde cero.

---

> _Última actualización: 2025-10-19