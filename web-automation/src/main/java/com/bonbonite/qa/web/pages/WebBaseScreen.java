package com.bonbonite.qa.web.pages;

import com.bonbonite.qa.web.driver.Browser;
import io.qameta.allure.Step;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/**
 * Clase base de todas las pantallas del sitio.
 *
 * <p>Cumple dos funciones. La primera es custodiar el WebDriver: lo mantiene en una
 * variable por hilo, lo que permite que varios escenarios se ejecuten en paralelo
 * sin compartir navegador. La segunda es inicializar los elementos anotados con
 * {@code FindBy} al construir la pantalla, de modo que las clases que heredan de
 * ella solo declaren localizadores.</p>
 */
@Slf4j
@Getter
public abstract class WebBaseScreen {

  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  /**
   * Devuelve el driver asociado al hilo en ejecución.
   *
   * @return driver del hilo actual, o {@code null} si todavía no se ha creado
   */
  public static WebDriver getDriver() {
    return DRIVER.get();
  }

  /**
   * Asocia un driver al hilo en ejecución.
   *
   * @param driver driver a asociar
   */
  public static void setDriver(WebDriver driver) {
    DRIVER.set(driver);
  }

  /**
   * Cierra el navegador del hilo actual y libera la referencia.
   */
  public static void removeDriver() {
    WebDriver driver = DRIVER.get();
    if (driver != null) {
      driver.quit();
      DRIVER.remove();
      log.debug("Driver cerrado y liberado del hilo actual");
    }
  }

  /**
   * Inicializa los localizadores de la pantalla mediante Page Factory.
   *
   * <p>Si el hilo aún no tiene driver se crea uno, de modo que una pantalla pueda
   * instanciarse sin depender del orden de los hooks.</p>
   */
  protected WebBaseScreen() {
    if (getDriver() == null) {
      setDriver(Browser.createWebDriver());
    }
    PageFactory.initElements(getDriver(), this);
  }

  /**
   * Abre una dirección en el navegador.
   *
   * @param url dirección a abrir
   */
  @Step("Abrir la página {url}")
  public void openPage(String url) {
    log.info("Abriendo {}", url);
    getDriver().get(url);
  }

  /**
   * Recarga la página actual.
   */
  @Step("Recargar la página")
  public void refreshPage() {
    getDriver().navigate().refresh();
  }

  /**
   * Devuelve la dirección que muestra el navegador en este momento.
   *
   * @return URL actual
   */
  public String getCurrentUrl() {
    return getDriver().getCurrentUrl();
  }
}
