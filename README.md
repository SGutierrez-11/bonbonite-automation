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
| `web-automation` | Capa web: driver, pages, components, tasks, questions, step definitions y features. Depende de `api-automation`. |

La dirección de la dependencia es de una sola vía: `api-automation` nunca conoce a
`web-automation`. Cuando exista un tercer módulo consumidor (por ejemplo mobile), la
infraestructura compartida se extrae a un módulo `core-automation`.

## Requisitos

- JDK 17, con `JAVA_HOME` apuntando a esa instalación
- Google Chrome (navegador por defecto)

No es necesario instalar Maven: el proyecto incluye el Maven Wrapper, que descarga
la versión declarada en `.mvn/wrapper/maven-wrapper.properties` en la primera
ejecución.

## Ejecución

```bash
./mvnw clean test
```

En Windows:

```
mvnw.cmd clean test
```

## Estado

En construcción.
