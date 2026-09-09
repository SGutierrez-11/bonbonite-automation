package com.bonbonite.qa.web.pages;

import com.bonbonite.qa.web.driver.Browser;
import io.qameta.allure.Step;
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
   * Opens an address in the browser.
   *
   * @param url address to open
   */
  @Step("Abrir la página {url}")
  public void openPage(String url) {
    log.info("Opening {}", url);
    getDriver().get(url);
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
