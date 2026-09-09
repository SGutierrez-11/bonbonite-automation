# Imagen que ejecuta la suite contra el Selenium Grid del compose.
# No instala navegadores: los aporta el Grid, y por eso la imagen se mantiene liviana.

# Primera etapa: resuelve dependencias con solo los pom.xml. Como el codigo de
# pruebas cambia mucho mas seguido que las dependencias, esta capa queda cacheada
# y las reconstrucciones posteriores no vuelven a descargar Maven Central.
FROM maven:3.9-eclipse-temurin-17 AS deps
WORKDIR /app
COPY pom.xml ./
COPY api-automation/pom.xml api-automation/
COPY web-automation/pom.xml web-automation/
COPY runner/pom.xml runner/
RUN mvn -B dependency:go-offline -DskipTests || true

FROM maven:3.9-eclipse-temurin-17
WORKDIR /app
COPY --from=deps /root/.m2 /root/.m2
COPY . .

ENTRYPOINT ["mvn", "clean", "test", "-pl", "runner", "-am"]
CMD ["-Dselenium.grid=true", "-Dselenium.grid.url=http://selenium-hub:4444"]
