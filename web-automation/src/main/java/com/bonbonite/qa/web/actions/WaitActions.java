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
 * Explicit waits over the interface elements.
 *
 * <p>The site loads a good part of its content after the page load event, so
 * querying an element right after navigating produces flaky results. Every wait of
 * the framework goes through this class and none of them uses a fixed pause: they
 * wait for an observable condition, not for an amount of time.</p>
 *
 * <p>Before waiting, the driver implicit wait is disabled and restored afterwards.
 * Mixing both waits produces unpredictable timings, because every internal lookup
 * performed by the explicit wait would inherit the implicit one.</p>
 */
@Slf4j
@UtilityClass
public class WaitActions {

  /**
   * Waits until an element becomes visible.
   *
   * @param element          element to observe
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the element became visible within the given time
   */
  public static boolean isTheElementVisible(WebElement element, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.visibilityOf(element), timeoutInSeconds) != null;
  }

  /**
   * Waits until an element becomes visible using the default timeout.
   *
   * @param element element to observe
   * @return {@code true} if the element became visible
   */
  public static boolean isTheElementVisible(WebElement element) {
    return isTheElementVisible(element, WAIT_EXPLICIT);
  }

  /**
   * Waits until an element is ready to receive a click.
   *
   * @param element          element to observe
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the element became clickable
   */
  public static boolean isTheElementClickable(WebElement element, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.elementToBeClickable(element), timeoutInSeconds) != null;
  }

  /**
   * Waits until an element disappears from the view.
   *
   * @param element          element to observe
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the element stopped being visible
   */
  public static boolean isTheElementInvisible(WebElement element, int timeoutInSeconds) {
    return Boolean.TRUE.equals(
      waitFor(ExpectedConditions.invisibilityOf(element), timeoutInSeconds));
  }

  /**
   * Waits until every element of a list becomes visible.
   *
   * @param elements         list to observe
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the list was populated within the given time
   */
  public static boolean areTheElementsVisible(List<WebElement> elements, int timeoutInSeconds) {
    return waitFor(ExpectedConditions.visibilityOfAllElements(elements), timeoutInSeconds) != null;
  }

  /**
   * Waits until an element contains the given text.
   *
   * @param element          element to observe
   * @param expectedText     text that must show up
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the text showed up within the given time
   */
  public static boolean hasTheElementText(WebElement element, String expectedText,
                                          int timeoutInSeconds) {
    return Boolean.TRUE.equals(
      waitFor(ExpectedConditions.textToBePresentInElement(element, expectedText),
        timeoutInSeconds));
  }

  /**
   * Waits until the browser address contains the given fragment.
   *
   * @param urlFragment      fragment that must show up in the URL
   * @param timeoutInSeconds maximum seconds to wait
   * @return {@code true} if the URL contained the fragment within the given time
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
      log.debug("The condition was not met within {} seconds", timeoutInSeconds);
      return null;
    } finally {
      setImplicitWait(driver, WAIT_IMPLICIT);
    }
  }

  private static void setImplicitWait(WebDriver driver, int seconds) {
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
  }
}
