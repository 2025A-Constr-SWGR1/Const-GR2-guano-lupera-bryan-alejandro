# Flujo de Calidad de Código - Proyecto U-Food

## Resumen del Proyecto

U-Food es una aplicación web desarrollada con Java EE que permite a los usuarios buscar, comparar y calificar restaurantes, así como planificar visitas y recibir notificaciones. [Aquí](README.md) para más detalles.

## Flujo de Calidad de Código con GitHub Actions

Hemos implementado un flujo de integración continua utilizando GitHub Actions y Actions Runners que permite automatizar la validación de calidad del código. El flujo está definido en el archivo [`.github/workflows/build.yml`](.github/workflows/build.yml).

### 1. Estructura del Workflow

#### 1.1. Configuración del Entorno

```yaml
steps:
- name: Descargar el repositorio
  uses: actions/checkout@v4
- name: Configurar JDK 17
  uses: actions/setup-java@v4
  with:
    java-version: '17'
    distribution: 'temurin'
    cache: maven
```

#### 1.2. Compilación

```yaml
- name: Construir con Maven
  run: mvn -B compile --file pom.xml
```

#### 1.3. Análisis Estático (Linting)

El workflow incluye dos herramientas para análisis estático:

**PMD**: Analiza el código fuente en busca de problemas potenciales.
```yaml
- name: Correr tests de calidad con PMD
  run: mvn -B pmd:check --file pom.xml
  continue-on-error: true
```

Resultado del linting de PMD arrojó 17 violaciones, agrupadas en 7 tipos de problemas:
1. Unused Imports (Importaciones no utilizadas)
2. Unused Private Fields (Campos privados no utilizados)
3. Collapsible If Statements (Ifs anidados que pueden combinarse)
4. Unused Private Methods (Métodos privados no utilizados)
5. Useless Parentheses (Paréntesis innecesarios)
6. Unused Formal Parameter (Parámetros no utilizados)
7. Empty Catch Block (Bloques catch vacíos)

Para más detalles de las violaciones encontradas por PMD [click aquí](docs/pmd.xml)

#### 2.4. Pruebas Unitarias y de Integración

Ejecución de todas las pruebas JUnit:

```yaml
- name: Correr unit tests
  run: mvn -B test --file pom.xml
```

El resultado de la ejecución de los tests unitarios se resume de la siguiente manera:

| Test Class | Tests Run | Failures | Errors | Skipped | Time Elapsed (s) |
|---------------------------------------------|-----------|----------|--------|---------|------------------|
| servicios.NotificacionServiceParametrizedTest | 2         | 0        | 0      | 0       | 0.085            |
| servicios.PlanificacionRestauranteParametrizedTest | 3         | 0        | 0      | 0       | 0.014            |
| servicios.PlanificacionServiceTest           | 9         | 0        | 0      | 0       | 0.658            |
| servicios.PlanificacionSugerirParametrizedTest | 5         | 0        | 0      | 0       | 0.011            |
| servicios.PreferenciaMatchingTest            | 1         | 0        | 0      | 0       | 0.001            |
| servicios.RecomendacionesServiceTest         | 1         | 0        | 0      | 0       | 0.004            |
| servicios.RecomendacionServiceParametrizedTest | 1         | 0        | 0      | 0       | 0.026            |
| **TOTAL**                                   | **22**    | **0**    | **0**  | **0**   | **6.049**        |
#### 2.5. Empaquetado

Generación del archivo WAR para despliegue:

```yaml
- name: Package application
  run: mvn -B package --file pom.xml -DskipTests
```

El archivo generado se guarda como artefacto para su posterior uso:

```yaml
- name: Upload WAR artifact
  uses: actions/upload-artifact@v4
  with:
    name: webapp
    path: target/ROOT.war
```

#### 2.6. Construcción de Imagen Docker

El workflow verifica el estado de Docker Desktop y construye la imagen:

```yaml
- name: Contruir la imagen de Docker
  run: |
    docker build -t u-food .
    docker tag u-food bryang02/u-food:latest
```
