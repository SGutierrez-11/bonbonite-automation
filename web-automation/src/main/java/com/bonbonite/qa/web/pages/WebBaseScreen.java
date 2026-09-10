package com.bonbonite.qa.web.pages;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.web.driver.Browser;
import io.qameta.allure.Step;
import java.util.List;
import java.util.Locale;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Base class of every screen of the site.
 *
 * <p>It serves two purposes. The first is owning the WebDriver: it keeps it in a
 * thread local variable, which allows several scenarios to run in parallel without
 * sharing a browser. The second is initializing the elements annotated with
 * {@code FindBy} when the screen is constructed, so that subclasses only declare
 * locators.</p>
 */
@Slf4j
@Getter
public abstract class WebBaseScreen {

  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  /** Titles the site serves when it refuses to deliver the requested page. */
  private static final List<String> BLOCKED_PAGE_SIGNALS =
    List.of("403 forbidden", "429 too many requests", "access denied",
      "attention required", "502 bad gateway", "503 service", "504 gateway");

  /**
   * Returns the driver bound to the running thread.
   *
   * @return the current thread driver, or {@code null} if it has not been created yet
   */
  public static WebDriver getDriver() {
    return DRIVER.get();
  }

  /**
   * Binds a driver to the running thread.
   *
   * @param driver driver to bind
   */
  public static void setDriver(WebDriver driver) {
    DRIVER.set(driver);
  }

  /**
   * Quits the browser of the current thread and releases the reference.
   */
  public static void removeDriver() {
    WebDriver driver = DRIVER.get();
    if (driver != null) {
      driver.quit();
      DRIVER.remove();
      log.debug("Driver quit and released from the current thread");
    }
  }

  /**
   * Initializes the screen locators through Page Factory.
   *
   * <p>When the thread has no driver yet, one is created, so a screen can be
   * instantiated without depending on the order of the hooks.</p>
   */
  protected WebBaseScreen() {
    if (getDriver() == null) {
      setDriver(Browser.createWebDriver());
    }
    PageFactory.initElements(getDriver(), this);
  }

  /**
   * Opens an address in the browser and checks that the site actually served the
   * page.
   *
   * @param url address to open
   * @throws CustomException if the site answered with a block or an error page
   */
  @Step("Abrir la página {url}")
  public void openPage(String url) {
    log.info("Opening {}", url);
    getDriver().get(url);
    requireServedPage(url);
  }

  /**
   * Fails fast and clearly when the site answered with a block or an error page
   * instead of the expected content.
   *
   * <p>The site under test sits behind a firewall that rejects traffic it reads as
   * automated, and the answer is a bare {@code 403 Forbidden} document. Without this
   * check every later step fails on a missing element, and the report blames the
   * product for what is an infrastructure problem. Raising the framework exception
   * marks the scenario as broken instead of failed, which is the honest
   * classification, and lets the retry policy give it a second chance.</p>
   *
   * @param url address that was requested
   * @throws CustomException if the served document is a block or an error page
   */
  private void requireServedPage(String url) {
    String title = getDriver().getTitle();
    if (title == null) {
      return;
    }
    String normalized = title.trim().toLowerCase(Locale.ROOT);
    for (String signal : BLOCKED_PAGE_SIGNALS) {
      if (normalized.contains(signal)) {
        throw new CustomException(String.format(
          "El sitio no entregó la página solicitada: respondió '%s' en %s. "
            + "Suele ser el cortafuegos bloqueando el tráfico automatizado; "
            + "ejecuta en secuencial y espera unos minutos.", title.trim(), url));
      }
    }
  }

  /**
   * Reloads the current page.
   */
  @Step("Recargar la página")
  public void refreshPage() {
    getDriver().navigate().refresh();
  }

  /**
   * Returns the address currently displayed by the browser.
   *
   * @return the current URL
   */
  public String getCurrentUrl() {
    return getDriver().getCurrentUrl();
  }
}
