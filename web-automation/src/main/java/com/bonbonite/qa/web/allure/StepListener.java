package com.bonbonite.qa.web.allure;

import static com.bonbonite.qa.web.driver.BrowserProperties.SCREENSHOT_ON_STEP;

import io.qameta.allure.listener.StepLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

/**
 * Captura evidencia automáticamente al cerrar cada paso.
 *
 * <p>Se engancha al ciclo de vida de Allure, de modo que ninguna task ni question
 * tiene que acordarse de tomar una captura: cualquier paso que termine en fallo o en
 * error deja su imagen y el HTML de la página en el reporte.</p>
 *
 * <p>Allure lo descubre mediante el archivo de servicio
 * {@code META-INF/services/io.qameta.allure.listener.StepLifecycleListener}. Sin ese
 * archivo la clase se compila pero nunca se invoca.</p>
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
