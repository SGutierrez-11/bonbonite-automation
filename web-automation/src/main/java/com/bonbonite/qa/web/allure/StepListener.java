package com.bonbonite.qa.web.allure;

import static com.bonbonite.qa.web.driver.BrowserProperties.SCREENSHOT_ON_STEP;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

/**
 * Captures evidence automatically when each step closes.
 *
 * <p>It hooks into the Allure lifecycle, so no task or question has to remember to
 * take a screenshot: any step ending in failure or error leaves its image and the
 * page HTML in the report.</p>
 *
 * <p>Allure discovers it through the service file
 * {@code META-INF/services/io.qameta.allure.listener.StepLifecycleListener}. Without
 * that file the class compiles but is never invoked.</p>
 */
public class StepListener implements StepLifecycleListener {

  @Override
  public void beforeStepStop(StepResult result) {
    boolean stepFailed = Status.FAILED.equals(result.getStatus())
      || Status.BROKEN.equals(result.getStatus());

    if (stepFailed) {
      AllureLogger.attachScreenshot("Evidencia del fallo");
      AllureLogger.attachPageSource();
      return;
    }
    if (SCREENSHOT_ON_STEP) {
      AllureLogger.attachScreenshot("Evidencia del paso");
    }
  }
}
