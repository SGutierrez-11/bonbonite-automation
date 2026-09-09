package com.bonbonite.qa.api.config;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Loads and resolves the configuration parameters of the framework.
 *
 * <p>A parameter is looked up in three sources, in this order: environment variable,
 * system property and properties file. That precedence lets the same execution work
 * locally reading the files and on continuous integration receiving credentials and
 * URLs as environment variables, without touching a single line of code.</p>
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
   * Creates the manager loading the given file.
   *
   * @param propertiesFileName file name inside the configuration folder
   */
  public PropertiesManager(String propertiesFileName) {
    this.propertiesFileName = propertiesFileName;
    loadProperties();
  }

  /**
   * Returns the manager for the properties common to every environment.
   *
   * @return a manager over {@code common.properties}
   */
  public static PropertiesManager getInstance() {
    return new PropertiesManager(COMMON_PROPERTIES);
  }

  /**
   * Returns the manager for a module's properties on the active environment.
   *
   * <p>The environment is taken from the {@code environment} system property and the
   * resulting file follows the {@code module.environment.properties} format.</p>
   *
   * @param moduleName file prefix, for instance {@code web} or {@code api}
   * @return a manager over the file of that module and environment
   */
  public static PropertiesManager getInstance(String moduleName) {
    String environment = Environments.byName(System.getProperty("environment")).getName();
    return new PropertiesManager(moduleName + "." + environment + PROPERTIES_EXTENSION);
  }

  /**
   * Resolves a parameter looking it up in environment variables, system properties
   * and finally the common properties file.
   *
   * @param key parameter name
   * @return the parameter value
   * @throws CustomException if the parameter is absent from all three sources
   */
  public static String getParameter(String key) {
    return resolve(key, () -> getInstance().getProperty(key));
  }

  /**
   * Resolves a module specific parameter with the same precedence as
   * {@link #getParameter(String)} but reading that module's file.
   *
   * @param key        parameter name
   * @param moduleName prefix of the module properties file
   * @return the parameter value
   * @throws CustomException if the parameter is absent from all three sources
   */
  public static String getParameter(String key, String moduleName) {
    return resolve(key, () -> getInstance(moduleName).getProperty(key));
  }

  /**
   * Returns the value of a key inside the file loaded by this instance.
   *
   * @param key property name
   * @return the associated value, or {@code null} when the key is absent
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
      log.debug("Properties loaded from {}", propertiesFileName);
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
