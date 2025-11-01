# BlockBreaker 2024  

Proyecto desarrollado en **Java 17** utilizando el framework **LibGDX**.  
Es una versión personalizada y mejorada del clásico *Block Breaker*, con nuevos elementos visuales, niveles progresivos, bloques de distinta resistencia y múltiples pelotas en simultáneo.

---

## Requisitos de instalación

### Requisitos mínimos
- **Java Development Kit (JDK):** versión **17** (se recomienda [Eclipse Adoptium Temurin 17](https://adoptium.net/)).  
- **NetBeans 21**, **IntelliJ IDEA** o **Eclipse** con soporte Gradle.  
- **Gradle Wrapper** incluido en el proyecto (`gradlew` o `gradlew.bat`).  
- **Sistema operativo:** Windows, macOS o Linux (compatible con LWJGL3).  

---

## Estructura del proyecto

El proyecto está dividido en módulos siguiendo la arquitectura estándar de **LibGDX**:

- **core/** → Contiene la lógica principal del juego (`BlockBreakerGame`, `PingBall`, `Paddle`, `Block`, `BlockHard`, `GameObject`, `Hittable`, etc.).  
- **lwjgl3/** → Lanzador para escritorio (Windows/Linux/macOS).  
- **assets/** → Carpeta de recursos (fuentes, texturas, sonidos).  
- **build.gradle** → Configuración principal de dependencias y tareas Gradle.  

---

## Instrucciones para ejecutar el juego

### Opción 1: Desde NetBeans / IntelliJ IDEA
1. Abre tu IDE (NetBeans o IntelliJ).  
2. Selecciona **Archivo → Abrir proyecto** y busca la carpeta `BlockBreaker2024`.  
3. Configura el proyecto para usar **Java 17**:  
   - En NetBeans:  
     - Ve a **Herramientas → Plataformas Java → Añadir Plataforma...**  
     - Agrega el JDK 17 y márcalo como predeterminado.  
   - En IntelliJ:  
     - Ve a **File → Project Structure → Project SDK → Add JDK...** y selecciona la carpeta donde está instalado JDK 17.  
4. Una vez abierto, ubica el módulo **lwjgl3** (lanzador).  
5. Haz clic derecho sobre él → **Run** o **Ejecutar**.  
6. El juego se abrirá en una ventana (por defecto 1280x720 píxeles).  

---

### Opción 2: Desde línea de comandos
1. Abre una terminal en la carpeta raíz del proyecto.  
2. Ejecuta el siguiente comando según tu sistema:

   **Windows:**
   gradlew lwjgl3:run

   **macOS / Linux:**
   ./gradlew lwjgl3:run


## Exportar ejecutable (.exe)
Para generar un ejecutable de Windows:
    gradlew build
    gradlew lwjgl3:jar

El archivo .jar o .exe se generará en:
    lwjgl3/build/libs/

También puedes usar el bloque construct en build.gradle para compilar versiones nativas para Windows, macOS o Linux.

## Autores
- Simon Martinez
- Samuel Astudillo
- Martina Valenzuela