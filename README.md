# Sistema de Gestión de Tareas con eXist-db

Aplicación de escritorio desarrollada en Java que permite gestionar tareas utilizando una arquitectura orientada a componentes y almacenamiento en base de datos XML con eXist-db.

## Descripción

Este proyecto es una aplicación de gestión de tareas que implementa los principios de programación orientada a componentes. Permite crear, leer, actualizar y eliminar tareas (CRUD completo), almacenándolas en una base de datos XML nativa eXist-db.

La aplicación está diseñada de forma modular, donde cada componente tiene una responsabilidad específica y puede funcionar de manera independiente, facilitando el mantenimiento y la escalabilidad del sistema.

## Características

- **Gestión completa de tareas**: Crear, consultar, modificar y eliminar tareas
- **Arquitectura basada en componentes**: Diseño modular y escalable
- **Base de datos XML**: Almacenamiento persistente en eXist-db
- **Interfaz gráfica intuitiva**: Desarrollada con Java Swing
- **Operaciones en tiempo real**: Conexión directa con la base de datos
- **Validación de datos**: Formularios con validación de campos obligatorios
- **Consultas XPath**: Filtrado y búsqueda avanzada de tareas

## Tecnologías Utilizadas

- **Java 23**: Lenguaje de programación
- **Maven**: Gestión de dependencias y construcción del proyecto
- **eXist-db 6.2.0**: Base de datos XML nativa
- **Java Swing**: Framework para interfaz gráfica
- **XML:DB API**: Conector estándar para bases de datos XML
- **Eclipse IDE**: Entorno de desarrollo

## Estructura del Proyecto

```
gestion-tareas-xml/
├── src/main/java/
│   └── com/gestiontareas/
│       ├── modelo/
│       │   └── Tarea.java                    # Modelo de datos
│       ├── componentes/
│       │   ├── bd/
│       │   │   └── ConectorBD.java           # Componente de conexión a BD
│       │   └── gestion/
│       │       └── GestorTareas.java         # Componente de lógica de negocio
│       ├── ui/
│       │   └── VentanaPrincipal.java         # Componente de interfaz gráfica
│       └── main/
│           └── Main.java                     # Clase principal
├── pom.xml
└── README.md
```

## Arquitectura de Componentes

El sistema está dividido en los siguientes componentes independientes:

### 1. Componente Modelo (`Tarea.java`)
Define la estructura de datos de una tarea con los siguientes atributos:
- ID único
- Título
- Descripción
- Estado (pendiente, en_proceso, completada)
- Prioridad (baja, media, alta)
- Fecha de creación
- Fecha de vencimiento

### 2. Componente de Conexión (`ConectorBD.java`)
Responsable de:
- Establecer conexión con eXist-db
- Crear y gestionar colecciones XML
- Proporcionar acceso a la base de datos
- Cerrar recursos de forma segura

### 3. Componente de Gestión (`GestorTareas.java`)
Implementa la lógica de negocio:
- Operaciones CRUD sobre tareas
- Conversión entre objetos Java y XML
- Consultas XPath para búsquedas y filtros
- Validación de operaciones

### 4. Componente de Interfaz (`VentanaPrincipal.java`)
Proporciona la interfaz gráfica:
- Formulario para gestión de tareas
- Tabla con listado de todas las tareas
- Botones de acción (Crear, Actualizar, Eliminar, Refrescar)
- Indicador de estado de conexión

<img width="985" height="584" alt="Captura de pantalla 2026-01-31 110555" src="https://github.com/user-attachments/assets/78958624-50d3-446d-918c-497a1437b2c1" />


### 5. Clase Principal (`Main.java`)
Punto de entrada de la aplicación:
- Inicializa todos los componentes
- Establece la conexión con la base de datos
- Gestiona el ciclo de vida de la aplicación

## Requisitos Previos

Antes de ejecutar la aplicación, necesitas tener instalado:

1. **Java Development Kit (JDK) 23 o superior**
   - Descarga: https://www.oracle.com/java/technologies/downloads/

2. **eXist-db 6.2.0 o superior**
   - Descarga: https://exist-db.org/
   - El servidor debe estar ejecutándose en `localhost:8080`
   - Usuario: `admin`
   - Contraseña: `admin`

3. **Maven** (opcional, para compilar desde código fuente)
   - Descarga: https://maven.apache.org/download.cgi

## Instalación

### Opción 1: Ejecutar JAR precompilado

1. Descarga el archivo `gestion-tareas-xml-1.0.0-jar-with-dependencies.jar`

2. Asegúrate de que eXist-db está ejecutándose:
   ```bash
   # En Windows
   [ruta-exist-db]\bin\startup.bat
   
   # En Linux/Mac
   [ruta-exist-db]/bin/startup.sh
   ```

3. Ejecuta la aplicación:
   ```bash
   java -jar gestion-tareas-xml-1.0.0-jar-with-dependencies.jar
   ```

