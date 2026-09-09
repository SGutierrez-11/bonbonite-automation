# Bonbonite Automation

Framework de automatización de pruebas para el sitio e-commerce de Bon-bonite
(`https://www.bon-bonite.com/`).

## Stack

Java 17 · Maven · Selenium 4 · Cucumber 7 · TestNG 7 · REST Assured · Allure

## Arquitectura

Monorepo Maven multi-módulo con patrón híbrido POM + Screenplay:

- Las **pages** contienen únicamente localizadores, resueltos con Page Factory.
- Los **tasks** ejecutan las acciones de negocio y los **questions** las validaciones.
  Ambos heredan de su page para acceder a los localizadores.
- Los **step definitions** solo orquestan tasks y questions; no tocan el DOM.

### Módulos

| Módulo | Responsabilidad |
|---|---|
| `api-automation` | Base del framework (configuración, contexto, cliente REST) y capa de servicios de API. |
| `web-automation` | Capa web: driver, pages, components, tasks, questions y step definitions. Depende de `api-automation`. |
| `runner` | Ejecución: features, suites de TestNG, configuración por ambiente y punto de entrada. Depende de los dos anteriores. |

La dirección de la dependencia es de una sola vía y nunca se invierte: `api-automation`
no conoce a `web-automation`, y ningún módulo de código conoce al `runner`.

Los módulos de código son bibliotecas puras: producen un jar reutilizable y no
arrastran el motor de pruebas. Toda la ejecución sale del `runner`, que es el único
que puede depender de todos y por tanto el único donde cabe un escenario que combine
varias capas.

Cuando exista un tercer módulo de código (por ejemplo mobile), la infraestructura
compartida se extrae a un módulo `core-automation`.

## Requisitos

- JDK 17, con `JAVA_HOME` apuntando a esa instalación
- Google Chrome (navegador por defecto)

No es necesario instalar Maven: el proyecto incluye el Maven Wrapper, que descarga
la versión declarada en `.mvn/wrapper/maven-wrapper.properties` en la primera
ejecución.

## Ejecución

Toda ejecución sale del módulo `runner`. `-pl runner` lo selecciona y `-am` construye
antes los módulos de los que depende.

```bash
./mvnw clean test -pl runner -am
```

En Windows:

```
mvnw.cmd clean test -pl runner -am
```

Opciones frecuentes:

| Objetivo | Parámetro |
|---|---|
| Otro navegador | `-Dbrowser.name=edge` (chrome, edge, firefox) |
| Sin ventana | `-Dheadless.mode=true` |
| Filtrar por etiqueta | `-Dcucumber.filter.tags="@smoke"` |
| Ejecución secuencial | `-DthreadCount=1` |
| Contra Selenium Grid | `-Dselenium.grid=true` |

Reporte:

```bash
./mvnw allure:serve -pl runner
```

## Estado

En construcción.
