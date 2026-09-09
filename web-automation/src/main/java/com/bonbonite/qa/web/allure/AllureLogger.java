package com.bonbonite.qa.web.allure;

import static com.bonbonite.qa.web.driver.BrowserProperties.BASE_URL;
import static com.bonbonite.qa.web.driver.BrowserProperties.BROWSER_NAME;
import static com.bonbonite.qa.web.driver.BrowserProperties.HEADLESS_MODE;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Adjunta evidencias al reporte de Allure.
 *
 * <p>Ningún método propaga excepciones: si la captura falla —porque el navegador ya
 * se cerró o la sesión se perdió— se registra el problema y la ejecución continúa.
 * Un fallo al recolectar evidencia nunca debe enmascarar el fallo real que se estaba
 * documentando.</p>
 */
@Slf4j
@UtilityClass
public class AllureLogger {

  private static final String RESULTS_DIRECTORY = "target/allure-results";

  /**
   * Adjunta una captura de la pantalla actual.
   *
   * @param name nombre con el que aparece el adjunto en el reporte
   */
  public static void attachScreenshot(String name) {
    WebDriver driver = WebBaseScreen.getDriver();
    if (driver == null) {
      return;
    }
    try {
      byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      Allure.getLifecycle().addAttachment(name, "image/png", "png", screenshot);
    } catch (Exception exception) {
      log.warn("No fue posible capturar la pantalla: {}", exception.getMessage());
    }
  }

  /**
   * Adjunta el HTML de la página en el momento del fallo y la dirección actual.
   *
   * <p>Sirve para diagnosticar un localizador que dejó de encontrar su elemento sin
   * tener que reproducir la ejecución.</p>
   */
  public static void attachPageSource() {
    WebDriver driver = WebBaseScreen.getDriver();
    if (driver == null) {
      return;
    }
    try {
      Allure.addAttachment("URL al momento del fallo", driver.getCurrentUrl());
      Allure.addAttachment("HTML de la página", "text/html",
        new ByteArrayInputStream(driver.getPageSource().getBytes(StandardCharsets.UTF_8)), ".html");
    } catch (Exception exception) {
      log.warn("No fue posible adjuntar el estado de la página: {}", exception.getMessage());
    }
  }

  /**
   * Escribe el archivo de entorno que Allure muestra en la portada del reporte.
   *
   * <p>Es lo primero que consulta quien revisa la evidencia para saber contra qué
   * navegador y qué ambiente se ejecutó la suite.</p>
   */
  public static void writeEnvironmentInfo() {
    Map<String, String> environment = new LinkedHashMap<>();
    environment.put("Navegador", BROWSER_NAME);
    environment.put("Modo headless", String.valueOf(HEADLESS_MODE));
    environment.put("URL base", BASE_URL);
    environment.put("Sistema operativo",
      System.getProperty("os.name") + " " + System.getProperty("os.version"));
    environment.put("Java", System.getProperty("java.version"));

    StringBuilder content = new StringBuilder();
    environment.forEach((key, value) -> content.append(key).append('=').append(value).append('\n'));

    try {
      Path directory = Path.of(RESULTS_DIRECTORY);
      Files.createDirectories(directory);
      Files.writeString(directory.resolve("environment.properties"), content.toString());
    } catch (IOException exception) {
      log.warn("No fue posible escribir el entorno del reporte: {}", exception.getMessage());
    }
  }
}