### Opción 2: Compilar desde código fuente

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/gestion-tareas-xml.git
   cd gestion-tareas-xml
   ```

2. Compila el proyecto con Maven:
   ```bash
   mvn clean package
   ```

3. El JAR se generará en `target/gestion-tareas-xml-1.0.0-jar-with-dependencies.jar`

4. Ejecuta la aplicación:
   ```bash
   java -jar target/gestion-tareas-xml-1.0.0-jar-with-dependencies.jar
   ```

### Opción 3: Importar en Eclipse

1. Abre Eclipse
2. File → Import → Maven → Existing Maven Projects
3. Selecciona la carpeta del proyecto
4. Click derecho en `Main.java` → Run As → Java Application

## Configuración

Si necesitas cambiar las credenciales de eXist-db, edita el archivo `Main.java`:

```java
private static final String USUARIO = "admin";
private static final String PASSWORD = "tu_password";
```

La URI de conexión también puede modificarse en `ConectorBD.java`:

```java
private static final String URI = "xmldb:exist://localhost:8080/exist/xmlrpc";
```

## Uso de la Aplicación

### Crear una tarea

1. Rellena los campos del formulario:
   - Título (obligatorio)
   - Descripción
   - Estado (pendiente, en_proceso, completada)
   - Prioridad (baja, media, alta)
   - Fecha de vencimiento (formato: YYYY-MM-DD)

2. Click en el botón **Crear**

3. La tarea aparecerá en la tabla

<img width="979" height="592" alt="Captura de pantalla 2026-01-31 110603" src="https://github.com/user-attachments/assets/a42ab225-2555-4536-beac-7f30a6c559fe" />
<img width="982" height="577" alt="Captura de pantalla 2026-01-31 111045" src="https://github.com/user-attachments/assets/8dc4e420-06f5-4b0e-9f0c-7c4010afb13b" />




### Actualizar una tarea

1. Selecciona una tarea de la tabla (click en la fila)
2. Los datos se cargarán automáticamente en el formulario
3. Modifica los campos que desees
4. Click en el botón **Actualizar**

### Eliminar una tarea

1. Selecciona una tarea de la tabla
2. Click en el botón **Eliminar**
3. Confirma la eliminación en el diálogo

### Refrescar la lista

Click en el botón **Refrescar** para recargar todas las tareas desde la base de datos

## Almacenamiento de Datos

Las tareas se almacenan en eXist-db en formato XML en la colección `/db/tareas`.

Cada tarea se guarda como un documento XML independiente:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<tarea>
  <id>123e4567-e89b-12d3-a456-426614174000</id>
  <titulo>Estudiar componentes XML</titulo>
  <descripcion>Repasar la teoría de programación orientada a componentes</descripcion>
  <estado>pendiente</estado>
  <prioridad>alta</prioridad>
  <fechaCreacion>2026-01-31</fechaCreacion>
  <fechaVencimiento>2026-02-15</fechaVencimiento>
</tarea>
```

<img width="626" height="201" alt="Captura de pantalla 2026-01-31 111159" src="https://github.com/user-attachments/assets/1489a058-a084-41ec-9f6c-804dd3015a56" />


<img width="726" height="178" alt="Captura de pantalla 2026-01-31 111154" src="https://github.com/user-attachments/assets/74fcda7e-bc80-4844-b0ed-7c0b3d14527f" />


## Dependencias del Proyecto

El proyecto utiliza las siguientes dependencias Maven:

```xml
<dependency>
    <groupId>org.exist-db</groupId>
    <artifactId>exist-core</artifactId>
    <version>6.2.0</version>
</dependency>

<dependency>
    <groupId>xml-resolver</groupId>
    <artifactId>xml-resolver</artifactId>
    <version>1.2</version>
</dependency>
```

## Solución de Problemas

### Error: "No se pudo conectar con eXist-db"

**Causa**: eXist-db no está ejecutándose o no está en el puerto correcto

**Solución**:
1. Verifica que eXist-db esté ejecutándose
2. Abre `http://localhost:8080/exist/` en tu navegador
3. Si no responde, inicia eXist-db con los scripts de inicio

### Error: "ClassNotFoundException: org.exist.xmldb.DatabaseImpl"

**Causa**: Las dependencias de Maven no se descargaron correctamente

**Solución**:
1. En Eclipse: Click derecho en proyecto → Maven → Update Project
2. O ejecuta: `mvn clean install`

### Error al crear tareas: "Formato de fecha inválido"

**Causa**: La fecha no está en el formato correcto

**Solución**: Usa el formato YYYY-MM-DD (ejemplo: 2026-02-15)

## Características Técnicas

- **Patrón de diseño**: Arquitectura basada en componentes
- **Separación de responsabilidades**: Cada componente tiene una función específica
- **Persistencia**: Almacenamiento en base de datos XML nativa
- **API utilizada**: XML:DB para acceso a eXist-db
- **Consultas**: XPath para búsquedas y filtros
- **Interfaz**: Java Swing con diseño responsive

## Futuras Mejoras

- [ ] Filtrado avanzado de tareas por múltiples criterios
- [ ] Exportación de tareas a diferentes formatos (PDF, CSV)
- [ ] Sistema de etiquetas para categorizar tareas
- [ ] Recordatorios y notificaciones
- [ ] Modo oscuro en la interfaz
- [ ] Búsqueda de texto completo
- [ ] Historial de cambios en tareas
- [ ] Soporte multi-usuario con permisos

## Autor

**Esteban Sanchez**
- Proyecto académico - 2º DAM
- Año: 2026

## Licencia

Este proyecto está bajo la Licencia MIT. Ver archivo [LICENSE](LICENSE) para más detalles.

## Agradecimientos

- A Anthropic por el desarrollo de tecnologías de IA
- A la comunidad de eXist-db por la documentación y soporte
- Al equipo docente de DAM por la guía en el proyecto

---

**Nota**: Este proyecto fue desarrollado como parte del módulo de Acceso a Datos del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).
