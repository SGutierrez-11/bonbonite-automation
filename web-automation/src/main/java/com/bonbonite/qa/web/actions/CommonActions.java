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
 * Interactions with the interface elements.
 *
 * <p>Tasks never call Selenium methods on an element directly. They go through this
 * class, which waits for the element to be available and publishes the step in the
 * report with a readable description. As a result Allure shows "Hacer clic en
 * 'Botón iniciar sesión'" instead of a CSS selector.</p>
 */
@Slf4j
@UtilityClass
public class CommonActions {

  private static final int DEFAULT_CLICK_ATTEMPTS = 3;

  /**
   * Clicks on an element.
   *
   * @param element     element to click
   * @param description element name as displayed in the report
   * @throws CustomException if the element never becomes clickable
   */
  @Step("Hacer clic en '{description}'")
  public static void click(WebElement element, String description) {
    requireClickable(element, description);
    element.click();
  }

  /**
   * Clicks on an element retrying when another element intercepts the action.
   *
   * <p>The site displays banners and animations that may cover the element for a
   * moment; retrying avoids failures that do not correspond to a defect.</p>
   *
   * @param element     element to click
   * @param description element name as displayed in the report
   * @throws CustomException if the click is still intercepted after every attempt
   */
  @Step("Hacer clic en '{description}'")
  public static void clickHandlingInterception(WebElement element, String description) {
    for (int attempt = 1; attempt <= DEFAULT_CLICK_ATTEMPTS; attempt++) {
      try {
        requireClickable(element, description);
        element.click();
        return;
      } catch (ElementClickInterceptedException exception) {
        log.debug("Click intercepted on '{}', attempt {}", description, attempt);
        if (attempt == DEFAULT_CLICK_ATTEMPTS) {
          throw new CustomException(String.format(
            "El clic sobre '%s' fue interceptado en los %d intentos realizados",
            description, DEFAULT_CLICK_ATTEMPTS), exception);
        }
      }
    }
  }

  /**
   * Clicks on an element by executing JavaScript.
   *
   * <p>Reserved for the cases where the element exists and works but sits outside
   * the visible area or is covered by a decorative element.</p>
   *
   * @param element     element to click
   * @param description element name as displayed in the report
   */
  @Step("Hacer clic por JavaScript en '{description}'")
  public static void clickByJavaScript(WebElement element, String description) {
    javascriptExecutor().executeScript("arguments[0].click();", element);
  }

  /**
   * Types a text into a field, clearing it first.
   *
   * @param element     field to type into
   * @param text        text to type
   * @param description field name as displayed in the report
   */
  @Step("Escribir '{text}' en '{description}'")
  public static void sendKeys(WebElement element, String text, String description) {
    requireVisible(element, description);
    element.clear();
    element.sendKeys(text);
  }

  /**
   * Types a sensitive value into a field without exposing it in the report.
   *
   * @param element     field to type into
   * @param text        value to type
   * @param description field name as displayed in the report
   */
  public static void sendSecretKeys(WebElement element, String text, String description) {
    requireVisible(element, description);
    element.clear();
    element.sendKeys(text);
    Allure.step(String.format("Escribir ••••••• en '%s'", description));
  }

  /**
   * Selects an option of a dropdown list by its visible text.
   *
   * @param element     dropdown list
   * @param visibleText text of the option to select
   * @param description list name as displayed in the report
   */
  @Step("Seleccionar '{visibleText}' en '{description}'")
  public static void selectByVisibleText(WebElement element, String visibleText,
                                         String description) {
    requireVisible(element, description);
    new Select(element).selectByVisibleText(visibleText);
  }

  /**
   * Ticks a checkbox when it is not already selected.
   *
   * <p>Some checkboxes of the site are visually replaced by a styled element, so the
   * click is delegated to JavaScript when the native one is intercepted.</p>
   *
   * @param element     checkbox to tick
   * @param description element name as displayed in the report
   */
  @Step("Marcar '{description}'")
  public static void check(WebElement element, String description) {
    if (element.isSelected()) {
      return;
    }
    try {
      clickHandlingInterception(element, description);
    } catch (CustomException exception) {
      log.debug("Native click failed on '{}', falling back to JavaScript", description);
      clickByJavaScript(element, description);
    }
  }

  /**
   * Returns the visible text of an element.
   *
   * @param element     element to read
   * @param description element name as displayed in the report
   * @return the element text, trimmed
   */
  @Step("Obtener el texto de '{description}'")
  public static String getText(WebElement element, String description) {
    requireVisible(element, description);
    return element.getText().trim();
  }

  /**
   * Returns the current value of a form field.
   *
   * <p>It reads the DOM property rather than the HTML attribute: the attribute keeps
   * the value the page was rendered with, so after typing into the field it would
   * report a stale result.</p>
   *
   * @param element     field to read
   * @param description element name as displayed in the report
   * @return the current value of the field, never null
   */
  @Step("Obtener el valor de '{description}'")
  public static String getValue(WebElement element, String description) {
    requireVisible(element, description);
    String value = element.getDomProperty("value");
    return value == null ? "" : value;
  }

  /**
   * Returns the value of an element attribute.
   *
   * @param element       element to read
   * @param attributeName attribute name
   * @param description   element name as displayed in the report
   * @return the attribute value, or {@code null} when it is not present
   */
  public static String getAttribute(WebElement element, String attributeName,
                                    String description) {
    requireVisible(element, description);
    return element.getDomAttribute(attributeName);
  }

  /**
   * Scrolls the page until the element is centered in the view.
   *
   * <p>It waits for the element first. Some forms of the site are rendered by
   * JavaScript after the page load and replace their own markup, so scrolling without
   * waiting hits the window where the element does not exist yet.</p>
   *
   * @param element     element to bring into view
   * @param description element name as displayed in the report
   */
  public static void scrollToElement(WebElement element, String description) {
    requireVisible(element, description);
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
