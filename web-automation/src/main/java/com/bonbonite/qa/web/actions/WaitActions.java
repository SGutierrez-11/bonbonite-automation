package com.bonbonite.qa.web.actions;

import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_EXPLICIT;
import static com.bonbonite.qa.web.driver.BrowserProperties.WAIT_IMPLICIT;

import com.bonbonite.qa.web.pages.WebBaseScreen;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Esperas explícitas sobre los elementos de la interfaz.
 *
 * <p>El sitio carga buena parte de su contenido después del evento de carga de la
 * página, de modo que consultar un elemento apenas navegar produce resultados
 * intermitentes. Todas las esperas del framework pasan por aquí y ninguna usa
 * pausas fijas: se espera por una condición observable, no por un tiempo.</p>
 *
 * <p>Antes de esperar se desactiva la espera implícita del driver y se restaura al
 * terminar. Mezclar ambas esperas produce tiempos impredecibles, porque cada
 * consulta interna de la espera explícita heredaría el tiempo de la implícita.</p>
 */
@Slf4j
@UtilityClass
public class WaitActions {

  /**
   * Espera a que un elemento sea visible.
   *
   * @param element          elemento a observar
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si el elemento se hizo visible dentro del tiempo indicado
   */
  public static boolean isTheElementVisible(WebElement element, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.visibilityOf(element), timeoutInSeconds) != null;
  }

  /**
   * Espera a que un elemento sea visible usando el tiempo por defecto.
   *
   * @param element elemento a observar
   * @return {@code true} si el elemento se hizo visible
   */
  public static boolean isTheElementVisible(WebElement element) {
    return isTheElementVisible(element, WAIT_EXPLICIT);
  }

  /**
   * Espera a que un elemento esté habilitado para recibir un clic.
   *
   * @param element          elemento a observar
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si el elemento quedó disponible para interactuar
   */
  public static boolean isTheElementClickable(WebElement element, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.elementToBeClickable(element), timeoutInSeconds) != null;
  }

  /**
   * Espera a que un elemento desaparezca de la vista.
   *
   * @param element          elemento a observar
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si el elemento dejó de ser visible
   */
  public static boolean isTheElementInvisible(WebElement element, int timeoutInSeconds) {
    return Boolean.TRUE.equals(
      waitFor(ExpectedConditions.invisibilityOf(element), timeoutInSeconds));
  }

  /**
   * Espera a que una lista de elementos tenga al menos un resultado visible.
   *
   * @param elements         lista a observar
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si la lista se pobló dentro del tiempo indicado
   */
  public static boolean areTheElementsVisible(List<WebElement> elements, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.visibilityOfAllElements(elements), timeoutInSeconds) != null;
  }

  /**
   * Espera a que un elemento contenga el texto indicado.
   *
   * @param element          elemento a observar
   * @param expectedText     texto que debe aparecer
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si el texto apareció dentro del tiempo indicado
   */
  public static boolean hasTheElementText(WebElement element, String expectedText,
                                          int timeoutInSeconds) {
    return Boolean.TRUE.equals(
      waitFor(ExpectedConditions.textToBePresentInElement(element, expectedText),
        timeoutInSeconds));
  }

  /**
   * Espera a que la dirección del navegador contenga el fragmento indicado.
   *
   * @param urlFragment      fragmento que debe aparecer en la URL
   * @param timeoutInSeconds segundos máximos de espera
   * @return {@code true} si la URL contuvo el fragmento dentro del tiempo indicado
   */
  public static boolean isTheUrlContaining(String urlFragment, int timeoutInSeconds) {
    return Boolean.TRUE.equals(
      waitFor(ExpectedConditions.urlContains(urlFragment), timeoutInSeconds));
  }

  private static <T> T waitFor(Function<WebDriver, T> condition, int timeoutInSeconds) {
    WebDriver driver = WebBaseScreen.getDriver();
    setImplicitWait(driver, 0);
    try {
      return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds))
        .ignoring(StaleElementReferenceException.class)
        .until(condition);
    } catch (TimeoutException | NoSuchElementException exception) {
      log.debug("La condición no se cumplió en {} segundos", timeoutInSeconds);
      return null;
    } finally {
      setImplicitWait(driver, WAIT_IMPLICIT);
    }
  }

  private static void setImplicitWait(WebDriver driver, int seconds) {
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
  }
}
