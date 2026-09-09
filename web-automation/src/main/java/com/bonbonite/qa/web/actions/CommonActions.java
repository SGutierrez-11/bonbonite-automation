package com.bonbonite.qa.web.actions;

import static com.bonbonite.qa.web.actions.WaitActions.isTheElementClickable;
import static com.bonbonite.qa.web.actions.WaitActions.isTheElementVisible;
import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_EXPLICIT;

import com.bonbonite.qa.api.exceptions.CustomException;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Interacciones con los elementos de la interfaz.
 *
 * <p>Los tasks nunca invocan directamente los métodos de Selenium sobre un elemento.
 * Pasan por esta clase, que espera a que el elemento esté disponible y publica el
 * paso en el reporte con una descripción entendible. El resultado es que Allure
 * muestra "Hacer clic en 'Botón iniciar sesión'" en lugar del selector.</p>
 */
@Slf4j
@UtilityClass
public class CommonActions {

  private static final int DEFAULT_CLICK_ATTEMPTS = 3;

  /**
   * Hace clic sobre un elemento.
   *
   * @param element     elemento sobre el que se hace clic
   * @param description nombre del elemento tal como se muestra en el reporte
   * @throws CustomException si el elemento no queda disponible para el clic
   */
  @Step("Hacer clic en '{description}'")
  public static void click(WebElement element, String description) {
    requireClickable(element, description);
    element.click();
  }

  /**
   * Hace clic sobre un elemento reintentando cuando otro elemento intercepta la acción.
   *
   * <p>El sitio muestra banners y animaciones que pueden cubrir el elemento durante
   * unos instantes; reintentar evita fallos que no corresponden a un defecto.</p>
   *
   * @param element     elemento sobre el que se hace clic
   * @param description nombre del elemento tal como se muestra en el reporte
   * @throws CustomException si tras todos los intentos el clic sigue interceptado
   */
  @Step("Hacer clic en '{description}'")
  public static void clickHandlingInterception(WebElement element, String description) {
    for (int attempt = 1; attempt <= DEFAULT_CLICK_ATTEMPTS; attempt++) {
      try {
        requireClickable(element, description);
        element.click();
        return;
      } catch (ElementClickInterceptedException exception) {
        log.debug("Clic interceptado en '{}', intento {}", description, attempt);
        if (attempt == DEFAULT_CLICK_ATTEMPTS) {
          throw new CustomException(String.format(
            "El clic sobre '%s' fue interceptado en los %d intentos realizados",
            description, DEFAULT_CLICK_ATTEMPTS), exception);
        }
      }
    }
  }

  /**
   * Hace clic sobre un elemento ejecutando JavaScript.
   *
   * <p>Reservado para los casos en que el elemento existe y es funcional pero queda
   * fuera del área visible o cubierto por un elemento decorativo.</p>
   *
   * @param element     elemento sobre el que se hace clic
   * @param description nombre del elemento tal como se muestra en el reporte
   */
  @Step("Hacer clic por JavaScript en '{description}'")
  public static void clickByJavaScript(WebElement element, String description) {
    javascriptExecutor().executeScript("arguments[0].click();", element);
  }

  /**
   * Escribe un texto en un campo, limpiándolo antes.
   *
   * @param element     campo donde se escribe
   * @param text        texto a escribir
   * @param description nombre del campo tal como se muestra en el reporte
   */
  @Step("Escribir '{text}' en '{description}'")
  public static void sendKeys(WebElement element, String text, String description) {
    requireVisible(element, description);
    element.clear();
    element.sendKeys(text);
  }

  /**
   * Escribe un valor sensible en un campo sin exponerlo en el reporte.
   *
   * @param element     campo donde se escribe
   * @param text        valor a escribir
   * @param description nombre del campo tal como se muestra en el reporte
   */
  public static void sendSecretKeys(WebElement element, String text, String description) {
    requireVisible(element, description);
    element.clear();
    element.sendKeys(text);
    Allure.step(String.format("Escribir ••••••• en '%s'", description));
  }

  /**
   * Selecciona una opción de una lista desplegable por su texto visible.
   *
   * @param element     lista desplegable
   * @param visibleText texto de la opción a seleccionar
   * @param description nombre de la lista tal como se muestra en el reporte
   */
  @Step("Seleccionar '{visibleText}' en '{description}'")
  public static void selectByVisibleText(WebElement element, String visibleText,
                                         String description) {
    requireVisible(element, description);
    new Select(element).selectByVisibleText(visibleText);
  }

  /**
   * Devuelve el texto visible de un elemento.
   *
   * @param element     elemento a leer
   * @param description nombre del elemento tal como se muestra en el reporte
   * @return texto del elemento, sin espacios sobrantes
   */
  @Step("Obtener el texto de '{description}'")
  public static String getText(WebElement element, String description) {
    requireVisible(element, description);
    return element.getText().trim();
  }

  /**
   * Devuelve el valor de un atributo de un elemento.
   *
   * @param element       elemento a leer
   * @param attributeName nombre del atributo
   * @param description   nombre del elemento tal como se muestra en el reporte
   * @return valor del atributo, o {@code null} si no está presente
   */
  public static String getAttribute(WebElement element, String attributeName,
                                    String description) {
    requireVisible(element, description);
    return element.getDomAttribute(attributeName);
  }

  /**
   * Desplaza la página hasta dejar el elemento centrado en la vista.
   *
   * @param element     elemento a mostrar
   * @param description nombre del elemento tal como se muestra en el reporte
   */
  public static void scrollToElement(WebElement element, String description) {
    javascriptExecutor()
      .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
  }

  private static void requireVisible(WebElement element, String description) {
    if (!isTheElementVisible(element, WAIT_EXPLICIT)) {
      throw new CustomException(String.format(
        "'%s' no fue visible después de %d segundos", description, WAIT_EXPLICIT));
    }
  }

  private static void requireClickable(WebElement element, String description) {
    if (!isTheElementClickable(element, WAIT_EXPLICIT)) {
      throw new CustomException(String.format(
        "'%s' no estuvo disponible para hacer clic después de %d segundos",
        description, WAIT_EXPLICIT));
    }
  }

  private static JavascriptExecutor javascriptExecutor() {
    return (JavascriptExecutor) WebBaseScreen.getDriver();
  }
}
