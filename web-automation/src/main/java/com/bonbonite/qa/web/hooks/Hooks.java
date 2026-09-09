package com.bonbonite.qa.web.hooks;

import static com.bonbonite.qa.api.context.TestContextManager.cleanTestContext;

import com.bonbonite.qa.web.allure.AllureLogger;
import com.bonbonite.qa.web.assertions.SoftAssertManager;
import com.bonbonite.qa.web.driver.Browser;
import com.bonbonite.qa.web.pages.WebBaseScreen;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;

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
