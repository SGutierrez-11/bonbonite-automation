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
 * Factory of WebDriver instances.
 *
 * <p>It concentrates in one place the decision of which browser is started and with
 * which options, so that supporting a new browser means adding a branch instead of
 * touching the tests. No external driver manager is used: since Selenium 4.6,
 * Selenium Manager downloads the driver matching the installed browser version.</p>
 */
@Slf4j
@UtilityClass
public class Browser {

  /**
   * User agent of a regular desktop Chrome.
   *
   * <p>In headless mode Chrome announces itself as {@code HeadlessChrome}, which the
   * firewall of the site under test reads as a bot and answers with a block. Sending
   * the same string a real browser sends removes that false rejection; it does not
   * change how the page behaves.</p>
   */
  private static final String DESKTOP_USER_AGENT =
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
      + "(KHTML, like Gecko) Chrome/152.0.0.0 Safari/537.36";

  /** Seconds allowed for a page to finish loading before the driver gives up. */
  private static final int PAGE_LOAD_TIMEOUT = 90;

  private static final String CHROME = "chrome";
  private static final String EDGE = "edge";
  private static final String FIREFOX = "firefox";
  private static final String SAFARI = "safari";

  /**
   * Creates the driver of the configured browser, local or remote as needed.
   *
   * @return a driver ready to use, with the implicit wait already applied
   * @throws CustomException if the configured browser is not supported or the
   *                         Selenium Grid address is not valid
   */
  public static WebDriver createWebDriver() {
    String browserName = BROWSER_NAME.toLowerCase().trim();
    log.info("Creating driver for '{}' (grid: {}, headless: {})",
      browserName, SELENIUM_GRID, HEADLESS_MODE);

    WebDriver driver = SELENIUM_GRID ? remoteDriver(browserName) : localDriver(browserName);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(WAIT_IMPLICIT));
    driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(PAGE_LOAD_TIMEOUT));
    applyWindowSize(driver);
    return driver;
  }

  /**
   * Sets the window size through the driver API.
   *
   * <p>The browser command line argument is not always honoured, and the site under
   * test renders different headers depending on the available width: if the window
   * ends up below the breakpoint, the desktop locators point at hidden elements.
   * Setting the size here guarantees every browser starts at the same resolution.</p>
   *
   * @param driver freshly created driver
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
    options.addArguments("--user-agent=" + DESKTOP_USER_AGENT);
    options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
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
    options.addArguments("--user-agent=" + DESKTOP_USER_AGENT);
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
      "--disable-blink-features=AutomationControlled",
      "--remote-allow-origins=*"));

    if (HEADLESS_MODE) {
      arguments.addAll(containerArguments());
    }
    return arguments;
  }

  /**
   * Arguments required to run inside a container.
   *
   * <p>Without them Chrome fails intermittently on Docker and on continuous
   * integration runners: {@code --no-sandbox} because the process runs as root
   * without the privileges the sandbox requires, and {@code --disable-dev-shm-usage}
   * because the default shared memory size triggers the
   * {@code DevToolsActivePort file doesn't exist} error. They are applied together
   * with headless mode, which is the one used in those environments.</p>
   *
   * @return the container compatibility arguments
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
