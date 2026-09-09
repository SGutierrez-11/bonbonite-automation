## Qué incluye

<!-- Lista corta de lo que entra con este cambio. -->

## Por qué

<!-- La decisión de diseño detrás del cambio, o el defecto que corrige.
     Si es evidente, se puede omitir. -->

## Cómo se verificó

<!-- El comando exacto que se ejecutó y el resultado. -->

```bash
./mvnw clean test -pl runner -am -Dcucumber.filter.tags="@smoke"
```

## Revisión

- [ ] El proyecto compila (`./mvnw clean install -DskipTests`)
- [ ] La suite pasa en local
- [ ] Las pages solo contienen localizadores
- [ ] No hay esperas fijas ni datos quemados
- [ ] Clases y métodos públicos documentados con Javadoc
- [ ] No se versionaron credenciales ni resultados de ejecución
