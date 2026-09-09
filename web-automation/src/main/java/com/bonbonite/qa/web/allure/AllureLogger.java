package com.bonbonite.qa.web.allure;

import static com.bonbonite.qa.web.driver.BrowserProperties.BASE_URL;
import static com.bonbonite.qa.web.driver.BrowserProperties.BROWSER_NAME;
import static com.bonbonite.qa.web.driver.BrowserProperties.HEADLESS_MODE;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Attaches evidence to the Allure report.
 *
 * <p>No method propagates exceptions: if capturing fails — because the browser was
 * already closed or the session was lost — the problem is logged and execution goes
 * on. A failure while collecting evidence must never mask the real failure it was
 * documenting.</p>
 */
@Slf4j
@UtilityClass
public class AllureLogger {

  private static final String RESULTS_DIRECTORY = "target/allure-results";

  /**
   * Attaches a screenshot of the current screen.
   *
   * @param name name the attachment gets in the report
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
      log.warn("Could not capture the screen: {}", exception.getMessage());
    }
  }

  /**
   * Attaches the page HTML at the moment of the failure together with the current
   * address.
   *
   * <p>It allows diagnosing a locator that stopped finding its element without
   * having to reproduce the execution.</p>
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
      log.warn("Could not attach the page state: {}", exception.getMessage());
    }
  }

  /**
   * Copies the failure categories definition into the results directory.
   *
   * <p>Allure only picks up {@code categories.json} when it sits next to the results,
   * not from the classpath. Copying it here keeps the classification working both
   * locally and on continuous integration, without duplicating the file.</p>
   */
  public static void copyCategories() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    try (InputStream categories = classLoader.getResourceAsStream("categories.json")) {
      if (categories == null) {
        log.warn("categories.json was not found on the classpath");
        return;
      }
      Path directory = Path.of(RESULTS_DIRECTORY);
      Files.createDirectories(directory);
      Files.copy(categories, directory.resolve("categories.json"),
        StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException exception) {
      log.warn("Could not copy the report categories: {}", exception.getMessage());
    }
  }

  /**
   * Writes the environment file Allure displays on the report cover.
   *
   * <p>It is the first thing whoever reviews the evidence looks at, to know which
   * browser and environment the suite ran against.</p>
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
      log.warn("Could not write the report environment file: {}", exception.getMessage());
    }
  }
}
