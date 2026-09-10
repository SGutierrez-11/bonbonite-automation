package com.bonbonite.qa.web.hooks;

import static com.bonbonite.qa.api.context.TestContextManager.cleanTestContext;
import static com.bonbonite.qa.web.driver.BrowserProperties.SCENARIO_PACING_SECONDS;

import com.bonbonite.qa.web.allure.AllureLogger;
import com.bonbonite.qa.web.assertions.SoftAssertManager;
import com.bonbonite.qa.web.driver.Browser;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;

/**
 * Lifecycle of the web scenarios.
 *
 * <p>Hooks only manage resources: they start the browser, consolidate the
 * accumulated assertions and clear the context. No business logic lives here; data
 * preconditions are resolved in the steps, through services.</p>
 *
 * <p>The order of the {@code @After} hooks matters. Cucumber runs the ones with the
 * highest {@code order} first, so assertions are consolidated before the browser is
 * closed: otherwise the Allure listener would not manage to capture the failure
 * screenshot, because the session would already be gone.</p>
 */
@Slf4j
public class Hooks {

  /**
   * Records in the report the environment the suite was launched with.
   */
  @BeforeAll
  public static void reportEnvironment() {
    AllureLogger.writeEnvironmentInfo();
    AllureLogger.copyCategories();
  }

  /**
   * Spaces out the scenarios so the suite stays under the rate limit of the site.
   *
   * <p>This is the one fixed pause the framework allows, and it is not a wait for the
   * interface: it is traffic pacing. The site under test answers {@code 403} when it
   * receives many requests in a short window, and a blocked run reports nothing about
   * the quality of the product. Every other wait in the project is explicit and by
   * condition.</p>
   *
   * <p>The real fix belongs to the client: allowing the addresses that run the suite.
   * Until then, the pause is what keeps the execution usable.</p>
   */
  @Before(value = "@web", order = 10)
  public void paceRequests() {
    if (SCENARIO_PACING_SECONDS <= 0) {
      return;
    }
    Awaitility.await()
      .pollDelay(Duration.ofSeconds(SCENARIO_PACING_SECONDS))
      .atMost(Duration.ofSeconds(SCENARIO_PACING_SECONDS + 5L))
      .until(() -> true);
  }

  /**
   * Starts the browser before every scenario tagged as web.
   *
   * @param scenario scenario about to start
   */
  @Before(value = "@web", order = 0)
  public void initDriver(Scenario scenario) {
    log.info("Starting scenario: {}", scenario.getName());
    WebBaseScreen.setDriver(Browser.createWebDriver());
  }

  /**
   * Consolidates the soft assertions accumulated during the scenario.
   */
  @After(value = "@web", order = 2)
  public void assertAll() {
    SoftAssertManager.assertAll();
  }

  /**
   * Attaches the final evidence when the scenario ends in failure.
   *
   * @param scenario finished scenario
   */
  @After(value = "@web", order = 1)
  public void attachEvidenceOnFailure(Scenario scenario) {
    if (scenario.isFailed()) {
      AllureLogger.attachScreenshot("Estado final del escenario");
      AllureLogger.attachPageSource();
    }
  }

  /**
   * Closes the browser and clears the context when the scenario ends.
   *
   * @param scenario finished scenario
   */
  @After(value = "@web", order = 0)
  public void tearDown(Scenario scenario) {
    log.info("Finished scenario: {} with status {}", scenario.getName(), scenario.getStatus());
    WebBaseScreen.removeDriver();
    cleanTestContext();
  }
}
