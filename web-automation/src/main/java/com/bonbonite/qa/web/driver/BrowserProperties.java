package com.bonbonite.qa.web.driver;

import static com.bonbonite.qa.api.config.PropertiesManager.getParameter;

import lombok.experimental.UtilityClass;

/**
 * Parámetros de configuración del navegador.
 *
 * <p>Se resuelven una sola vez al cargar la clase, con la precedencia definida en
 * el gestor de propiedades: variable de entorno, parámetro de ejecución y, por
 * último, archivo de configuración. Cambiar de navegador o activar el modo sin
 * interfaz es entonces un parámetro en la línea de comandos, sin tocar código.</p>
 */
@UtilityClass
public class BrowserProperties {

  private static final String WEB_MODULE = "web";

  /** Navegador con el que se ejecutan los escenarios. */
  public static final String BROWSER_NAME = getParameter("browser.name");

  /** Indica si el navegador se ejecuta sin interfaz gráfica. */
  public static final boolean HEADLESS_MODE = Boolean.parseBoolean(getParameter("headless.mode"));

  /** Indica si el navegador se abre en una sesión privada, sin cookies previas. */
  public static final boolean PRIVATE_MODE = Boolean.parseBoolean(getParameter("private.mode"));

  /** Dimensiones de la ventana, en formato ancho,alto. */
  public static final String WINDOW_SIZE = getParameter("window.size");

  /** Segundos de espera implícita aplicados al driver. */
  public static final int WAIT_IMPLICIT = Integer.parseInt(getParameter("wait.implicit"));

  /** Segundos de espera explícita usados por defecto en las esperas por condición. */
  public static final int WAIT_EXPLICIT = Integer.parseInt(getParameter("wait.explicit"));

  /** Indica si la ejecución se delega a un Selenium Grid remoto. */
  public static final boolean SELENIUM_GRID = Boolean.parseBoolean(getParameter("selenium.grid"));

  /** Dirección del concentrador de Selenium Grid. */
  public static final String SELENIUM_GRID_URL = getParameter("selenium.grid.url");

  /** Indica si se adjunta una captura en cada paso, no solo en los fallos. */
  public static final boolean SCREENSHOT_ON_STEP =
    Boolean.parseBoolean(getParameter("screenshot.on.step"));

  /** Dirección principal del sitio bajo prueba. */
  public static final String BASE_URL = getParameter("base.url", WEB_MODULE);

  /** Dirección de la pantalla de cuenta. */
  public static final String ACCOUNT_URL = getParameter("account.url", WEB_MODULE);

  /** Dirección del carrito. */
  public static final String CART_URL = getParameter("cart.url", WEB_MODULE);
}
