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

## Reporte de ejecución publicado

El reporte de Allure de la última ejecución sobre `main` se publica automáticamente
en GitHub Pages:

**https://sgutierrez-11.github.io/bonbonite-automation**

No hace falta clonar ni ejecutar nada para revisar la evidencia: el reporte incluye
los pasos de cada escenario, las capturas de los fallos, el HTML de la página en el
momento del error, las trazas de cada llamada a la API y la gráfica de tendencia
entre ejecuciones.

## Limitación conocida: el sitio bloquea el tráfico automatizado

El sitio bajo prueba está en **producción** y detrás de un cortafuegos que rechaza el
tráfico que interpreta como automatizado. Tras unos minutos de ejecución continua
empieza a responder **403 Forbidden**, incluso a peticiones simples desde el mismo
equipo. El bloqueo se levanta solo al cabo de un rato.

**No es un defecto del framework.** Es la infraestructura del cliente defendiéndose, y
es el argumento más fuerte a favor de la recomendación principal del informe: habilitar
un ambiente de pruebas separado.

### Qué hace el proyecto al respecto

| Medida | Efecto |
|---|---|
| Detección explícita del bloqueo | `WebBaseScreen` verifica la página servida y lanza la excepción del framework al detectar un 403. El escenario se marca como **roto**, no como fallido, así el reporte no le achaca a Bon-bonite un problema de infraestructura. |
| Reintento acotado | Un escenario se reintenta **una vez y solo si el fallo es de entorno**. Una aserción que no se cumple nunca se reintenta: sería enmascarar un defecto real. |
| Espaciado entre escenarios | `scenario.pacing.seconds` introduce una pausa antes de cada escenario para no superar el límite de peticiones. Es la única pausa fija del proyecto y no es una espera de interfaz. |
| Agente de usuario real | En modo headless Chrome se anuncia como `HeadlessChrome`, lo que dispara el bloqueo. Se envía el mismo agente que manda un navegador de escritorio. |
| Ejecución secuencial por defecto | El paralelismo fue lo que disparó el bloqueo la primera vez. `threadCount` viene en 1 y se sube solo cuando exista un ambiente que lo tolere. |

### Cómo ejecutar cuando el sitio está bloqueando

```bash
mvnw.cmd clean test -pl runner -am -DthreadCount=1 "-Dcucumber.filter.tags=@smoke"
```

Un subconjunto pequeño y espaciado tiene muchas más probabilidades de terminar en
verde. Si aun así falla, abre el reporte de Allure y revisa el HTML adjunto: si dice
*403 Forbidden*, hay que esperar, no hay que tocar el código.

Para subir la pausa entre escenarios en una corrida puntual:

```bash
mvnw.cmd clean test -pl runner -am -Dscenario.pacing.seconds=15
```

### La solución de fondo

Que el cliente permita las direcciones desde las que se ejecuta la suite, o que
habilite un ambiente de pruebas. Mientras se ejecute contra producción, la
disponibilidad del sitio para pruebas depende de un cortafuegos que no controlamos.

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

Tras cada ejecución sobre `main`, un segundo job genera el reporte de Allure y lo
despliega en GitHub Pages con las acciones oficiales de GitHub, sin depender de
acciones de terceros. Antes de generarlo recupera el historial del reporte anterior
desde el sitio ya publicado, que es lo que alimenta la gráfica de tendencia; en la
primera ejecución ese historial no existe todavía y el reporte se genera igual.

## Configuración sensible

El proyecto nunca versiona credenciales. Las llaves existen vacías en
`runner/src/test/resources/config/web.prod.properties` a modo de documentación, y el
valor se entrega por variable de entorno:

En local, lo más cómodo es un archivo `.env` en la raíz del proyecto. Está ignorado
por Git y nunca se versiona:

```
CUSTOMER_DOCUMENT=<numero de cedula>
CUSTOMER_PASSWORD=<contrasena>
```

También se pueden definir como variables de entorno del sistema, que es lo que hace
la integración continua a partir de los *secrets*:

```bash
setx CUSTOMER_DOCUMENT "<numero de cedula>"
```

El `PropertiesManager` resuelve cada parámetro en este orden: variable de entorno,
parámetro de ejecución `-D`, archivo `.env` de la raíz y, por último, el archivo de
configuración. Gracias a esa precedencia la misma suite corre en local, en Docker y en
integración continua sin cambiar una línea de código, y el `.env` no interfiere en CI
porque allí las credenciales llegan como variables de entorno reales.

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
