package com.bonbonite.qa.web.driver;

import static com.bonbonite.qa.api.config.PropertiesManager.getParameter;

import lombok.experimental.UtilityClass;

/**
 * Browser configuration parameters.
 *
 * <p>They are resolved once when the class is loaded, with the precedence defined in
 * the properties manager: environment variable, execution parameter and finally the
 * configuration file. Switching browser or enabling headless mode is therefore a
 * command line parameter, with no code change.</p>
 */
@UtilityClass
public class BrowserProperties {

  private static final String WEB_MODULE = "web";

  /** Browser the scenarios are executed with. */
  public static final String BROWSER_NAME = getParameter("browser.name");

  /** Whether the browser runs without a graphical interface. */
  public static final boolean HEADLESS_MODE = Boolean.parseBoolean(getParameter("headless.mode"));

  /** Whether the browser opens a private session, with no previous cookies. */
  public static final boolean PRIVATE_MODE = Boolean.parseBoolean(getParameter("private.mode"));

  /** Window dimensions, in width,height format. */
  public static final String WINDOW_SIZE = getParameter("window.size");

  /** Seconds of implicit wait applied to the driver. */
  public static final int WAIT_IMPLICIT = Integer.parseInt(getParameter("wait.implicit"));

  /** Default seconds used by the explicit waits. */
  public static final int WAIT_EXPLICIT = Integer.parseInt(getParameter("wait.explicit"));

  /** Whether execution is delegated to a remote Selenium Grid. */
  public static final boolean SELENIUM_GRID = Boolean.parseBoolean(getParameter("selenium.grid"));

  /** Address of the Selenium Grid hub. */
  public static final String SELENIUM_GRID_URL = getParameter("selenium.grid.url");

  /** Whether a screenshot is attached on every step, not only on failures. */
  public static final boolean SCREENSHOT_ON_STEP =
    Boolean.parseBoolean(getParameter("screenshot.on.step"));

  /** Main address of the site under test. */
  public static final String BASE_URL = getParameter("base.url", WEB_MODULE);

  /** Address of the account screen. */
  public static final String ACCOUNT_URL = getParameter("account.url", WEB_MODULE);

  /** Address of the cart. */
  public static final String CART_URL = getParameter("cart.url", WEB_MODULE);
}
