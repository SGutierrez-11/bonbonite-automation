# Bonbonite Automation

[![Pruebas automatizadas](https://github.com/SGutierrez-11/bonbonite-automation/actions/workflows/tests.yml/badge.svg)](https://github.com/SGutierrez-11/bonbonite-automation/actions/workflows/tests.yml)

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

## Ejecución sobre Selenium Grid

El proyecto incluye un Grid en `docker/docker-compose.yml` con nodos de Chrome,
Firefox y Edge. Permite ejecutar en varios navegadores y en paralelo sin
instalarlos en la máquina.

```bash
docker compose -f docker/docker-compose.yml up -d
```

Con el Grid arriba, la suite se apunta a él por parámetro:

```bash
./mvnw clean test -pl runner -am -Dselenium.grid=true
```

La consola del Grid queda en `http://localhost:4444`. Para ver un navegador en
vivo hay VNC por navegador en `http://localhost:7900` (Chrome), `7901` (Firefox)
y `7902` (Edge), con la contraseña `secret`.

Dos perfiles opcionales:

```bash
docker compose -f docker/docker-compose.yml --profile video up -d
```

```bash
docker compose -f docker/docker-compose.yml --profile suite up --build --abort-on-container-exit
```

El primero graba un video por sesión en `runner/target/videos`. El segundo ejecuta
la suite completa dentro de un contenedor, sin necesidad de tener Java ni Maven
instalados.

Para bajar todo:

```bash
docker compose -f docker/docker-compose.yml --profile video --profile suite down -v
```

## Integración continua

El workflow `.github/workflows/tests.yml` se ejecuta en cada push a `main` y en cada
pull request, con el navegador en modo sin interfaz y dos hilos. Publica como
artefactos los resultados de Allure y el reporte de Cucumber, con `if: always()`
para que también queden disponibles cuando la suite falla — que es justo cuando más
se necesitan.

**En automático solo corre el subconjunto `@smoke`.** El sitio bajo prueba es el de
producción de un tercero y no corresponde generarle tráfico en cada commit. La
regresión completa se lanza a demanda desde la pestaña *Actions*, eligiendo
etiquetas y navegador.

Las credenciales del usuario de pruebas se inyectan desde *Settings → Secrets and
variables → Actions* como `CUSTOMER_DOCUMENT` y `CUSTOMER_PASSWORD`. No hay ningún
dato sensible en el repositorio.

## Configuración sensible

El proyecto nunca versiona credenciales. Las llaves existen vacías en
`runner/src/test/resources/config/web.prod.properties` a modo de documentación, y el
valor se entrega por variable de entorno:

```bash
setx CUSTOMER_DOCUMENT "<numero de cedula>"
```

```bash
setx CUSTOMER_PASSWORD "<contrasena>"
```

El `PropertiesManager` resuelve cada parámetro buscando primero en variables de
entorno, luego en parámetros de ejecución y por último en el archivo, de modo que la
misma suite corre en local, en Docker y en integración continua sin cambiar código.

### Por qué Grid y no otra opción

Se evaluaron tres caminos: un navegador instalado en la máquina, Selenium Grid en
contenedores y Testcontainers. El primero no permite multi-navegador sin instalar
cada uno; el tercero es más elegante pero añade complejidad que este alcance no
justifica. El Grid en `docker-compose` da paralelismo real y los tres navegadores
sin instalar nada, y es el mismo mecanismo que se usaría en un pipeline serio.

Las imágenes van con versión fija, no `latest`: un navegador que cambia de versión
sin aviso rompe la suite sin que nadie haya tocado el código.

## Estado

En construcción.
