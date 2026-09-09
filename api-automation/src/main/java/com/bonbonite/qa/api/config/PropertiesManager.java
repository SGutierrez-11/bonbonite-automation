package com.bonbonite.qa.api.config;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Carga y resuelve los parámetros de configuración del framework.
 *
 * <p>Un parámetro se busca en tres fuentes, en este orden: variable de entorno,
 * propiedad de sistema y archivo de propiedades. Esa precedencia permite que la
 * misma ejecución funcione en local leyendo los archivos, y en integración
 * continua recibiendo credenciales y URLs por variable de entorno, sin cambiar
 * una línea de código.</p>
 */
@Slf4j
public class PropertiesManager {

  private static final String PROPERTIES_FOLDER = "config/";
  private static final String PROPERTIES_EXTENSION = ".properties";
  private static final String COMMON_PROPERTIES = "common.properties";

  @Getter
  private final Properties properties = new Properties();

  private final String propertiesFileName;

  /**
   * Crea el gestor cargando el archivo indicado.
   *
   * @param propertiesFileName nombre del archivo dentro de la carpeta de configuración
   */
  public PropertiesManager(String propertiesFileName) {
    this.propertiesFileName = propertiesFileName;
    loadProperties();
  }

  /**
   * Obtiene el gestor de las propiedades comunes a todos los entornos.
   *
   * @return gestor sobre {@code common.properties}
   */
  public static PropertiesManager getInstance() {
    return new PropertiesManager(COMMON_PROPERTIES);
  }

  /**
   * Obtiene el gestor de las propiedades de un módulo para el entorno activo.
   *
   * <p>El entorno se toma de la propiedad de sistema {@code environment} y el
   * archivo resultante sigue el formato {@code <modulo>.<entorno>.properties}.</p>
   *
   * @param moduleName prefijo del archivo, por ejemplo {@code web} o {@code api}
   * @return gestor sobre el archivo del módulo y entorno activos
   */
  public static PropertiesManager getInstance(String moduleName) {
    String environment = Environments.byName(System.getProperty("environment")).getName();
    return new PropertiesManager(moduleName + "." + environment + PROPERTIES_EXTENSION);
  }

  /**
   * Resuelve un parámetro buscándolo en variables de entorno, propiedades de
   * sistema y, finalmente, en las propiedades comunes.
   *
   * @param key nombre del parámetro
   * @return valor del parámetro
   * @throws CustomException si el parámetro no existe en ninguna de las tres fuentes
   */
  public static String getParameter(String key) {
    return resolve(key, () -> getInstance().getProperty(key));
  }

  /**
   * Resuelve un parámetro propio de un módulo, con la misma precedencia que
   * {@link #getParameter(String)} pero leyendo el archivo de ese módulo.
   *
   * @param key        nombre del parámetro
   * @param moduleName prefijo del archivo de propiedades del módulo
   * @return valor del parámetro
   * @throws CustomException si el parámetro no existe en ninguna de las tres fuentes
   */
  public static String getParameter(String key, String moduleName) {
    return resolve(key, () -> getInstance(moduleName).getProperty(key));
  }

  /**
   * Obtiene el valor de una clave dentro del archivo cargado por esta instancia.
   *
   * @param key nombre de la propiedad
   * @return valor asociado, o {@code null} si la clave no está presente
   */
  public String getProperty(String key) {
    return properties.getProperty(key.trim());
  }

  private static String resolve(String key, Supplier<String> fromFile) {
    String value = System.getenv(formatAsEnvironmentVariable(key));
    if (isBlank(value)) {
      value = System.getProperty(key);
    }
    if (isBlank(value)) {
      value = fromFile.get();
    }
    if (isBlank(value)) {
      throw new CustomException(buildMissingParameterMessage(key));
    }
    return value;
  }

  private void loadProperties() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try (InputStream input =
           classLoader.getResourceAsStream(PROPERTIES_FOLDER + propertiesFileName)) {
      if (input == null) {
        throw new CustomException(
          "No se encontró el archivo de propiedades: " + PROPERTIES_FOLDER + propertiesFileName);
      }
      properties.load(input);
      log.debug("Propiedades cargadas desde {}", propertiesFileName);
    } catch (IOException exception) {
      throw new CustomException(
        "No fue posible leer el archivo de propiedades: " + propertiesFileName, exception);
    }
  }

  private static String formatAsEnvironmentVariable(String key) {
    return key.toUpperCase().replace(".", "_");
  }

  private static boolean isBlank(String value) {
    return value == null || value.isBlank();
  }

  private static String buildMissingParameterMessage(String key) {
    return String.format(
      "No fue posible resolver el parámetro '%s'. Defínelo de alguna de estas formas:%n"
        + "  1. variable de entorno %s%n"
        + "  2. parámetro de ejecución -D%s=<valor>%n"
        + "  3. entrada en el archivo de configuración: %s=<valor>",
      key, formatAsEnvironmentVariable(key), key, key);
  }
}
