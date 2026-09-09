package com.bonbonite.qa.web.driver;

import static com.bonbonite.qa.web.driver.BrowserProperties.BROWSER_NAME;
import static com.bonbonite.qa.web.driver.BrowserProperties.HEADLESS_MODE;
import static com.bonbonite.qa.web.driver.BrowserProperties.PRIVATE_MODE;
import static com.bonbonite.qa.web.driver.BrowserProperties.SELENIUM_GRID;
import static com.bonbonite.qa.web.driver.BrowserProperties.SELENIUM_GRID_URL;
import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_IMPLICIT;
import static com.bonbonite.qa.web.driver.BrowserProperties.WINDOW_SIZE;

import com.bonbonite.qa.api.exceptions.CustomException;
import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

/**
 * Fábrica de instancias de WebDriver.
 *
 * <p>Concentra en un solo punto la decisión de qué navegador se levanta y con qué
 * opciones, de modo que agregar un navegador sea agregar una rama y no modificar
 * las pruebas. No se usa ningún gestor de binarios externo: desde Selenium 4.6 el
 * propio Selenium Manager descarga el driver que corresponde a la versión del
 * navegador instalado.</p>
 */
@Slf4j
@UtilityClass
public class Browser {

  private static final String CHROME = "chrome";
  private static final String EDGE = "edge";
  private static final String FIREFOX = "firefox";
  private static final String SAFARI = "safari";

  /**
   * Crea el driver del navegador configurado, local o remoto según corresponda.
   *
   * @return driver listo para usarse, con la espera implícita aplicada
   * @throws CustomException si el navegador configurado no está soportado o si la
   *                         dirección del Selenium Grid no es válida
   */
  public static WebDriver createWebDriver() {
    String browserName = BROWSER_NAME.toLowerCase().trim();
    log.info("Creando driver para '{}' (grid: {}, headless: {})",
      browserName, SELENIUM_GRID, HEADLESS_MODE);

    WebDriver driver = SELENIUM_GRID ? remoteDriver(browserName) : localDriver(browserName);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_IMPLICIT));
    applyWindowSize(driver);
    return driver;
  }

  /**
   * Fija el tamaño de la ventana mediante la API del driver.
   *
   * <p>El argumento de línea de comandos del navegador no siempre se respeta, y el
   * sitio bajo prueba renderiza encabezados distintos según el ancho disponible: si
   * la ventana queda por debajo del punto de quiebre, los localizadores de la vista
   * de escritorio apuntan a elementos ocultos. Fijar el tamaño aquí garantiza que
   * todos los navegadores arranquen con la misma resolución.</p>
   *
   * @param driver driver recién creado
   */
  private static void applyWindowSize(WebDriver driver) {
    driver.manage().window().setSize(
      new Dimension(Integer.parseInt(windowDimension(0)), Integer.parseInt(windowDimension(1))));
  }

  private static WebDriver localDriver(String browserName) {
    return switch (browserName) {
      case CHROME -> new ChromeDriver((ChromeOptions) buildOptions(browserName));
      case EDGE -> new EdgeDriver((EdgeOptions) buildOptions(browserName));
      case FIREFOX -> new FirefoxDriver((FirefoxOptions) buildOptions(browserName));
      case SAFARI -> new SafariDriver();
      default -> throw unsupportedBrowser(browserName);
    };
  }

  private static WebDriver remoteDriver(String browserName) {
    try {
      return new RemoteWebDriver(URI.create(SELENIUM_GRID_URL).toURL(), buildOptions(browserName));
    } catch (MalformedURLException | IllegalArgumentException exception) {
      throw new CustomException(
        "La dirección del Selenium Grid no es válida: " + SELENIUM_GRID_URL, exception);
    }
  }

  private static MutableCapabilities buildOptions(String browserName) {
    return switch (browserName) {
      case CHROME -> chromeOptions();
      case EDGE -> edgeOptions();
      case FIREFOX -> firefoxOptions();
      default -> throw unsupportedBrowser(browserName);
    };
  }

  private static ChromeOptions chromeOptions() {
    ChromeOptions options = new ChromeOptions();
    options.addArguments(commonArguments());
    if (HEADLESS_MODE) {
      options.addArguments("--headless=new");
    }
    if (PRIVATE_MODE) {
      options.addArguments("--incognito");
    }
    return options;
  }

  private static EdgeOptions edgeOptions() {
    EdgeOptions options = new EdgeOptions();
    options.addArguments(commonArguments());
    if (HEADLESS_MODE) {
      options.addArguments("--headless=new");
    }
    if (PRIVATE_MODE) {
      options.addArguments("--inprivate");
    }
    return options;
  }

  private static FirefoxOptions firefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("--width=" + windowDimension(0), "--height=" + windowDimension(1));
    if (HEADLESS_MODE) {
      options.addArguments("-headless");
    }
    if (PRIVATE_MODE) {
      options.addArguments("-private");
    }
    return options;
  }

  private static List<String> commonArguments() {
    List<String> arguments = new ArrayList<>(List.of(
      "--window-size=" + WINDOW_SIZE,
      "--disable-gpu",
      "--disable-extensions",
      "--disable-notifications",
      "--remote-allow-origins=*"));

    if (HEADLESS_MODE) {
      arguments.addAll(containerArguments());
    }
    return arguments;
  }

  /**
   * Argumentos necesarios para ejecutar dentro de un contenedor.
   *
   * <p>Sin ellos Chrome falla de forma intermitente en Docker y en los runners de
   * integración continua: {@code --no-sandbox} porque el proceso corre como root sin
   * los privilegios que exige el aislamiento, y {@code --disable-dev-shm-usage}
   * porque el tamaño por defecto de la memoria compartida provoca el error
   * {@code DevToolsActivePort file doesn't exist}. Se aplican junto al modo sin
   * interfaz, que es el que se usa en esos entornos.</p>
   *
   * @return argumentos de compatibilidad con contenedores
   */
  private static List<String> containerArguments() {
    return List.of("--no-sandbox", "--disable-dev-shm-usage");
  }

  private static String windowDimension(int index) {
    return WINDOW_SIZE.split(",")[index].trim();
  }

  private static CustomException unsupportedBrowser(String browserName) {
    return new CustomException(String.format(
      "El navegador '%s' no está soportado. Opciones válidas: %s, %s, %s, %s "
        + "(safari requiere macOS y no admite ejecución remota en este framework).",
      browserName, CHROME, EDGE, FIREFOX, SAFARI));
  }
}
